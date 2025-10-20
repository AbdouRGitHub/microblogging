package com.abdou.microblogging.dto.account;

import java.util.UUID;

public record AccountResponseDto(
        UUID id,
        String username
) {
}