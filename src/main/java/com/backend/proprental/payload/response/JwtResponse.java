package com.backend.proprental.payload.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class JwtResponse {
    private UUID id;
    private String email;
    private List<String> roles;
    private String accessToken;
    @Builder.Default
    private String type = "Bearer";
}
