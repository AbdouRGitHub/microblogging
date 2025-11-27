package com.abdou.microblogging.post.dto;

import com.abdou.microblogging.account.dto.AccountDto;
import com.abdou.microblogging.post.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PostDto(
        UUID id,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        AccountDto account
) {
    public static PostDto toPostResponseDto(Post post) {
        return new PostDto(post.getId(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                AccountDto.toAccountDto(post.getAccount()));
    }

    public static List<PostDto> toPostResponseDtoList(List<Post> posts) {
        return posts.stream().map(PostDto::toPostResponseDto).toList();
    }
}
