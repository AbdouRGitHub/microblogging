package com.abdou.microblogging.account.dto;

import java.util.UUID;

public record AccountResponseDto(
        UUID id,
        String username
) {
}