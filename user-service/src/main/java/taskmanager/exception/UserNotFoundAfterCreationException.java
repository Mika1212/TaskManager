package taskmanager.exception;

public class UserNotFoundAfterCreationException extends RuntimeException {
    public UserNotFoundAfterCreationException(String email) {
        super("User not found after creation: " + email);
    }
}
