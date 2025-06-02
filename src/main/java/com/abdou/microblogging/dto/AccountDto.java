package com.abdou.microblogging.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AccountDto {
    @NotBlank(message = "Username is empty")
    @Size(min= 5, max= 20, message = "Username should contain between 5 & 20 characters")
    private String username;

    @Email
    private String email;

    @NotBlank(message = "Password is empty")
    @Size(min= 6, max= 20, message = "Password should contain at least 6 characters")
    private String password;

    // Getters / Setters
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
