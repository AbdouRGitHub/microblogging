package com.abdou.microblogging.account.dto;

import com.abdou.microblogging.account.Account;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record AccountDetailsDto(
        UUID id,
        String username,
        List<String> roles,
        LocalDateTime createdAt
) {
    public static AccountDetailsDto toDto(Account account) {
        List<String> roles = account.getAuthorities().stream().map(
                GrantedAuthority::getAuthority).toList();
        return new AccountDetailsDto(account.getId(),
                account.getUsername(),
                roles,
                account.getCreatedAt());
    }
}
