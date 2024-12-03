package com.backend.proprental.service.implementation;

import com.backend.proprental.entity.User;
import com.backend.proprental.enums.UserStatus;
import com.backend.proprental.exception.DuplicatedValuesException;
import com.backend.proprental.payload.request.LoginRequest;
import com.backend.proprental.payload.request.SignupRequest;
import com.backend.proprental.payload.response.JwtResponse;
import com.backend.proprental.repository.UserRepository;
import com.backend.proprental.security.jwt.JwtUtils;
import com.backend.proprental.security.services.UserDetailsImpl;
import com.backend.proprental.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @Value("${token.app.jwtAccessToken.expirationMs}")
    private int accessTokenExpiration;

    @Value("${token.app.jwtRefreshToken.expirationMs}")
    private int refreshTokenExpiration;

    @Override
    public void register(SignupRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new DuplicatedValuesException("Email already exists");
        }
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(request.getPassword())
                .phoneNumber(request.getPhoneNumber())
                .image(request.getImage())
                .address(request.getAddress())
                .status(UserStatus.PENDING)
                .birthDate(request.getBirthDate())
                .role(request.getRole())
                .build();
        userRepository.save(user);
    }

    @Override
    public JwtResponse login(LoginRequest request, HttpServletResponse response) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        String accessToken = jwtUtils.generateAccessToken(userDetails.getUsername(), roles);
        String refreshToken = jwtUtils.generateRefreshToken(userDetails.getUsername(), roles);


        Cookie accessTokenCookie = new Cookie("access_token", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setMaxAge(accessTokenExpiration / 1000);
        accessTokenCookie.setSecure(true);

        Cookie refresTokenCookie = new Cookie("refresh_token", refreshToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setMaxAge(refreshTokenExpiration / 1000);
        accessTokenCookie.setSecure(true);

        response.addCookie(accessTokenCookie);
        response.addCookie(refresTokenCookie);

        return JwtResponse.builder()
                        .id(userDetails.getId())
                        .email(userDetails.getEmail())
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .roles(roles)
                        .build();
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext();

        Cookie accessTokenCookie = new Cookie("access_token", null);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setMaxAge(0);
        accessTokenCookie.setSecure(true);

        Cookie refreshTokenCookie = new Cookie("refresh_token", null);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setMaxAge(0);
        accessTokenCookie.setSecure(true);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (headerAuth!= null && headerAuth.startsWith("Bearer ")) {
            refreshToken = headerAuth.substring(7);
            userEmail = jwtUtils.extractUsername(refreshToken);
            if (userEmail != null) {
                UserDetailsImpl user = UserDetailsImpl.build(userRepository.findByEmailIgnoreCase(userEmail)
                        .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail)));
                if (jwtUtils.validateJwtToken(refreshToken)) {
                    var accessToken = jwtUtils.generateAccessToken(user.getUsername(), user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
                    var newRefreshToken = jwtUtils.generateRefreshToken(user.getUsername(), user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
                    var authResponse = JwtResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(newRefreshToken)
                            .build();

                    Cookie accessTokenCookie = new Cookie("access_token", accessToken);
                    accessTokenCookie.setHttpOnly(true);
                    accessTokenCookie.setMaxAge(accessTokenExpiration / 1000);
                    accessTokenCookie.setSecure(true);
                    response.addCookie(accessTokenCookie);

                    Cookie refresTokenCookie = new Cookie("refresh_token", newRefreshToken);
                    accessTokenCookie.setHttpOnly(true);
                    accessTokenCookie.setMaxAge(refreshTokenExpiration / 1000);
                    accessTokenCookie.setSecure(true);
                    response.addCookie(accessTokenCookie);
                    response.addCookie(refresTokenCookie);

                    new ObjectMapper().writeValue(response.getOutputStream(), authResponse);

                }
        }
        }
    }
}
