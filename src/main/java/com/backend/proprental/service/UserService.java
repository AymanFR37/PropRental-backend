package com.backend.proprental.service;

import com.backend.proprental.payload.request.LoginRequest;
import com.backend.proprental.payload.request.SignupRequest;
import com.backend.proprental.payload.response.JwtResponse;
import com.backend.proprental.payload.response.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface UserService {

    void register(SignupRequest request);

    JwtResponse login(LoginRequest request, HttpServletResponse response);

    void logout(HttpServletRequest request, HttpServletResponse response);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    UserInfo userInfo();

    void checkEmail(String email);

    String forgotPassword(String email);
}
