package com.backend.proprental.entity;

import com.backend.proprental.entity.utilities.AbstractEntity;
import com.backend.proprental.enums.Role;
import com.backend.proprental.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class User extends AbstractEntity {
    @Column(name = "first_name", nullable = false, length = 56)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 56)
    private String lastName;
    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String email;
    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "address", nullable = false)
    private String address;
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;
    @Column(name = "image")
    private String image;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status;
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;
    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified;
    @Column(name = "phone_number_verified", nullable = false)
    private boolean phoneNumberVerified;
    @Column(name = "reset_password_code")
    private Integer resetPasswordCode;
    @Column(name = "creation_date_reset_password_code")
    private OffsetDateTime creationDateResetPassword;

    @Transient
    public String getFullName() {
        return "%s %s".formatted(firstName, lastName);
    }
}
