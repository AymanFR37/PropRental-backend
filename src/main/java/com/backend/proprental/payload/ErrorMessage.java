package com.backend.proprental.payload;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
public class ErrorMessage {
    private int statusCode;
    @Builder.Default
    private Date timestamp = Timestamp.valueOf(LocalDateTime.now());
    @Builder.Default
    private String message = "Oops! Something went wrong...";
    private String description;

    public ErrorMessage(int statusCode, Date timestamp, String message, String description) {
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.message = message;
        this.description = description;
    }
}
