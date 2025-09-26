package com.abdou.microblogging.services;

import com.abdou.microblogging.dto.post.PostDto;
import com.abdou.microblogging.entities.Account;
import com.abdou.microblogging.entities.Post;
import com.abdou.microblogging.exceptions.PostNotFoundException;
import com.abdou.microblogging.repositories.AccountRepository;
import com.abdou.microblogging.repositories.PostRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final AccountRepository accountRepository;

    PostService(PostRepository postRepository, AccountRepository accountRepository) {
        this.postRepository = postRepository;
        this.accountRepository = accountRepository;
    }

    public ResponseEntity<Post> createPost(Account account, PostDto postDto) {
        Post post = new Post(
                postDto.content(),
                account);
        Post saved = postRepository.save(post);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    public ResponseEntity<PagedModel<Post>> getPaginatedPosts(int page) {
        return ResponseEntity.ok(
                new PagedModel<>(postRepository.findAll(Pageable.ofSize(10).withPage(page - 1)))
        );
    }

    public ResponseEntity<Post> getPostInfo(UUID id) {
        return ResponseEntity.ok(
                postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id))
        );
    }

    public ResponseEntity<Post> updatePost(PostDto postDto, UUID id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));

        if (postDto.content() != null) {
            post.setContent(postDto.content());
        }

        return ResponseEntity.ok(postRepository.save(post));
    }

    public ResponseEntity<?> deletePost(UUID id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
        postRepository.delete(post);
        return ResponseEntity.ok().build();
    }
}
