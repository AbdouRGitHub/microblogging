package com.abdou.microblogging.account.dto;

import com.abdou.microblogging.account.Account;

import java.time.LocalDateTime;
import java.util.UUID;

public record AccountDetailsDto(
        UUID id,
        String username,
        String email,
        LocalDateTime createdAt
) {
    public static AccountDetailsDto toDto(Account account) {
        return new AccountDetailsDto(account.getId(),
                account.getUsername(),
                account.getEmail(),
                account.getCreatedAt());
    }
}
