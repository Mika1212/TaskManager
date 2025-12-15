package taskmanager.task.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import taskmanager.common.event.ProjectCreatedEvent;
import taskmanager.task.dto.CreateTaskRequest;
import taskmanager.task.service.TaskService;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectCreatedListener {

    private final TaskService taskService;

    @KafkaListener(
            topics = "project.created",
            groupId = "task-service",
            containerFactory = "projectCreatedKafkaListenerContainerFactory"
    )
    public void handleProjectCreated(ProjectCreatedEvent event) {
        log.info("New project received: {}", event);

        taskService.createTask(new CreateTaskRequest(
                "Welcome Task",
                "This is your first task in project " + event.getProjectName(),
                event.getProjectId(),
                event.getOwnerId()
                )
        );
    }
}
