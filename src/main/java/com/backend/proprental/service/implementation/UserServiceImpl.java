package com.backend.proprental.service.implementation;

import com.backend.proprental.entity.User;
import com.backend.proprental.enums.UserStatus;
import com.backend.proprental.exception.BadRequestException;
import com.backend.proprental.exception.DuplicatedValuesException;
import com.backend.proprental.payload.request.LoginRequest;
import com.backend.proprental.payload.request.SignupRequest;
import com.backend.proprental.payload.response.JwtResponse;
import com.backend.proprental.payload.response.UserInfo;
import com.backend.proprental.repository.UserRepository;
import com.backend.proprental.security.jwt.JwtUtils;
import com.backend.proprental.security.services.UserDetailsImpl;
import com.backend.proprental.service.UserService;
import com.backend.proprental.utils.AuthenticationUtils;
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
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.backend.proprental.utils.immutable.AppConstants.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final AuthenticationUtils authenticationUtils;

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
                .emailVerified(false)
                .phoneNumberVerified(false)
                .build();
        userRepository.save(user);
        // TODO : send email (welcome, code verification)
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


        Cookie accessTokenCookie = new Cookie(ACCESS_TOKEN, accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setMaxAge(accessTokenExpiration / 1000);
        accessTokenCookie.setSecure(true);

        Cookie refresTokenCookie = new Cookie(REFRESH_TOKEN, refreshToken);
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

        Cookie accessTokenCookie = new Cookie(ACCESS_TOKEN, null);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setMaxAge(0);
        accessTokenCookie.setSecure(true);

        Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN, null);
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
        if (headerAuth!= null && headerAuth.startsWith(BEARER)) {
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

                    Cookie accessTokenCookie = new Cookie(ACCESS_TOKEN, accessToken);
                    accessTokenCookie.setHttpOnly(true);
                    accessTokenCookie.setMaxAge(accessTokenExpiration / 1000);
                    accessTokenCookie.setSecure(true);
                    response.addCookie(accessTokenCookie);

                    Cookie refresTokenCookie = new Cookie(REFRESH_TOKEN, newRefreshToken);
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

    @Override
    public UserInfo userInfo() {
        var currentUser = authenticationUtils.getCurrentUser();
        return UserInfo.builder()
                .id(currentUser.getId())
                .firstName(currentUser.getFirstName())
                .lastName(currentUser.getLastName())
                .email(currentUser.getEmail())
                .image(currentUser.getImage())
                .phoneNumber(currentUser.getPhoneNumber())
                .birthDate(currentUser.getBirthDate().toString())
                .address(currentUser.getAddress())
                .role(currentUser.getRole().name())
                .emailVerified(currentUser.isEmailVerified())
                .phoneNumberVerified(currentUser.isPhoneNumberVerified())
                .status(currentUser.getStatus().name())
                .build();
    }

    @Override
    public void checkEmail(String email) {
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new BadRequestException("Email already exists");
        }
    }

    @Override
    public String forgotPassword(String email) {
        Optional<User> user = userRepository.findByEmailIgnoreCase(email);
        if (user.isEmpty()) {
            throw new BadRequestException("No account exist with this mail.");
        }
        User existUser = user.get();
        int verificationCode = new Random().nextInt(900000) + 100000;
        existUser.setResetPasswordCode(verificationCode);
        existUser.setCreationDateResetPassword(OffsetDateTime.now());
        userRepository.save(existUser);
        emailSenderService.sendPasswordResetEmail(existUser.getEmail(), String.valueOf(verificationCode), existUser.getFullName());
        return "A password reset email has been sent.";
    }
}
