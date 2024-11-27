package com.backend.proprental.payload;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GenericResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> GenericResponse<T> success(T data) {
        return GenericResponse.<T>builder()
                .message("SUCCESS!")
                .data(data)
                .success(true)
                .build();
    }

    public static <T> GenericResponse<T> error() {
        return GenericResponse.<T>builder()
                .message("ERROR!")
                .success(false)
                .build();
    }

    public static <T> GenericResponse<T> message(String msg, T data) {
        return GenericResponse.<T>builder()
                .success(true)
                .message(msg)
                .data(data)
                .build();
    }
}
