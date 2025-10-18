package taskmanager.task.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import taskmanager.common.event.UserCreatedEvent;

@Service
@Slf4j
public class UserCreatedListener {

    @KafkaListener(
            topics = "user.created",
            groupId = "task-service",
            containerFactory = "userCreatedKafkaListenerContainerFactory"
    )
    public void handleUserCreated(UserCreatedEvent event) {
        System.out.println("Received message: " + event);
        // Todo добавить постановку задачи на сетап профиля новомым пользователям
    }
}
