package com.abdou.microblogging.post;

import com.abdou.microblogging.account.Account;
import com.abdou.microblogging.like.LikeRepository;
import com.abdou.microblogging.post.dto.PostDetailsDto;
import com.abdou.microblogging.post.exception.PostNotFoundException;
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
    private final LikeRepository likeRepository;

    PostService(PostRepository postRepository,
                LikeRepository likeRepository
    ) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
    }

    public ResponseEntity<Post> createPost(Account account,
                                           PostDetailsDto postDetailsDto
    ) {
        Post post = new Post(postDetailsDto.content(), account);
        Post saved = postRepository.save(post);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    public ResponseEntity<PagedModel<PostDetailsDto>> getLatestPosts(int page) {
        Page<Post> posts =
                postRepository.findLatestPosts(Pageable.ofSize(10)
                        .withPage(page - 1));
        return ResponseEntity.ok()
                .body(new PagedModel<>(posts.map(post -> {
                    int likes = likeRepository.countByPostId(post.getId());
                    return PostDetailsDto.toPostResponseDto(post, likes);
                })));
    }

    public ResponseEntity<PagedModel<PostDetailsDto>> getPaginatedComments(UUID postId,
                                                                           int page
    ) {
        Page<Post> posts =
                postRepository.findLatestComments(Pageable.ofSize(10)
                        .withPage(page - 1), postId);
        return ResponseEntity.ok()
                .body(new PagedModel<>(posts.map(post -> {
                    int likes = likeRepository.countByPostId(post.getId());
                    return PostDetailsDto.toPostResponseDto(post, likes);
                })));
    }

    public ResponseEntity<PostDetailsDto> getPostInfo(UUID id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        int likes = likeRepository.countByPostId(id);
        return ResponseEntity.ok(PostDetailsDto.toPostResponseDto(post, likes));
    }

    public ResponseEntity<PagedModel<PostDetailsDto>> getPaginatedUserPosts(UUID userId,
                                                                            int page
    ) {
        Page<Post> posts = postRepository.findUserPosts(Pageable.ofSize(10)
                .withPage(page - 1), userId);

        return ResponseEntity.ok(new PagedModel<>(posts.map(post -> {
            int likes = likeRepository.countByPostId(post.getId());
            return PostDetailsDto.toPostResponseDto(post, likes);
        })));
    }

    public ResponseEntity<PagedModel<PostDetailsDto>> getPaginatedUserReplies(
            UUID userId,
            int page
    ) {
        Page<Post> posts = postRepository.findUserReplies(Pageable.ofSize(10)
                .withPage(page - 1), userId);

        return ResponseEntity.ok(new PagedModel<>(posts.map(post -> {
            int likes = likeRepository.countByPostId(post.getId());
            return PostDetailsDto.toPostResponseDto(post, likes);
        })));
    }

    public ResponseEntity<Post> updatePost(PostDetailsDto postDetailsDto,
                                           UUID id
    ) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        if (postDetailsDto.content() != null) {
            post.setContent(postDetailsDto.content());
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
