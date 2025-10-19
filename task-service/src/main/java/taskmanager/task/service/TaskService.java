package taskmanager.task.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import taskmanager.task.dto.CreateTaskRequest;
import taskmanager.task.dto.TaskResponse;
import taskmanager.task.event.TaskCreatedEvent;
import taskmanager.task.model.Task;
import taskmanager.task.model.TaskStatus;
import taskmanager.task.repository.TaskRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public TaskService(TaskRepository taskRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.taskRepository = taskRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public TaskResponse createTask(CreateTaskRequest request) {
        Task task = new Task(request.getTitle(), request.getDescription(), TaskStatus.NEW, request.getAssigneeId(), request.getProjectId());
        Task saved = taskRepository.save(task);

        TaskCreatedEvent event = new TaskCreatedEvent(saved.getId(), saved.getAssigneeId(), saved.getStatus(), saved.getProjectId());
        kafkaTemplate.send("task_created", event);

        return new TaskResponse(saved.getId(), saved.getTitle(), saved.getDescription(), saved.getStatus(), saved.getAssigneeId());
    }

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(task -> new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), task.getAssigneeId()))
                .collect(Collectors.toList());
    }

    public List<TaskResponse> getTasksByUser(Long userId) {
        return taskRepository.findByAssigneeId(userId)
                .stream()
                .map(task -> new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), task.getAssigneeId()))
                .collect(Collectors.toList());
    }
}
