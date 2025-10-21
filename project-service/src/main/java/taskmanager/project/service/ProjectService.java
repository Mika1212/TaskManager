package taskmanager.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import taskmanager.common.event.ProjectCreatedEvent;
import taskmanager.project.dto.CreateProjectRequest;
import taskmanager.project.dto.ProjectResponse;
import taskmanager.project.model.Project;
import taskmanager.project.repository.ProjectRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final KafkaTemplate<String, ProjectCreatedEvent> kafkaTemplate;

    public ProjectResponse createProject(CreateProjectRequest request) {
        Project project = new Project(request.getName(), request.getDescription(), request.getOwnerId());
        Project saved = projectRepository.save(project);

        kafkaTemplate.send("project.created", new ProjectCreatedEvent(
                saved.getId(),
                saved.getName(),
                saved.getOwnerId()
        ));

        return new ProjectResponse(saved.getName(), saved.getDescription(), saved.getOwnerId());
    }

    public List<ProjectResponse> getProjectOwnerId(Long ownerId) {
        return projectRepository.findByOwnerId(ownerId)
                .stream()
                .map(project -> new ProjectResponse(project.getName(), project.getDescription(), project.getOwnerId()))
                .collect(Collectors.toList());
    }
}
