package com.backend.proprental.configuration;

import com.backend.proprental.entity.User;
import com.backend.proprental.enums.Role;
import com.backend.proprental.enums.UserStatus;
import com.backend.proprental.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class DataLoader {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public ApplicationRunner init(){
        return args -> loadAdmin();
    }

    public void loadAdmin(){
        Optional<User> users = userRepository.findByRole(Role.ROLE_ADMIN);
        if (users.isEmpty()){
            var user = User.builder()
                    .firstName("Admin")
                    .lastName("Admin")
                    .email("proprental_admin@yopmail.com")
                    .password(passwordEncoder.encode("Admin@123"))
                    .address("casablanca, MA")
                    .phoneNumber("+212666666666")
                    .birthDate(LocalDate.of(1999, 6, 26))
                    .role(Role.ROLE_ADMIN)
                    .status(UserStatus.ACTIVE)
                    .build();
            userRepository.save(user);
        }
    }
}
