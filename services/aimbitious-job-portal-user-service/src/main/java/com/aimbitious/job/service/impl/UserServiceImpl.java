package com.aimbitious.job.service.impl;

import com.aimbitious.job.domain.UserRole;
import com.aimbitious.job.domain.UserStatus;
import com.aimbitious.job.dto.response.UserResponse;
import com.aimbitious.job.exception.UserException;
import com.aimbitious.job.mapper.UserMapper;
import com.aimbitious.job.model.User;
import com.aimbitious.job.payload.UpdateUserRequest;
import com.aimbitious.job.repository.UserRepository;
import com.aimbitious.job.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getUserByEmail(String email) throws UserException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserException("User not found with email: " + email);
        }
        return user;
    }

    @Override
    public User getUserById(Long id) throws UserException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserException("User not found with id: " + id));
    }

    @Override
    public List<User> getUsersByRole(UserRole role) throws UserException {
        return userRepository.findByRole(role);
    }

    @Override
    public List<User> getAllUsers() throws UserException {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public UserResponse updateProfile(String email, UpdateUserRequest req) throws UserException {
        User user = getUserByEmail(email);
        user.setFullName(req.getFullName());
        if (req.getPhone() != null) {
            user.setPhone(req.getPhone());
        }
        if (req.getProfileImage() != null) {
            user.setProfileImage(req.getProfileImage());
        }
        return UserMapper.toDTO(userRepository.save(user));
    }

    // ── Admin actions ──────────────────────────────────────────────────────────

    @Override
    @Transactional
    public UserResponse suspendUser(Long id) throws UserException {
        User user = getUserById(id);
        user.setStatus(UserStatus.SUSPENDED);
        user.setSuspendedAt(LocalDateTime.now());
        return UserMapper.toDTO(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponse activateUser(Long id) throws UserException {
        User user = getUserById(id);
        user.setStatus(UserStatus.ACTIVE);
        user.setSuspendedAt(null);
        return UserMapper.toDTO(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponse deleteUser(Long id) throws UserException {
        User user = getUserById(id);
        user.setStatus(UserStatus.DELETED);
        user.setDeletedAt(LocalDateTime.now());
        return UserMapper.toDTO(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponse changeUserRole(Long id, UserRole role) throws UserException {
        User user = getUserById(id);
        user.setRole(role);
        return UserMapper.toDTO(userRepository.save(user));
    }
}
