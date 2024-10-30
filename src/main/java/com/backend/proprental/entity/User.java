package com.backend.proprental.entity;

import com.backend.proprental.entity.utilities.AbstractEntity;
import com.backend.proprental.enums.Role;
import com.backend.proprental.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "users")
public class User extends AbstractEntity {
    @Column(name = "first_name", nullable = false, length = 56)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 56)
    private String lastName;
    @Column(name = "email", nullable = false, unique = true, length = 156)
    private String email;
    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "address", nullable = false)
    private String address;
    @Column(name = "birth_date", nullable = false)
    private String birthDate;
    @Column(name = "image")
    private String image;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status;
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;
}
