package taskmanager.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCreatedEvent {
    private Long projectId;
    private String projectName;
    private Long ownerId;
}
