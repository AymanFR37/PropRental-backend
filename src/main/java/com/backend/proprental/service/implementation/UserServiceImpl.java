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
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @Value("${token.app.jwtExpirationMs}")
    private int jwtExpirationMs;

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

        String accessToken = jwtUtils.generateJwtToken(authentication);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        Cookie accessTokenCookie = new Cookie("access_token", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setMaxAge(jwtExpirationMs / 1000);
        accessTokenCookie.setSecure(true);
        response.addCookie(accessTokenCookie);

        return JwtResponse.builder()
                        .id(userDetails.getId())
                        .email(userDetails.getEmail())
                        .accessToken(accessToken)
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
        response.addCookie(accessTokenCookie);
    }
}
