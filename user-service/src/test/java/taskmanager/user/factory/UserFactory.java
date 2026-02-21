package taskmanager.user.factory;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import taskmanager.user.model.User;

@Component
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
                .passwordHash(passwordEncoder.encode("123456"))
                .build();
    }

    public User createWithEmailAndPassword(String email, String password) {
        return User.builder()
                .name("John")
                .email(email)
                .role("USER")
                .passwordHash(passwordEncoder.encode(password))
                .build();
    }
}
