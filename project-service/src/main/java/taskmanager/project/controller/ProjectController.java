package taskmanager.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import taskmanager.project.dto.CreateProjectRequest;
import taskmanager.project.dto.ProjectResponse;
import taskmanager.project.service.ProjectService;

import java.util.List;

@RestController
@RequestMapping("api/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public ProjectResponse create(@RequestBody CreateProjectRequest request) {
        return projectService.createProject(request);
    }

    @GetMapping("owner/{ownerId}")
    public List<ProjectResponse> getByOwner(@PathVariable Long ownerId) {
        return projectService.getProjectOwnerId(ownerId);
    }
}
