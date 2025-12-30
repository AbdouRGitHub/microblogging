package com.abdou.microblogging.post.dto;

import com.abdou.microblogging.account.dto.AccountDetailsDto;
import com.abdou.microblogging.post.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PostDetailsDto(
        UUID id,
        String content,
        int likes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        AccountDetailsDto account
) {
    public static PostDetailsDto toPostResponseDto(Post post, int likes) {
        return new PostDetailsDto(post.getId(),
                post.getContent(),
                likes,
                post.getCreatedAt(),
                post.getUpdatedAt(),
                AccountDetailsDto.toDto(post.getAccount()));
    }

    public static List<PostDetailsDto> toPostResponseDtoList(List<Post> posts,
                                                             int likes
    ) {
        return posts.stream()
                .map((post -> toPostResponseDto(post, likes)))
                .toList();
    }
}
