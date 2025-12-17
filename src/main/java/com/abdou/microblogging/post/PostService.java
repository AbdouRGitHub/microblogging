package com.abdou.microblogging.post;

import com.abdou.microblogging.account.Account;
import com.abdou.microblogging.account.AccountRepository;
import com.abdou.microblogging.common.exceptions.PostNotFoundException;
import com.abdou.microblogging.post.dto.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PostService {

    private final PostRepository postRepository;

    PostService(PostRepository postRepository,
                AccountRepository accountRepository
    ) {
        this.postRepository = postRepository;
    }

    public ResponseEntity<Post> createPost(Account account,
                                           PostDto postDto
    ) {
        Post post = new Post(postDto.content(), account);
        Post saved = postRepository.save(post);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    public ResponseEntity<PagedModel<PostDto>> getPaginatedPosts(int page) {
        Page<Post> posts =
                postRepository.findLatestPosts(Pageable.ofSize(10)
                        .withPage(page - 1));
        return ResponseEntity.ok()
                .body(new PagedModel<>(posts.map(PostDto::toPostResponseDto)));
    }

    public ResponseEntity<PagedModel<PostDto>> getPaginatedComments(UUID postId,
                                                                    int page
    ) {
        Page<Post> posts =
                postRepository.findLatestComments(Pageable.ofSize(10)
                        .withPage(page - 1), postId);
        return ResponseEntity.ok()
                .body(new PagedModel<>(posts.map(PostDto::toPostResponseDto)));
    }

    public ResponseEntity<PostDto> getPostInfo(UUID id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        return ResponseEntity.ok(PostDto.toPostResponseDto(post));
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
