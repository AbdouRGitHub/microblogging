package com.abdou.microblogging.post;

import com.abdou.microblogging.account.Account;
import com.abdou.microblogging.common.exceptions.PostNotFoundException;
import com.abdou.microblogging.account.AccountRepository;
import com.abdou.microblogging.post.dto.PostDto;
import com.abdou.microblogging.post.dto.PostResponseDto;
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
        Post post = new Post(postDto.content(), account);
        Post saved = postRepository.save(post);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    public ResponseEntity<PagedModel<PostResponseDto>> getPaginatedPosts(int page) {
        return ResponseEntity.ok()
                .body(new PagedModel<>(postRepository.findAll(Pageable.ofSize(10)
                                .withPage(page - 1))
                        .map(post -> new PostResponseDto(post.getId(),
                                post.getContent(),
                                post.getCreatedAt(),
                                post.getUpdatedAt()
                        ))));
    }

    public ResponseEntity<Post> getPostInfo(UUID id) {
        return ResponseEntity.ok(postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id)));
    }

    public ResponseEntity<Post> updatePost(PostDto postDto, UUID id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        if (postDto.content() != null) {
            post.setContent(postDto.content());
        }

        return ResponseEntity.ok(postRepository.save(post));
    }

    public ResponseEntity<?> deletePost(UUID id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        postRepository.delete(post);
        return ResponseEntity.ok().build();
    }
}
