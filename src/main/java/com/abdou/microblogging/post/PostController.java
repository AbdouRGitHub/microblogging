package com.abdou.microblogging.post;

import com.abdou.microblogging.account.Account;
import com.abdou.microblogging.like.LikeService;
import com.abdou.microblogging.post.dto.CreatePostDto;
import com.abdou.microblogging.post.dto.PostDetailsDto;
import jakarta.validation.Valid;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final LikeService likeService;

    PostController(PostService postService, LikeService likeService) {
        this.postService = postService;
        this.likeService = likeService;
    }

    @PostMapping()
    public ResponseEntity<PostDetailsDto> createPost(@AuthenticationPrincipal Account account,
                                                     @Valid @RequestBody CreatePostDto createPostDto
    ) {
        return postService.createPost(account, createPostDto);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Object> createComment(@PathVariable UUID id,
                                                @Valid @RequestBody CreatePostDto createPostDto,
                                                @AuthenticationPrincipal Account account
    ) {
        return postService.createComment(id, createPostDto, account);
    }

    @PostMapping("/{id}/likes")
    public ResponseEntity<Object> createLike(@PathVariable UUID id,
                                             @AuthenticationPrincipal Account account
    ) {
        return likeService.createLike(id, account);
    }

    @DeleteMapping("/{id}/likes")
    public ResponseEntity<Object> deleteLike(@PathVariable UUID id,
                                             @AuthenticationPrincipal Account account
    ) {
        return likeService.deleteLike(id, account);
    }

    @GetMapping()
    public ResponseEntity<PagedModel<PostDetailsDto>> getLatestPosts(@RequestParam(defaultValue = "1") int page,
                                                                     @AuthenticationPrincipal Account account
    ) {
        return postService.getLatestPosts(page, account);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<PagedModel<PostDetailsDto>> getPaginatedComments(@PathVariable("id") UUID postId,
                                                                           @RequestParam(defaultValue = "1") int page,
                                                                           @AuthenticationPrincipal Account account
    ) {
        return postService.getPaginatedComments(postId, page, account);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDetailsDto> getPostInfo(@PathVariable UUID id,
                                                      @AuthenticationPrincipal Account account
    ) {
        return postService.getPostInfo(id, account);
    }

    @GetMapping("/by-user/{id}")
    public ResponseEntity<PagedModel<PostDetailsDto>> getPaginatedUserPosts(@PathVariable UUID id,
                                                                            @RequestParam(defaultValue = "1") int page,
                                                                            @AuthenticationPrincipal Account account
    ) {
        return postService.getPaginatedUserPosts(id, page, account);
    }

    @GetMapping("/by-user/{id}/replies")
    public ResponseEntity<PagedModel<PostDetailsDto>> getPaginatedUserReplies(@PathVariable UUID id,
                                                                              @RequestParam(defaultValue = "1") int page,
                                                                              @AuthenticationPrincipal Account account
    ) {
        return postService.getPaginatedUserReplies(id, page, account);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Post> updatePost(@RequestBody PostDetailsDto postDetailsDto,
                                           @PathVariable UUID id
    ) {
        return postService.updatePost(postDetailsDto, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable UUID id) {
        return postService.deletePost(id);
    }
}
