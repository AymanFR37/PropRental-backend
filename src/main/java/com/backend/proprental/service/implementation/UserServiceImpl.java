package com.backend.proprental.service.implementation;

import com.backend.proprental.payload.request.LoginRequest;
import com.backend.proprental.payload.request.SignupRequest;
import com.backend.proprental.payload.response.JwtResponse;
import com.backend.proprental.payload.response.MessageResponse;
import com.backend.proprental.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public MessageResponse register(SignupRequest request) {
        return null;
    }

    @Override
    public JwtResponse login(LoginRequest request) {
        return null;
    }
}
