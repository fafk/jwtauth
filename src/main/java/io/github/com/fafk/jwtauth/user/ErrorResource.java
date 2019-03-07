package io.github.com.fafk.jwtauth.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ErrorResource {
    private Integer status;

    private String message;

    private String path;

    private LocalDateTime timestamp;

    private String error;
}
