package com.example.mini_saas.tasks;

import com.example.mini_saas.common.dto.UpdateTaskStatusRequest;
import com.example.mini_saas.tasks.event.CreateTaskRequest;
import com.example.mini_saas.common.dto.TaskResponseDto;
import com.example.mini_saas.users.UserEntity;
import com.example.mini_saas.users.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for tasks.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(
            @Valid @RequestBody CreateTaskRequest request,
            Authentication authentication) {

        UserEntity currentUser = getCurrentUser(authentication);
        TaskEntity task = taskService.createTask(request.title(), currentUser);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(TaskResponseDto.from(task));
    }

    @GetMapping
    public ResponseEntity<Page<TaskResponseDto>> getTasks(
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable,
            Authentication authentication) {

        UserEntity currentUser = getCurrentUser(authentication);
        Page<TaskEntity> tasks = taskService.findByOwner(currentUser, pageable);

        return ResponseEntity.ok(tasks.map(TaskResponseDto::from));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTask(
            @PathVariable Long id,
            Authentication authentication) {

        UserEntity currentUser = getCurrentUser(authentication);
        TaskEntity task = taskService.findById(id, currentUser);

        return ResponseEntity.ok(TaskResponseDto.from(task));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody CreateTaskRequest request,
            Authentication authentication) {

        UserEntity currentUser = getCurrentUser(authentication);
        TaskEntity task = taskService.updateTask(id, request.title(), currentUser);

        return ResponseEntity.ok(TaskResponseDto.from(task));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponseDto> updateTaskStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskStatusRequest request,
            Authentication authentication) {

        UserEntity currentUser = getCurrentUser(authentication);
        TaskEntity task = taskService.updateTaskStatus(id, request.status(), currentUser);

        return ResponseEntity.ok(TaskResponseDto.from(task));
    }


    @PostMapping("/{id}/start")
    public ResponseEntity<TaskResponseDto> startTask(
            @PathVariable Long id,
            Authentication authentication) {

        UserEntity currentUser = getCurrentUser(authentication);
        TaskEntity task = taskService.startTask(id, currentUser);

        return ResponseEntity.ok(TaskResponseDto.from(task));
    }


    @PostMapping("/{id}/complete")
    public ResponseEntity<TaskResponseDto> completeTask(
            @PathVariable Long id,
            Authentication authentication) {

        UserEntity currentUser = getCurrentUser(authentication);
        TaskEntity task = taskService.completeTask(id, currentUser);

        return ResponseEntity.ok(TaskResponseDto.from(task));
    }

    @PostMapping("/{id}/reopen")
    public ResponseEntity<TaskResponseDto> reopenTask(
            @PathVariable Long id,
            Authentication authentication) {

        UserEntity currentUser = getCurrentUser(authentication);
        TaskEntity task = taskService.reopenTask(id, currentUser);

        return ResponseEntity.ok(TaskResponseDto.from(task));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id,
            Authentication authentication) {

        UserEntity currentUser = getCurrentUser(authentication);
        taskService.deleteTask(id, currentUser);

        return ResponseEntity.noContent().build();
    }

    private UserEntity getCurrentUser(Authentication authentication) {
        String email = extractEmail(authentication);
        return userService.findByEmail(email);
    }

    private String extractEmail(Authentication authentication) {
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getClaimAsString("email");
        }
        return authentication.getName();
    }
}