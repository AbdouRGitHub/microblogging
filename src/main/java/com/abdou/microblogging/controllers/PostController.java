package com.abdou.microblogging.controllers;

import com.abdou.microblogging.dto.post.PostDto;
import com.abdou.microblogging.entities.Post;
import com.abdou.microblogging.exceptions.AccountNotFoundException;
import com.abdou.microblogging.exceptions.PostNotFoundException;
import com.abdou.microblogging.repositories.AccountRepository;
import com.abdou.microblogging.repositories.PostRepository;
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

    PostController(PostRepository postRepository, AccountRepository accountRepository) {
        this.postRepository = postRepository;
        this.accountRepository = accountRepository;
    }

    @PostMapping("/account/{id}")
    public ResponseEntity<Post> createPost(@PathVariable(name = "id") UUID AccountId, @RequestBody PostDto postDto) {
        Post post = new Post(postDto.content(), accountRepository.findById(AccountId).orElseThrow(() -> new AccountNotFoundException(AccountId)));
        return ResponseEntity.ok().body(postRepository.save(post));
    }

    @GetMapping()
    public ResponseEntity<PagedModel<Post>> getPaginatedPosts(@RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.ok().body(new PagedModel<>(postRepository.findAll(Pageable.ofSize(10).withPage(page - 1))));
    }

    @GetMapping("{id}")
    public ResponseEntity<Post> getPostInfo(@PathVariable UUID id) {
        return ResponseEntity.ok().body(postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deletePost(@PathVariable UUID id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
        postRepository.delete(post);
        return ResponseEntity.ok().build();
    }
}
