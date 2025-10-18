package taskmanager.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taskmanager.task.model.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssigneeId(Long assigneeId);
}
