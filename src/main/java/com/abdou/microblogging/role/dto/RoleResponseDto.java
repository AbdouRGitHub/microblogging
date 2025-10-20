package com.abdou.microblogging.role.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record RoleResponseDto(
        UUID id,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
