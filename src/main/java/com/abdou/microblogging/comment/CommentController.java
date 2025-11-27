package com.abdou.microblogging.comment;

import com.abdou.microblogging.account.Account;
import com.abdou.microblogging.comment.dto.CommentDto;
import com.abdou.microblogging.comment.dto.CreateCommentDto;
import org.springframework.data.web.PagedModel;
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
    public ResponseEntity<Comment> replyToComment(@PathVariable(name = "commentId") UUID id, @RequestBody CreateCommentDto createCommentDto, @AuthenticationPrincipal Account account) {
        return commentService.replyToComment(id, createCommentDto, account);
    }

    @PostMapping("/{commentId}/delete")
    public ResponseEntity<?> deleteComment(@PathVariable(name = "commentId") UUID id, @AuthenticationPrincipal Account account) {
        return commentService.deleteComment(id, account);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(@RequestBody CreateCommentDto createCommentDto, @PathVariable(name = "commentId") UUID id, @AuthenticationPrincipal Account account) {
        return commentService.editComment();
    }

    @GetMapping("/{parentId}/replies")
    public ResponseEntity<PagedModel<CommentDto>> getPaginatedCommentReplies(@PathVariable(name = "parentId") UUID parentId, @RequestParam(defaultValue = "1") int page) {
        return commentService.getPaginatedCommentReplies(parentId, page);
    }
}
