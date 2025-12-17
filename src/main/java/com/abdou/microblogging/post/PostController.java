package com.abdou.microblogging.post;

import com.abdou.microblogging.account.Account;
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
    
    PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping()
    public ResponseEntity<Post> createPost(@AuthenticationPrincipal Account account, @RequestBody PostDto postDto) {
        return postService.createPost(account, postDto);
    }

    @GetMapping()
    public ResponseEntity<PagedModel<PostDto>> getPaginatedPosts(@RequestParam(defaultValue = "1") int page) {
        return postService.getPaginatedPosts(page);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<PagedModel<PostDto>> getPaginatedComments(@PathVariable("id") UUID postId, @RequestParam(defaultValue = "1") int page) {
        return postService.getPaginatedComments(postId, page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostInfo(@PathVariable UUID id) {
        return postService.getPostInfo(id);
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
