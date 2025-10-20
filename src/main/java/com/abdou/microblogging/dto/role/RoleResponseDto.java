package com.abdou.microblogging.dto.role;

import java.time.LocalDateTime;
import java.util.UUID;

public record RoleResponseDto(
        UUID id,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
