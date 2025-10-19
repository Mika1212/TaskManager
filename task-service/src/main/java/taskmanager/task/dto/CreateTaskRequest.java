package taskmanager.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateTaskRequest {
    private String title;
    private String description;
    private Long assigneeId;
    private Long projectId = null;
}
