package taskmanager.task.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import taskmanager.common.event.UserCreatedEvent;
import taskmanager.task.service.TaskCreationService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCreatedListener {
    private final TaskCreationService taskCreationService;

    @KafkaListener(
            topics = "user.created",
            groupId = "task-service",
            containerFactory = "userCreatedKafkaListenerContainerFactory"
    )
    public void handleUserCreated(UserCreatedEvent event) {
        log.info("Received message: {}", event);
        taskCreationService.createWelcomingTask(event.getUserId());
    }
}
