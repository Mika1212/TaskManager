package taskmanager.user.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import taskmanager.common.event.UserCreatedEvent;
import taskmanager.user.dto.CreateUserRequest;
import taskmanager.user.dto.LoginRequest;
import taskmanager.user.factory.UserFactory;
import taskmanager.user.model.User;
import taskmanager.user.repository.UserRepository;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class LoginControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    void register_shouldReturnToken() {
        CreateUserRequest request = CreateUserRequest.builder()
                .name("user")
                .email("email@email.ru")
                .password("password")
                .build();

        var response = restTemplate.postForEntity("/api/auth/register", request, Map.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().get("token"));
    }

    @Test
    void login_shouldReturnToken() {
        UserFactory userFactory = new UserFactory(passwordEncoder);
        User user = userFactory.createWithEmailAndPassword("test@example.com", "password");
        userRepository.save(user);

        LoginRequest request = new LoginRequest("test@example.com", "password");
        var response = restTemplate.postForEntity("/api/auth/login", request, Map.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().get("token"));
    }
}
