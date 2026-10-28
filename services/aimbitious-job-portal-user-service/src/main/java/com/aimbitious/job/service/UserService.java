package com.aimbitious.job.service;

import com.aimbitious.job.domain.UserRole;
import com.aimbitious.job.dto.response.UserResponse;
import com.aimbitious.job.exception.UserException;
import com.aimbitious.job.model.User;
import com.aimbitious.job.payload.UpdateUserRequest;

import java.util.List;

public interface UserService {

    User getUserByEmail(String email) throws UserException;

    User getUserById(Long id) throws UserException;

    List<User> getUsersByRole(UserRole role) throws UserException;

    List<User> getAllUsers() throws UserException;

    UserResponse updateProfile(String email, UpdateUserRequest req) throws UserException;

    // ── Admin actions ──────────────────────────────────────────────────────────
    UserResponse suspendUser(Long id) throws UserException;

    UserResponse activateUser(Long id) throws UserException;

    UserResponse deleteUser(Long id) throws UserException;

    UserResponse changeUserRole(Long id, UserRole role) throws UserException;

}
