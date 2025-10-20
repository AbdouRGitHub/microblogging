package com.abdou.microblogging.comment;

import com.abdou.microblogging.account.Account;
import com.abdou.microblogging.comment.dto.CommentDto;
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
    public ResponseEntity<Comment> replyToComment(@PathVariable(name = "commentId") UUID id, @RequestBody CommentDto commentDto, @AuthenticationPrincipal Account account) {
        return commentService.replyToComment(id, commentDto, account);
    }

    @PostMapping("/{commentId}/delete")
    public ResponseEntity<?> deleteComment(@PathVariable(name = "commentId") UUID id, @AuthenticationPrincipal Account account) {
        return commentService.deleteComment(id, account);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(@RequestBody CommentDto commentDto, @PathVariable(name = "commentId") UUID id, @AuthenticationPrincipal Account account) {
        return commentService.editComment();
    }

    @GetMapping("/{parentId}/replies")
    public ResponseEntity<PagedModel<Comment>> getPaginatedCommentReplies(@PathVariable(name = "parentId") UUID parentId, @RequestParam(defaultValue = "1") int page) {
        return commentService.getPaginatedCommentReplies(parentId, page);
    }
}
