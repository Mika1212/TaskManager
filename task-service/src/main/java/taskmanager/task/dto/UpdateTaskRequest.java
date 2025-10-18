package taskmanager.task.dto;

import lombok.Data;
import taskmanager.task.model.TaskStatus;

@Data
public class UpdateTaskRequest {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private Long assigneeId;
}
