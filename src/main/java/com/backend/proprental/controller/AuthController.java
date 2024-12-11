package com.backend.proprental.controller;

import com.backend.proprental.payload.GenericResponse;
import com.backend.proprental.payload.request.LoginRequest;
import com.backend.proprental.payload.request.SignupRequest;
import com.backend.proprental.payload.response.JwtResponse;
import com.backend.proprental.payload.MessageResponse;
import com.backend.proprental.payload.response.UserInfo;
import com.backend.proprental.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("register")
    public ResponseEntity<MessageResponse> signup(@Valid @RequestBody SignupRequest request) {
        userService.register(request);
        return ResponseEntity.ok(MessageResponse.builder().message("You have been registered successfully.").build());
    }

    @PostMapping("login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(userService.login(request, response));
    }

    @PostMapping("logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        userService.logout(request, response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        userService.refreshToken(request, response);
    }

    @GetMapping("user-info")
    public ResponseEntity<GenericResponse<UserInfo>> userInfo() {
        return ResponseEntity.ok(GenericResponse.success(userService.userInfo()));
    }

    @GetMapping("check")
    public ResponseEntity<Void> checkEmail(@RequestParam String email) {
        userService.checkEmail(email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/password")
    public ResponseEntity<MessageResponse> forgotPassword(@RequestParam("email") String email) {
        return ResponseEntity.ok(MessageResponse.builder().message(userService.forgotPassword(email)).build());
    }


    // TODO : send verification code endpoint
    // TODO : verify code endpoint
    // TODO : forgot password endpoint
}
