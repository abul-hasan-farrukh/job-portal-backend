package com.aimbitious.job.service.impl;

import com.aimbitious.job.domain.UserRole;
import com.aimbitious.job.exception.UserException;
import com.aimbitious.job.mapper.UserMapper;
import com.aimbitious.job.model.User;
import com.aimbitious.job.payload.AuthResponse;
import com.aimbitious.job.payload.LoginRequest;
import com.aimbitious.job.payload.SignupRequest;
import com.aimbitious.job.repository.UserRepository;
import com.aimbitious.job.security.CustomUserDetailsService;
import com.aimbitious.job.security.JwtProvider;
import com.aimbitious.job.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor

public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public AuthResponse signup(SignupRequest req) throws UserException {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new UserException("Email already registered: " + req.getEmail());
        }

        if (req.getRole() == UserRole.ROLE_ADMIN) {
            throw new UserException("Cannot self-register as ROLE_ADMIN");
        }

        User user = new User();
        user.setFullName(req.getFullName());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setPhone(req.getPhone());
        user.setRole(req.getRole());
        user.setLastLogin(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                savedUser.getEmail(), savedUser.getPassword()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(
                authentication, savedUser.getId());

        AuthResponse response = new AuthResponse();
        response.setTitle("Welcome " + savedUser.getFullName());
        response.setMessage("Registration successful");
        response.setJwt(jwt);
        response.setUser(UserMapper.toDTO(savedUser));
        return response;
    }

    @Override
    public AuthResponse login(LoginRequest req) throws UserException {
        Authentication authentication = authenticate(req.getEmail(), req.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(req.getEmail());
        String token = jwtProvider.generateToken(authentication, user.getId());

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        AuthResponse response = new AuthResponse();
        response.setTitle("Login successful");
        response.setMessage("Welcome back, " + user.getFullName());
        response.setJwt(token);
        response.setUser(UserMapper.toDTO(user));
        return response;
    }

    private Authentication authenticate(String email, String password) throws UserException {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        if (userDetails == null) {
            throw new UserException("User not found with email: " + email);
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new UserException("Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(email,
                null, userDetails.getAuthorities());
    }
}