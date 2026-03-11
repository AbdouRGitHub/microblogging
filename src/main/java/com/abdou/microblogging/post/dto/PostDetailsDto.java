package com.abdou.microblogging.post.dto;

import com.abdou.microblogging.account.dto.AccountSummaryDto;
import com.abdou.microblogging.bookmark.dto.BookmarkDetailsDto;
import com.abdou.microblogging.like.dto.LikeDetailsDto;
import com.abdou.microblogging.post.Post;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.UUID;

public record PostDetailsDto(
        UUID id,
        String content,
        LikeDetailsDto like,
        BookmarkDetailsDto bookmark,
        int commentsCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        AccountSummaryDto account
) {
    public static PostDetailsDto toDto(Post post, int commentsCount,
                                       LikeDetailsDto like, BookmarkDetailsDto bookmark
    ) {
        return new PostDetailsDto(post.getId(),
                post.getContent(),
                like,
                bookmark,
                commentsCount,
                post.getCreatedAt(),
                post.getUpdatedAt(),
                AccountSummaryDto.toDto(post.getAccount()));
    }
}
