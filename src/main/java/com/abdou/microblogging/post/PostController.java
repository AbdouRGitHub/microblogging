package com.abdou.microblogging.post;

import com.abdou.microblogging.account.Account;
import com.abdou.microblogging.comment.CommentService;
import com.abdou.microblogging.comment.dto.CommentDto;
import com.abdou.microblogging.comment.dto.CreateCommentDto;
import com.abdou.microblogging.post.dto.PostDto;
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
    public ResponseEntity<?> createComment(@PathVariable UUID id, @RequestBody CreateCommentDto createCommentDto, @AuthenticationPrincipal Account account) {
        return this.commentService.createComment(id, createCommentDto, account);
    }

    @GetMapping()
    public ResponseEntity<PagedModel<PostDto>> getPaginatedPosts(@RequestParam(defaultValue = "1") int page) {
        return postService.getPaginatedPosts(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostInfo(@PathVariable UUID id) {
        return postService.getPostInfo(id);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<PagedModel<CommentDto>> getPaginatedPostComments(@PathVariable UUID postId, @RequestParam(defaultValue = "1") int page) {
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
