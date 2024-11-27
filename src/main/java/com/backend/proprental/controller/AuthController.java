package com.backend.proprental.controller;

import com.backend.proprental.payload.request.LoginRequest;
import com.backend.proprental.payload.request.SignupRequest;
import com.backend.proprental.payload.response.JwtResponse;
import com.backend.proprental.payload.MessageResponse;
import com.backend.proprental.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> signup(@Valid @RequestBody SignupRequest request) {
        userService.register(request);
        return ResponseEntity.ok(MessageResponse.builder().message("You have been registered successfully.").build());
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(userService.login(request, response));
    }
}
