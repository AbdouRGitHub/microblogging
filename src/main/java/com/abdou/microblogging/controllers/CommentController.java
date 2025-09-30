package com.abdou.microblogging.controllers;

import com.abdou.microblogging.dto.comment.CommentDto;
import com.abdou.microblogging.entities.Account;
import com.abdou.microblogging.entities.Comment;
import com.abdou.microblogging.services.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{commentId}/reply")
    public ResponseEntity<Comment> replyToComment(@PathVariable(name = "commentId") UUID id, @RequestBody CommentDto commentDto, @AuthenticationPrincipal Account account) {
        return commentService.replyToComment(id, commentDto, account);
    }

    @PostMapping("/{commentId}/delete")
    public ResponseEntity<?> deleteComment(@PathVariable(name = "commentId") UUID id, @AuthenticationPrincipal Account account) {
        return commentService.deleteComment(id, account);
    }

    @PostMapping("/edit")
    public void editComment() {

    }
}
