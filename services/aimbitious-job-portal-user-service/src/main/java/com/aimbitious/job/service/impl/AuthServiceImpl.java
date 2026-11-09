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
import com.aimbitious.job.security.GoogleTokenVerifier;
import com.aimbitious.job.security.JwtProvider;
import com.aimbitious.job.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.aimbitious.job.payload.GoogleLoginRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Base64;

import com.aimbitious.job.domain.AuthProvider;
import com.aimbitious.job.domain.UserStatus;

import java.time.LocalDateTime;
@Slf4j
@Service
@RequiredArgsConstructor

public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;

    private final GoogleTokenVerifier googleTokenVerifier;

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

        UserDetails userDetails =
                customUserDetailsService.loadUserByUsername(
                        savedUser.getEmail()
                );

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

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

    //Google Login
    @Override
    @Transactional
    public AuthResponse googleLogin(GoogleLoginRequest req) throws UserException {

        try {

            if (req == null ||
                    req.getIdToken() == null ||
                    req.getIdToken().isBlank()) {

                throw new UserException("Google ID token is required");
            }

            // 1. Verify Google ID token
            GoogleIdToken.Payload payload =
                    googleTokenVerifier.verify(req.getIdToken());

            if (payload == null) {
                throw new UserException("Invalid Google ID token");
            }

            // 2. Extract verified Google information
            String googleId = payload.getSubject();
            String email = payload.getEmail();

            Boolean emailVerified = payload.getEmailVerified();

            if (googleId == null || googleId.isBlank() ||
                    email == null || email.isBlank() ||
                    !Boolean.TRUE.equals(emailVerified)) {

                throw new UserException("Google email is not verified");
            }

            String fullName = (String) payload.get("name");
            String profileImage = (String) payload.get("picture");

            // 3. Find user by Google ID
            User user = userRepository.findByGoogleId(googleId);

            if (user == null) {

                // 4. Check whether this email already belongs to an account
                User existingUser = userRepository.findByEmail(email);

                if (existingUser != null) {

                    // Do not automatically link Google to an existing local account.
                    throw new UserException(
                            "An account already exists with this email. " +
                                    "Please log in using your existing login method " +
                                    "before linking Google."
                    );
                }

                // 5. Validate role for new Google registration
                UserRole role = req.getRole();

                if (role == null) {
                    role = UserRole.ROLE_JOB_SEEKER;
                }

                if (role != UserRole.ROLE_JOB_SEEKER &&
                        role != UserRole.ROLE_EMPLOYER) {

                    throw new UserException(
                            "Invalid role for Google registration"
                    );
                }

                // 6. Generate a random password
                byte[] randomBytes = new byte[32];

                new SecureRandom().nextBytes(randomBytes);

                String randomPassword = Base64.getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(randomBytes);

                // 7. Create Google user
                user = new User();

                user.setFullName(
                        fullName != null && !fullName.isBlank()
                                ? fullName
                                : email
                );

                user.setEmail(email);
                user.setGoogleId(googleId);

                user.setPassword(
                        passwordEncoder.encode(randomPassword)
                );

                user.setProfileImage(profileImage);

                user.setAuthProvider(AuthProvider.GOOGLE);

                user.setVerified(true);

                user.setRole(role);

                user.setStatus(UserStatus.ACTIVE);

            } else {

                // Existing Google account
                if (user.getAuthProvider() != AuthProvider.GOOGLE) {

                    throw new UserException(
                            "This account is not configured for Google login"
                    );
                }

                // Update profile image when Google provides one
                if (profileImage != null && !profileImage.isBlank()) {
                    user.setProfileImage(profileImage);
                }

                // Refresh verified status from Google's verified email
                user.setVerified(true);
            }

            // 8. Check account status
            if (user.getStatus() != UserStatus.ACTIVE) {

                throw new UserException(
                        "Your account is not active. Please contact support."
                );
            }

            // 9. Update last login
            user.setLastLogin(LocalDateTime.now());

            User savedUser = userRepository.save(user);

            // 10. Load user authorities
            UserDetails userDetails =
                    customUserDetailsService.loadUserByUsername(
                            savedUser.getEmail()
                    );

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);

            // 11. Generate application JWT
            String jwt = jwtProvider.generateToken(
                    authentication,
                    savedUser.getId()
            );

            // 12. Return existing authentication response
            AuthResponse response = new AuthResponse();

            response.setTitle("Google login successful");

            response.setMessage(
                    "Welcome, " + savedUser.getFullName()
            );

            response.setJwt(jwt);

            response.setUser(UserMapper.toDTO(savedUser));

            return response;

        } catch (UserException e) {

            throw e;

        } catch (Exception e) {

            log.error("Google authentication failed", e);

            throw new UserException("Google authentication failed");
        }
    }
}