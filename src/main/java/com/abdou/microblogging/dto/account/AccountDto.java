package com.abdou.microblogging.dto.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AccountDto(
        @NotBlank(message = "Username is empty")
        @Size(min = 5, max = 20, message = "Username should contain between 5 & 20 characters")
        String username,

        @NotBlank(message = "Email is Empty")
        @Email(message = "Email is not valid")
        String email,

        @NotBlank(message = "Password is empty")
        @Size(min = 6, max = 20, message = "Password should contain at least 6 characters")
        String password
) {
}
