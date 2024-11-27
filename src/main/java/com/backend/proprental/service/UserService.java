package com.backend.proprental.service;

import com.backend.proprental.payload.request.LoginRequest;
import com.backend.proprental.payload.request.SignupRequest;
import com.backend.proprental.payload.response.JwtResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
    void register(SignupRequest request);

    JwtResponse login(LoginRequest request, HttpServletResponse response);
}
