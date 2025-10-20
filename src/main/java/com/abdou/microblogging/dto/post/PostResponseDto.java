package com.abdou.microblogging.dto.post;

import com.abdou.microblogging.entities.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PostResponseDto(
        UUID id,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PostResponseDto toPostResponseDto(Post post) {
        return new PostResponseDto(post.getId(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedAt());
    }

    public static List<PostResponseDto> toPostResponseDtoList(List<Post> posts) {
        return posts.stream().map(PostResponseDto::toPostResponseDto).toList();
    }
}
