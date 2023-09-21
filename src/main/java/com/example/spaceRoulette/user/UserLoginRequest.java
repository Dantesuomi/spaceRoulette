package com.example.spaceRoulette.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserLoginRequest {
    @NotNull
    @NotEmpty
    private String password;
    @NotNull
    @NotEmpty
    private String username;
}
