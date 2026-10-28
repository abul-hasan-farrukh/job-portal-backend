package com.aimbitious.job.service;

import com.aimbitious.job.exception.UserException;
import com.aimbitious.job.payload.AuthResponse;
import com.aimbitious.job.payload.LoginRequest;
import com.aimbitious.job.payload.SignupRequest;

public interface AuthService {

    AuthResponse signup(SignupRequest req) throws UserException;
    AuthResponse login(LoginRequest req) throws UserException;
}
