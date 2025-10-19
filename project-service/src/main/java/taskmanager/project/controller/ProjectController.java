package taskmanager.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import taskmanager.project.dto.CreateProjectRequest;
import taskmanager.project.model.Project;
import taskmanager.project.service.ProjectService;

import java.util.List;

@RestController
@RequestMapping("api/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public Project create(@RequestBody CreateProjectRequest request) {
        return projectService.createProject(request.getOwnerId(), request.getName(), request.getDescription());
    }

    @GetMapping("owner/{ownerId}")
    public List<Project> getByOwner(@PathVariable Long ownerId) {
        return projectService.getProjectOwnerId(ownerId);
    }
}
