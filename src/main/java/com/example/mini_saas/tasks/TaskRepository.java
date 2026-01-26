package com.example.mini_saas.tasks;

import com.example.mini_saas.users.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

/**
 * Repository for tasks.
 */
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    /**
     * Find all tasks for a specific owner with pagination.
     */
    Page<TaskEntity> findByOwner(UserEntity owner, Pageable pageable);

    Page<TaskEntity> findByOwnerAndStatus(UserEntity owner, TaskStatus status, Pageable pageable);

    /**
     * Find tasks by owner and completion status.
     */
    Page<TaskEntity> findByOwnerAndCompleted(UserEntity owner, boolean completed, Pageable pageable);

    /**
     * Count all tasks for a specific owner.
     */
    long countByOwner(UserEntity owner);

    /**
     * Count tasks by owner and completion status.
     */
    long countByOwnerAndCompleted(UserEntity owner, boolean completed);

    long countByOwnerAndStatus(UserEntity owner, TaskStatus status);
    /**
     * Search tasks by title keyword for a specific owner.
     */
    @Query("SELECT t FROM TaskEntity t WHERE t.owner = :owner AND LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<TaskEntity> searchByTitle(@Param("owner") UserEntity owner, @Param("keyword") String keyword, Pageable pageable);

    /**
     * Delete all tasks for a specific owner.
     */
    void deleteByOwner(UserEntity owner);
}