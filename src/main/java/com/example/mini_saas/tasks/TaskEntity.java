package com.example.mini_saas.tasks;

import com.example.mini_saas.users.UserEntity;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;

/**
 * Entity representing a task owned by a user.
 */
@Entity
@Table(name = "tasks", indexes = {
        @Index(name = "idx_task_owner", columnList = "owner_id"),
        @Index(name = "idx_task_completed", columnList = "completed"),
        @Index(name = "idx_task_created_at", columnList = "created_at")
})
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TaskStatus status = TaskStatus.TODO;

    @Column(nullable = false)
    private boolean completed = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    @Column(nullable = false, updatable = false, name = "created_at")
    private Instant createdAt;

    @Column(nullable = false, name = "updated_at")
    private Instant updatedAt;

    protected TaskEntity() {
        // JPA only
    }

    public TaskEntity(String title, UserEntity owner) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (owner == null) {
            throw new IllegalArgumentException("Owner cannot be null");
        }
        this.title = title;
        this.owner = owner;
        this.completed = false;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    public void start() {
        if (this.status != TaskStatus.TODO) {
            throw new IllegalStateException("Can only start tasks in TODO status");
        }
        this.status = TaskStatus.IN_PROGRESS;
    }



    // Business methods
    public void complete() {
        if (this.completed) {
            throw new IllegalStateException("Task is already completed");
        }
        this.completed = true;
    }

    public void reopen() {
        if (!this.completed) {
            throw new IllegalStateException("Task is not completed");
        }
        this.completed = false;
    }

    public void updateStatus(TaskStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        if (newStatus == TaskStatus.DONE) {
            this.completed = true;
        } else if (this.status == TaskStatus.DONE && newStatus != TaskStatus.DONE) {
            this.completed = false;
        }
        this.status = newStatus;
    }

    public void updateTitle(String newTitle) {
        if (newTitle == null || newTitle.isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        this.title = newTitle;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
    public TaskStatus getStatus() { return status; }

    public boolean isCompleted() {
        return completed;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskEntity)) return false;
        TaskEntity that = (TaskEntity) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "TaskEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", completed=" + completed +
                '}';
    }
}