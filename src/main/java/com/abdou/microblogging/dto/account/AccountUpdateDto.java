package com.abdou.microblogging.dto.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record AccountUpdateDto(
        @Size(min = 5, max = 20, message = "Username should contain between 5 & 20 characters")
        String username,

        @Email(message = "Email is not valid")
        String email,

        @Size(min = 6, max = 20, message = "Password should contain at least 6 characters")
        String password
) {
}