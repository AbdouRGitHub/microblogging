package com.abdou.microblogging.account.dto;

import com.abdou.microblogging.account.Account;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

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
        System.out.println("Before - password: " + account.getPassword());

        username().ifPresent(account::setUsername);
        email().ifPresent(account::setEmail);
        password().ifPresent(u -> {
            System.out.println("Setting password to: " + u);
            account.setPassword(u);
        });

        System.out.println("After - password: " + account.getPassword());
        return account;
    }
}