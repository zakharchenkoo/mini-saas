package com.example.mini_saas.tasks;

import com.example.mini_saas.common.exception.NotFoundException;
import com.example.mini_saas.tasks.event.TaskCreatedEvent;
import com.example.mini_saas.users.UserEntity;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for task-related business logic.
 */
@Service
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository taskRepository;
    private final ApplicationEventPublisher eventPublisher;

    public TaskService(TaskRepository taskRepository,
                       ApplicationEventPublisher eventPublisher) {
        this.taskRepository = taskRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public TaskEntity createTask(String title, UserEntity owner) {
        TaskEntity task = new TaskEntity(title, owner);
        TaskEntity savedTask = taskRepository.save(task);

        // Publish event with data, not entity
        eventPublisher.publishEvent(new TaskCreatedEvent(
                savedTask.getId(),
                savedTask.getTitle(),
                savedTask.getOwner().getId(),
                savedTask.getCreatedAt()
        ));

        return savedTask;
    }

    public Page<TaskEntity> findByOwner(UserEntity owner, Pageable pageable) {
        return taskRepository.findByOwner(owner, pageable);
    }

    public Page<TaskEntity> findByOwnerAndStatus(UserEntity owner, TaskStatus status, Pageable pageable) {
        return taskRepository.findByOwnerAndStatus(owner, status, pageable);
    }

    public TaskEntity findById(Long id, UserEntity currentUser) {
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found: " + id));

        checkOwnership(task, currentUser);
        return task;
    }

    @Transactional
    public TaskEntity updateTask(Long id, String newTitle, UserEntity currentUser) {
        TaskEntity task = findById(id, currentUser);
        task.updateTitle(newTitle);
        return taskRepository.save(task);
    }

    @Transactional
    public TaskEntity updateTaskStatus(Long id, TaskStatus newStatus, UserEntity currentUser) {
        TaskEntity task = findById(id, currentUser);
        task.updateStatus(newStatus);
        return taskRepository.save(task);
    }

    @Transactional
    public TaskEntity startTask(Long id, UserEntity currentUser) {
        TaskEntity task = findById(id, currentUser);
        task.start();
        return taskRepository.save(task);
    }

    @Transactional
    public TaskEntity completeTask(Long id, UserEntity currentUser) {
        TaskEntity task = findById(id, currentUser);
        task.complete();
        return taskRepository.save(task);
    }

    @Transactional
    public TaskEntity reopenTask(Long id, UserEntity currentUser) {
        TaskEntity task = findById(id, currentUser);
        task.reopen();
        return taskRepository.save(task);
    }

    @Transactional
    public void deleteTask(Long id, UserEntity currentUser) {
        TaskEntity task = findById(id, currentUser);
        taskRepository.delete(task);
    }

    private void checkOwnership(TaskEntity task, UserEntity currentUser) {
        if (!task.getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("User does not own this task");
        }
    }
}