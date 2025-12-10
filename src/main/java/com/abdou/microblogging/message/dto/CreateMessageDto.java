package com.abdou.microblogging.message.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateMessageDto(
        @NotBlank(message = "Content is empty")
        @Size(max = 300, message = "Content should not exceed 300 characters")
        String content
) {
}
