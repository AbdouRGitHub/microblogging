package com.abdou.microblogging.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentDto(
        @NotBlank @Size(min = 1, max = 500) String content
) {
}
