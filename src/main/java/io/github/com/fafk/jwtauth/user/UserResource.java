package io.github.com.fafk.jwtauth.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResource {

    @NotBlank
    @Email
    private String username;

    @NotBlank
    private String password;

}
