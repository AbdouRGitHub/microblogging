package com.abdou.microblogging.controllers;

import com.abdou.microblogging.dto.post.PostDto;
import com.abdou.microblogging.entities.Post;
import com.abdou.microblogging.exceptions.AccountNotFoundException;
import com.abdou.microblogging.exceptions.PostNotFoundException;
import com.abdou.microblogging.repositories.AccountRepository;
import com.abdou.microblogging.repositories.PostRepository;
import com.abdou.microblogging.services.PostService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostRepository postRepository;
    private final AccountRepository accountRepository;
    private final PostService postService;

    PostController(PostRepository postRepository, AccountRepository accountRepository, PostService postService) {
        this.postRepository = postRepository;
        this.accountRepository = accountRepository;
        this.postService = postService;
    }

    @PostMapping("/account/{id}")
    public ResponseEntity<Post> createPost(@PathVariable(name = "id") UUID AccountId, @RequestBody PostDto postDto) {
        return postService.createPost(AccountId, postDto);
    }

    @GetMapping()
    public ResponseEntity<PagedModel<Post>> getPaginatedPosts(@RequestParam(defaultValue = "1") int page) {
        return postService.getPaginatedPosts(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostInfo(@PathVariable UUID id) {
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
