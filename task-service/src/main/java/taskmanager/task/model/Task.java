package taskmanager.task.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Getter
@Setter
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

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Task(String title, String description, TaskStatus status, Long assigneeId) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.assigneeId = assigneeId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
