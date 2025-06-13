package com.abdou.microblogging.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostDto(
        @NotBlank(message = "Content is empty")
        @Size(max= 300, message = "Content should not exceed 300 characters")
        String content
) {
}
