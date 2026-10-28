package com.aimbitious.job.controller;


import com.aimbitious.job.domain.UserRole;
import com.aimbitious.job.dto.response.UserResponse;
import com.aimbitious.job.exception.UserException;
import com.aimbitious.job.mapper.UserMapper;
import com.aimbitious.job.model.User;
import com.aimbitious.job.payload.UpdateUserRequest;
import com.aimbitious.job.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ── Profile ────────────────────────────────────────────────────────────────

    @GetMapping("/api/users/profile")
    public ResponseEntity<UserResponse> getProfile(
            @RequestHeader("X-User-Email") String email) throws UserException {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(UserMapper.toDTO(user));
    }

    @PutMapping("/api/users/profile")
    public ResponseEntity<UserResponse> updateProfile(
            @RequestHeader("X-User-Email") String email,
            @RequestBody @Valid UpdateUserRequest req) throws UserException {
        return ResponseEntity.ok(userService.updateProfile(email, req));
    }

    // ── User lookups (admin / inter-service) ───────────────────────────────────

    @GetMapping("/api/users/{userId}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long userId) throws UserException {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(UserMapper.toDTO(user));
    }

    @GetMapping("/api/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() throws UserException {
        return ResponseEntity.ok(UserMapper.toDTOList(userService.getAllUsers()));
    }

    // ── Admin actions ──────────────────────────────────────────────────────────

    @PatchMapping("/api/users/{userId}/suspend")
    public ResponseEntity<UserResponse> suspendUser(
            @PathVariable Long userId) throws UserException {
        return ResponseEntity.ok(userService.suspendUser(userId));
    }

    @PatchMapping("/api/users/{userId}/activate")
    public ResponseEntity<UserResponse> activateUser(
            @PathVariable Long userId) throws UserException {
        return ResponseEntity.ok(userService.activateUser(userId));
    }

    @DeleteMapping("/api/users/{userId}")
    public ResponseEntity<UserResponse> deleteUser(
            @PathVariable Long userId) throws UserException {
        return ResponseEntity.ok(userService.deleteUser(userId));
    }

    @PatchMapping("/api/users/{userId}/role")
    public ResponseEntity<UserResponse> changeUserRole(
            @PathVariable Long userId,
            @RequestParam UserRole role) throws UserException {
        return ResponseEntity.ok(userService.changeUserRole(userId, role));
    }
}
