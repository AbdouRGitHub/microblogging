package com.abdou.microblogging.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePostDto(
        @NotBlank(message = "Content is empty")
        @Size(max = 300, message = "Content should not exceed 300 characters")
        String content
) {
}
