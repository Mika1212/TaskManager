package taskmanager.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import taskmanager.common.event.UserCreatedEvent;
import taskmanager.user.factory.UserFactory;
import taskmanager.user.repository.UserRepository;

import java.util.Map;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.liquibase.enabled=false",
        "spring.profiles.active=test"
})
@AutoConfigureMockMvc
@Transactional
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserFactory userFactory;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;

    private static final String REGISTER_URL = "/api/auth/register";
    private static final String LOGIN_URL = "/api/auth/login";
    private static final String ME_URL = "/api/auth/me";

    private static final String DEFAULT_PASSWORD = "123456";
    private static final String DEFAULT_ROLE = "USER";

    private String toJson(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    private void createTestUser(String email) {
        userRepository.save(userFactory.createWithEmailAndPassword(email, DEFAULT_PASSWORD));
    }

    private String getJwtForUser(String email, String password) throws Exception {
        var loginRequest = Map.of(
                "email", email,
                "password", password
        );

        String response = mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(loginRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response).get("token").asText();
    }

    private ResultActions performRegister(String name, String email, String password) throws Exception {
        var registerRequest = Map.of(
                "name", name,
                "email", email,
                "password", password,
                "role", DEFAULT_ROLE
        );
        return mockMvc.perform(post(REGISTER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(registerRequest)));
    }

    private ResultActions performLogin(String email, String password) throws Exception {
        var loginRequest = Map.of(
                "email", email,
                "password", password
        );
        return mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(loginRequest)));
    }

    private ResultActions performGetMe(String jwt) throws Exception {
        return mockMvc.perform(get(ME_URL)
                .header("Authorization", "Bearer " + jwt));
    }

    @Test
    void register_ShouldReturnToken_WhenValid() throws Exception {
        performRegister("John", "test@mail.com", DEFAULT_PASSWORD)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void register_ShouldFail_WhenEmailIsInvalid() throws Exception {
        performRegister("John", "invalid-email", DEFAULT_PASSWORD)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[*].field").value(hasItem("email")))
                .andExpect(jsonPath("$.errors[*].message").value(hasItem("Email must be valid")));
    }

    @Test
    void register_ShouldFail_WhenEmailIsBlank() throws Exception {
        performRegister("John", "", DEFAULT_PASSWORD)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[*].field").value(hasItem("email")))
                .andExpect(jsonPath("$.errors[*].message").value(hasItem("Email is required")));
    }

    @Test
    void register_ShouldFail_WhenPasswordIsInvalid() throws Exception {
        performRegister("John", "test@mail.com", "")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[*].field").value(hasItem("password")))
                .andExpect(jsonPath("$.errors[*].message")
                        .value(hasItem("Password must be at least 6 characters")))
                .andExpect(jsonPath("$.errors[*].message")
                        .value(hasItem("Password is required")));
    }

    @Test
    void register_ShouldFail_WhenNameIsEmpty() throws Exception {
        performRegister("", "test3@mail.com", DEFAULT_PASSWORD)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[*].field").value(hasItem("name")))
                .andExpect(jsonPath("$.errors[*].message")
                        .value(hasItem("Name is required")));
    }

    @Test
    void register_ShouldFail_WhenEmailAlreadyExists() throws Exception {
        String email = "duplicate@mail.com";
        createTestUser(email);

        performRegister("John", email, DEFAULT_PASSWORD)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errors[*].field").value(hasItem("email")))
                .andExpect(jsonPath("$.errors[*].message")
                        .value(hasItem("User with email: \"" + email + "\" already exists")));
    }

    @Test
    void login_ShouldReturnToken_WhenValid() throws Exception {
        String email = "login@mail.com";
        createTestUser(email);

        performLogin(email, DEFAULT_PASSWORD)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void login_ShouldFail_WhenPasswordIncorrect() throws Exception {
        String email = "wrongpass@mail.com";
        createTestUser(email);

        performLogin(email, "wrong123")
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errors[*].field").doesNotExist())
                .andExpect(jsonPath("$.errors[*].message")
                        .value(hasItem("Invalid credentials")));
    }

    @Test
    void login_ShouldFail_WhenEmailIsBlank() throws Exception {
        performLogin("", DEFAULT_PASSWORD)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[*].field").value(hasItem("email")))
                .andExpect(jsonPath("$.errors[*].message")
                        .value(hasItem("Email is required")));
    }

    @Test
    void login_ShouldFail_WhenEmailIsInvalid() throws Exception {
        String email = "not.an.email";

        performLogin(email, DEFAULT_PASSWORD)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[*].field").value(hasItem("email")))
                .andExpect(jsonPath("$.errors[*].message")
                        .value(hasItem("Email must be valid")));
    }

    @Test
    void login_ShouldFail_WhenPasswordIsInvalid() throws Exception {
        performLogin("login@mail.com", "")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[*].field").value(hasItem("password")))
                .andExpect(jsonPath("$.errors[*].message")
                        .value(hasItem("Password is required")))
                .andExpect(jsonPath("$.errors[*].message")
                        .value(hasItem("Password must be at least 6 characters")));
    }

    @Test
    void login_ShouldFail_WhenUserNotExists() throws Exception {
        performLogin("nouser@mail.com", DEFAULT_PASSWORD)
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errors[*].field").doesNotExist())
                .andExpect(jsonPath("$.errors[*].message")
                        .value(hasItem("Invalid credentials")));
    }

    @Test
    void me_ShouldReturnUserId_WhenAuthenticated() throws Exception {
        String email = "me@mail.com";
        performRegister("Mark", email, DEFAULT_PASSWORD);

        String jwt = getJwtForUser(email, DEFAULT_PASSWORD);

        performGetMe(jwt)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").isNotEmpty());
    }

    @Test
    void me_ShouldFail_WhenUnauthenticated() throws Exception {
        performGetMe(null)
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errors[*].field").doesNotExist())
                .andExpect(jsonPath("$.errors[*].message")
                        .value(hasItem("Unauthorized")));
    }

    @Test
    void me_ShouldFail_WhenTokenInvalid() throws Exception {
        performGetMe("invalid.token")
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errors[*].field").doesNotExist())
                .andExpect(jsonPath("$.errors[*].message")
                        .value(hasItem("Unauthorized")));
    }
}
