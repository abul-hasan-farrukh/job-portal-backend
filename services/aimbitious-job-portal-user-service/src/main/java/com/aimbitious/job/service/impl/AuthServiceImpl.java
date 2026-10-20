package com.aimbitious.job.service.impl;

import com.aimbitious.job.domain.UserRole;
import com.aimbitious.job.domain.UserStatus;
import com.aimbitious.job.mapper.UserMapper;
import com.aimbitious.job.model.User;
import com.aimbitious.job.payload.AuthResponse;
import com.aimbitious.job.payload.LoginRequest;
import com.aimbitious.job.payload.SignupRequest;
import com.aimbitious.job.repository.UserRepository;
import com.aimbitious.job.security.CustomUserDetailsService;
import com.aimbitious.job.security.JwtProvider;
import com.aimbitious.job.service.AuthService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
    public AuthResponse signup(SignupRequest req) throws Exception {

        if(userRepository.existsByEmail(req.getEmail())){
            throw new Exception("Email Already Registered: "+ req.getEmail());
        }

        if (req.getRole() == UserRole.ROLE_ADMIN){
            throw new Exception("cannot self register as role admin");
        }

        User user = User.builder()
                .fullName(req.getFullName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(req.getRole())
                .phone(req.getPhone())
                .lastLogin(LocalDateTime.now())
                .status(UserStatus.ACTIVE)
                .build();

        User savedUser = userRepository.save(user);

        //creating JWT token below
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(), user.getPassword()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication, savedUser.getId());

        AuthResponse res = new AuthResponse();
        res.setTitle("welcome " +savedUser.getFullName());
        res.setMessage("Registered Successfully");
        res.setJwt(jwt);
        res.setUser(UserMapper.toDTO(savedUser));


        return res;
    }

    @Override
    public AuthResponse login(LoginRequest req) throws Exception {

        //creating JWT token below
        Authentication authentication = authenticate(
                req.getEmail(), req.getPassword()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(req.getEmail());

        String jwt = jwtProvider.generateToken(authentication, user.getId());

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        AuthResponse res = new AuthResponse();
        res.setTitle("welcome back -- "+user.getFullName());
        res.setMessage("Login Successfully");
        res.setJwt(jwt);
        res.setUser(UserMapper.toDTO(user));

        return res;
    }

    private Authentication authenticate(@Email(message = "Email should be valid") @NotBlank(message = "Email is mandatory") String email, @NotBlank(message = "Password is mandatory") String password) throws Exception {

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        if(userDetails == null) {
            throw new Exception("user not found with email"+email);
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new Exception("Invalid Password");
        }

        return new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
    }
}