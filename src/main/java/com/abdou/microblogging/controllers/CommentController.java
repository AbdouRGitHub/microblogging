package com.abdou.microblogging.controllers;

import com.abdou.microblogging.entities.Comment;
import com.abdou.microblogging.services.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{commentId}/reply")
    public ResponseEntity<Comment> replyToComment() {
        
    }

    @PostMapping("/delete")
    public void deleteComment() {

    }

    @PostMapping("/edit")
    public void editComment() {

    }
}
