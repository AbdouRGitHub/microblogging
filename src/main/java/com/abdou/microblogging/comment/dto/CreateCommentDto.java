package com.abdou.microblogging.comment.dto;

import com.abdou.microblogging.account.Account;
import com.abdou.microblogging.comment.Comment;
import com.abdou.microblogging.post.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCommentDto(
        @NotBlank @Size(min = 1, max = 500) String content
) {
    public static Comment createComment(CreateCommentDto createCommentDto, Post post, Account account) {
        return new Comment(createCommentDto.content(), post, account);
    }
}
