package com.backend.proprental.payload.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserInfo {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String image;
    private String phoneNumber;
    private String address;
    private String birthDate;
    private String role;
    private String status;
    private boolean emailVerified;
    private boolean phoneNumberVerified;
}
