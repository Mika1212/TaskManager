package taskmanager.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import taskmanager.common.event.ProjectCreatedEvent;
import taskmanager.project.model.Project;
import taskmanager.project.repository.ProjectRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final KafkaTemplate<String, ProjectCreatedEvent> kafkaTemplate;

    public Project createProject(Long ownerId, String name, String description) {
        Project project = new Project();
        project.setName(name);
        project.setDescription(description);
        project.setOwnerId(ownerId);
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());
        project = projectRepository.save(project);

        kafkaTemplate.send("project.created", new ProjectCreatedEvent(
                project.getId(),
                project.getName(),
                project.getOwnerId()
        ));

        return project;
    }

    public List<Project> getProjectOwnerId(Long ownerId) {
        return projectRepository.findByOwnerId(ownerId);
    }
}
