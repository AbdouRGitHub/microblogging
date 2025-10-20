package com.abdou.microblogging.controllers;

import com.abdou.microblogging.dto.comment.CommentDto;
import com.abdou.microblogging.dto.post.PostDto;
import com.abdou.microblogging.dto.post.PostResponseDto;
import com.abdou.microblogging.entities.Account;
import com.abdou.microblogging.entities.Comment;
import com.abdou.microblogging.entities.Post;
import com.abdou.microblogging.services.CommentService;
import com.abdou.microblogging.services.PostService;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final CommentService commentService;

    PostController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @PostMapping()
    public ResponseEntity<Post> createPost(@AuthenticationPrincipal Account account, @RequestBody PostDto postDto) {
        return postService.createPost(account, postDto);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<?> createComment(@PathVariable UUID id, @RequestBody CommentDto commentDto, @AuthenticationPrincipal Account account) {
        return this.commentService.createComment(id, commentDto, account);
    }

    @GetMapping()
    public ResponseEntity<PagedModel<PostResponseDto>> getPaginatedPosts(@RequestParam(defaultValue = "1") int page) {
        return postService.getPaginatedPosts(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostInfo(@PathVariable UUID id) {
        return postService.getPostInfo(id);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<PagedModel<Comment>> getPaginatedPostComments(@PathVariable UUID postId, @RequestParam(defaultValue = "1") int page) {
        return commentService.getPaginatedPostComments(postId, page);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Post> updatePost(@RequestBody PostDto postDto, @PathVariable UUID id) {
        return postService.updatePost(postDto, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable UUID id) {
        return postService.deletePost(id);
    }
}
