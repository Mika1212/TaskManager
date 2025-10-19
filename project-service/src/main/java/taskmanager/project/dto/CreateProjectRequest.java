package taskmanager.project.dto;

import lombok.Data;

@Data
public class CreateProjectRequest {
    Long ownerId;
    String name;
    String description;
}
