package com.abdou.microblogging.account.dto;

import com.abdou.microblogging.account.Account;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

public record UpdateAccountDto(

        Optional<@Size(min = 5,
                       max = 20,
                       message = "Username should contain between 5 & 20 characters") String> username,

        Optional<@Email(message = "Email is not valid") String> email,

        Optional<@Size(min = 6,
                       max = 20,
                       message = "Password should contain at least 6 characters") String> password) {
    public Account toAccount(Account account) {

        username().ifPresent(account::setUsername);
        email().ifPresent(account::setEmail);
        password().ifPresent(u -> {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
            account.setPassword(encoder.encode(u));
        });

        return account;
    }
}