package com.example.mini_saas.users;

import com.example.mini_saas.common.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

/**
 * Service containing business logic related to users.
 */
@Service
@Transactional(readOnly = true)
public class UserService {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserEntity createUser(String email, Role role) {
        validateEmail(email);
        checkEmailNotExists(email);

        if (role == null) {
            role = Role.USER;
        }

        UserEntity user = new UserEntity(email, role);
        return userRepository.save(user);
    }

    @Transactional
    public UserEntity createUser(String email) {
        return createUser(email, Role.USER);
    }

    public Page<UserEntity> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public UserEntity findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }

    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
    }

    @Transactional
    public UserEntity updateUserRole(Long id, Role newRole) {
        if (newRole == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }

        UserEntity user = findById(id);
        user.updateRole(newRole);
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        UserEntity user = findById(id);
        userRepository.delete(user);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        if (email.length() > 255) {
            throw new IllegalArgumentException("Email too long (max 255 characters)");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }

    private void checkEmailNotExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("User with email " + email + " already exists");
        }
    }
}