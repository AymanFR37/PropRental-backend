package com.backend.proprental.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @Builder.Default
    private String type = "Bearer";
}
