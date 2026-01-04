package taskmanager.user.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import taskmanager.user.dto.CreateUserRequest;
import taskmanager.user.dto.UserResponse;
import taskmanager.common.event.UserCreatedEvent;
import taskmanager.user.model.User;
import taskmanager.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       KafkaTemplate<String, UserCreatedEvent> kafkaTemplate,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with email " + request.getEmail() + " already exists");
        }

        String role = request.getRole() != null ? request.getRole() : "USER";
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(request.getName(), request.getEmail(), role, hashedPassword);
        User saved = userRepository.save(user);

        UserCreatedEvent event = new UserCreatedEvent(saved.getId(), saved.getName(), saved.getEmail(), saved.getRole());
        kafkaTemplate.send("user.created", event);

        return new UserResponse(saved.getId(), saved.getName(), saved.getEmail(), saved.getRole());
    }

    public List<UserResponse> getAll() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole()
                )).collect(Collectors.toList());
    }
}
