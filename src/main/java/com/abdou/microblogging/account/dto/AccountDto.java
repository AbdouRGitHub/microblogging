package com.abdou.microblogging.account.dto;

import com.abdou.microblogging.account.Account;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.UUID;

public record AccountDto(
        UUID id,
        String username,
        List<String> roles
) {
    public static AccountDto toAccountDto(Account account) {
        List<String> roles = account.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return new AccountDto(
                account.getId(),
                account.getUsername(),
                roles
        );
    }
}