package com.abdou.microblogging.account.dto;

import com.abdou.microblogging.account.Account;

import java.time.LocalDateTime;
import java.util.UUID;

public record AccountSummaryDto(
        UUID id,
        String username,
        LocalDateTime createdAt
) {
    public static AccountSummaryDto toDto(Account account) {
        return new AccountSummaryDto(account.getId(),
                account.getUsername(),
                account.getCreatedAt());
    }
}
