package taskmanager.user.factory;

import org.springframework.security.crypto.password.PasswordEncoder;
import taskmanager.user.model.User;

public class UserFactory {

    private final PasswordEncoder passwordEncoder;

    public UserFactory(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User createDefault() {
        return User.builder()
                .name("John")
                .email("default@example.com")
                .role("USER")
                .passwordHash("$2a$10$XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX")
                .build();
    }

    public User createWithEmail(String email) {
        User user = createDefault();
        user.setEmail(email);
        return user;
    }

    public User createWithEmailAndPassword(String email, String password) {
        User user = createDefault();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        return user;
    }
}
