package taskmanager.task.dto;

import lombok.Getter;
import taskmanager.task.model.TaskStatus;

@Getter
public class TaskResponse {
    private final Long id;
    private final String title;
    private final String description;
    private final TaskStatus status;
    private final Long assigneeId;

    public TaskResponse(Long id, String title, String description, TaskStatus status, Long assigneeId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.assigneeId = assigneeId;
    }
}
