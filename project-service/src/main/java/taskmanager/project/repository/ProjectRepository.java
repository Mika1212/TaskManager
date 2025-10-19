package taskmanager.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taskmanager.project.model.Project;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByOwnerId(Long OwnerId);
}
