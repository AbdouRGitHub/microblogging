package com.abdou.microblogging.comment.dto;

import com.abdou.microblogging.account.dto.AccountDto;
import com.abdou.microblogging.comment.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CommentDto(UUID id, String content, LocalDateTime createdAt,
                         LocalDateTime updatedAt,
                         AccountDto account) {
    public static CommentDto toCommentResponseDto(Comment comment) {
        return new CommentDto(comment.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                AccountDto.toAccountDto(comment.getAccount()));
    }

    public static List<CommentDto> toCommentResponseDtoList(List<Comment> comments) {
        return comments.stream().map(CommentDto::toCommentResponseDto).toList();
    }
}