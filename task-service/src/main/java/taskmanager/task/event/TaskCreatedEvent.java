package taskmanager.task.event;

import lombok.Getter;
import taskmanager.task.model.TaskStatus;

@Getter
public class TaskCreatedEvent {
    private final Long taskId;
    private final Long assigneeId;
    private final TaskStatus taskStatus;

    public TaskCreatedEvent(Long taskId, Long assigneeId, TaskStatus taskStatus) {
        this.taskId = taskId;
        this.assigneeId = assigneeId;
        this.taskStatus = taskStatus;
    }
}
