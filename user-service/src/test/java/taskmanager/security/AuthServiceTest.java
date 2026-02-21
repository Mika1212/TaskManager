package taskmanager.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import taskmanager.exception.InvalidCredentialsException;
import taskmanager.user.dto.CreateUserRequest;
import taskmanager.user.dto.LoginRequest;
import taskmanager.user.dto.UserResponse;
import taskmanager.user.factory.UserTestFactory;
import taskmanager.user.model.User;
import taskmanager.user.repository.UserRepository;
import taskmanager.user.service.UserService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldReturnTokenWhenCredentialsAreValid() {
        String email = "test@example.com";
        String rawPassword = "password";

        LoginRequest request = new LoginRequest(email, rawPassword);

        User user = UserTestFactory.withEmailAndPasswordHash(email, "hashed-password");

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(rawPassword, "hashed-password"))
                .thenReturn(true);

        when(jwtService.generateToken(user))
                .thenReturn("jwt-token");

        String token = authService.login(request);

        assertThat(token).isEqualTo("jwt-token");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        String email = "notfound@example.com";

        LoginRequest request = new LoginRequest(email, "password");

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsInvalid() {
        String email = "test@example.com";
        String rawPassword = "password";

        LoginRequest request = new LoginRequest(email, rawPassword);

        User user = UserTestFactory.withEmailAndPasswordHash(email, "hashed-password");

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(rawPassword, "hashed-password"))
                .thenReturn(false);

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(InvalidCredentialsException.class);

        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void shouldRegisterUserAndReturnToken() {
        String email = "test@example.com";

        CreateUserRequest request = CreateUserRequest.builder()
                .name("John")
                .email(email)
                .password("password")
                .role("USER")
                .build();

        UserResponse response = new UserResponse();
        response.setEmail(email);

        User user = UserTestFactory.withEmail(email);

        when(userService.createUser(request))
                .thenReturn(user);

        when(jwtService.generateToken(user))
                .thenReturn("jwt-token");

        String token = authService.register(request);

        assertThat(token).isEqualTo("jwt-token");

        verify(userService).createUser(request);
    }
}
