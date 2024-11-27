package com.backend.proprental.service;

import com.backend.proprental.payload.request.LoginRequest;
import com.backend.proprental.payload.request.SignupRequest;
import com.backend.proprental.payload.response.JwtResponse;
import com.backend.proprental.payload.response.MessageResponse;

public interface UserService {
    MessageResponse register(SignupRequest request);

    JwtResponse login(LoginRequest request);
}
