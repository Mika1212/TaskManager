package taskmanager.task.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private Long assigneeId;
    private Long projectId;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Task(String title, String description, TaskStatus status, Long assigneeId) {
        this(title, description, status, assigneeId, null);
    }

    public Task(String title, String description, TaskStatus status, Long assigneeId, Long projectId) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.assigneeId = assigneeId;
        this.projectId = projectId;
    }
}
