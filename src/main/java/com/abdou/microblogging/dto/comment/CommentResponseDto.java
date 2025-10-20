package com.abdou.microblogging.dto.comment;

import com.abdou.microblogging.entities.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CommentResponseDto(
        UUID id,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CommentResponseDto toCommentResponseDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }

    public static List<CommentResponseDto> toCommentResponseDtoList(List<Comment> comments) {
        return comments.stream()
                .map(CommentResponseDto::toCommentResponseDto)
                .toList();
    }
}