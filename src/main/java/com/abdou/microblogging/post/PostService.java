package com.abdou.microblogging.post;

import com.abdou.microblogging.account.Account;
import com.abdou.microblogging.account.AccountPrincipal;
import com.abdou.microblogging.account.AccountRepository;
import com.abdou.microblogging.account.exception.AccountNotFoundException;
import com.abdou.microblogging.like.LikeService;
import com.abdou.microblogging.like.dto.LikeDetailsDto;
import com.abdou.microblogging.post.dto.CreatePostDto;
import com.abdou.microblogging.post.dto.PostDetailsDto;
import com.abdou.microblogging.post.exception.PostNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PostService {

    private final static Logger logger =
            LoggerFactory.getLogger(PostService.class);
    private final PostRepository postRepository;
    private final LikeService likeService;
    private final AccountRepository accountRepository;

    PostService(PostRepository postRepository, LikeService likeService, AccountRepository accountRepository) {
        this.postRepository = postRepository;
        this.likeService = likeService;
        this.accountRepository = accountRepository;
    }

    public ResponseEntity<PostDetailsDto> createPost(AccountPrincipal principal,
                                                     CreatePostDto createPostDto
    ) {
        Account account = accountRepository.findById(principal.getId())
                .orElseThrow(() -> new AccountNotFoundException(principal.getId()));
        Post post = new Post(createPostDto.content(), account);
        Post saved = postRepository.save(post);
        logger.info("Post created: {}", saved.getId());
        return new ResponseEntity<>(PostDetailsDto.toDto(saved, 0, null),
                HttpStatus.CREATED);
    }

    public ResponseEntity<Object> createComment(UUID postId,
                                                CreatePostDto createPostDto,
                                                AccountPrincipal principal
    ) {
        Account account = accountRepository.findById(principal.getId())
                .orElseThrow(() -> new AccountNotFoundException(principal.getId()));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
        Post commentPost = new Post(createPostDto.content(), account, post);
        postRepository.save(commentPost);
        logger.info("Comment created: {}", commentPost.getId());
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<PagedModel<PostDetailsDto>> getLatestPosts(int page,
                                                                     AccountPrincipal principal
    ) {
        Page<Post> posts = postRepository.findLatestPosts(Pageable.ofSize(10)
                .withPage(page - 1));
        return ResponseEntity.ok().body(new PagedModel<>(posts.map(post -> {
            LikeDetailsDto likes =
                    likeService.getLikeDetails(post.getId(), principal);
            int commentsCount = getNumberOfComments(post.getId());
            return PostDetailsDto.toDto(post, commentsCount, likes);
        })));
    }

    public ResponseEntity<PagedModel<PostDetailsDto>> getPaginatedComments(UUID postId,
                                                                           int page,
                                                                           AccountPrincipal principal
    ) {
        Page<Post> posts = postRepository.findLatestComments(Pageable.ofSize(10)
                .withPage(page - 1), postId);
        return ResponseEntity.ok().body(new PagedModel<>(posts.map(post -> {
            LikeDetailsDto likes =
                    likeService.getLikeDetails(post.getId(), principal);
            int commentsCount = getNumberOfComments(post.getId());
            return PostDetailsDto.toDto(post, commentsCount, likes);
        })));
    }

    public ResponseEntity<PostDetailsDto> getPostInfo(UUID id,
                                                      AccountPrincipal principal
    ) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        LikeDetailsDto likes = likeService.getLikeDetails(id, principal);
        int commentsCount = getNumberOfComments(post.getId());
        return ResponseEntity.ok(PostDetailsDto.toDto(post,
                commentsCount,
                likes));
    }

    public ResponseEntity<PagedModel<PostDetailsDto>> getPaginatedUserPosts(UUID userId,
                                                                            int page,
                                                                            AccountPrincipal principal
    ) {
        Page<Post> posts = postRepository.findUserPosts(Pageable.ofSize(10)
                .withPage(page - 1), userId);

        return ResponseEntity.ok(new PagedModel<>(posts.map(post -> {
            LikeDetailsDto likes =
                    likeService.getLikeDetails(post.getId(), principal);
            int commentsCount = getNumberOfComments(post.getId());
            return PostDetailsDto.toDto(post, commentsCount, likes);
        })));
    }

    public ResponseEntity<PagedModel<PostDetailsDto>> getPaginatedUserReplies(
            UUID userId,
            int page,
            AccountPrincipal principal

    ) {
        Page<Post> posts = postRepository.findUserReplies(Pageable.ofSize(10)
                .withPage(page - 1), userId);

        return ResponseEntity.ok(new PagedModel<>(posts.map(post -> {
            LikeDetailsDto likes =
                    likeService.getLikeDetails(post.getId(), principal);
            int commentsCount = getNumberOfComments(post.getId());
            return PostDetailsDto.toDto(post, commentsCount, likes);
        })));
    }

    public int getNumberOfComments(UUID postId) {
        return postRepository.countReplies(postId);
    }

    public ResponseEntity<Post> updatePost(PostDetailsDto postDetailsDto,
                                           UUID id
    ) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        if (postDetailsDto.content() != null) {
            post.setContent(postDetailsDto.content());
        }
        Post updated = postRepository.save(post);
        logger.info("Post updated: {}", updated.getId());
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> deletePost(UUID id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        postRepository.delete(post);
        logger.info("Post deleted: {}", post.getId());
        return ResponseEntity.ok().build();
    }
}
