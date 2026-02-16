package taskmanager.user.factory;

import taskmanager.user.model.User;

import java.util.concurrent.atomic.AtomicLong;

public final class UserTestFactory {

    private static final AtomicLong idCounter = new AtomicLong(1);

    private UserTestFactory() {
    }

    public static User validUser() {
        return User.builder()
                .id(idCounter.getAndIncrement())
                .name("John")
                .email("test@example.com")
                .role("USER")
                .passwordHash("hashed-password")
                .build();
    }

    public static User withEmail(String email) {
        return validUser().toBuilder()
                .email(email)
                .build();
    }

    public static User withEmailAndPasswordHash(String email, String passwordHash) {
        return validUser().toBuilder()
                .email(email)
                .passwordHash(passwordHash)
                .build();
    }
}
