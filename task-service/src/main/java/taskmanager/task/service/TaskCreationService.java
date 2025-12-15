package taskmanager.task.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import taskmanager.task.model.Task;
import taskmanager.task.model.TaskStatus;
import taskmanager.task.repository.TaskRepository;

@Service
@RequiredArgsConstructor
public class TaskCreationService {
    private final TaskRepository taskRepository;

    public void createWelcomingTask(Long userId) {
        Task task = new Task(
                "Welcome! Set up your profile",
                "Add your photo, bio, and contact details",
                TaskStatus.NEW,
                userId
        );

        taskRepository.save(task);
    }
}
