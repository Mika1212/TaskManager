package taskmanager.user.factory;

import taskmanager.user.model.User;

public final class UserTestFactory {

    private UserTestFactory() {
    }

    public static User validUser() {
        return User.builder()
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
