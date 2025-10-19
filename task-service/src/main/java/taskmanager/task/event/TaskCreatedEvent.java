package taskmanager.task.event;

import lombok.Getter;
import taskmanager.task.model.TaskStatus;

@Getter
public class TaskCreatedEvent {
    private final Long taskId;
    private final Long assigneeId;
    private final TaskStatus taskStatus;
    private final Long projectId;

    public TaskCreatedEvent(Long taskId, Long assigneeId, TaskStatus taskStatus) {
        this(taskId, assigneeId, taskStatus, null);
    }

    public TaskCreatedEvent(Long taskId, Long assigneeId, TaskStatus taskStatus, Long projectId) {
        this.taskId = taskId;
        this.assigneeId = assigneeId;
        this.taskStatus = taskStatus;
        this.projectId = projectId;
    }
}
