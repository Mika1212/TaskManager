package taskmanager.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import taskmanager.user.dto.CreateUserRequest;
import taskmanager.user.dto.LoginRequest;
import taskmanager.user.dto.UserResponse;
import taskmanager.user.model.User;
import taskmanager.user.repository.UserRepository;
import taskmanager.user.service.UserService;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(loginRequest.password(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtService.generateToken(user);
    }

    public String register(CreateUserRequest request) {
        UserResponse userResponse = userService.createUser(request);

        User user = userRepository.findByEmail(userResponse.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found after creation"));

        return jwtService.generateToken(user);
    }
}

