package com.abdou.microblogging.post;

import com.abdou.microblogging.account.AccountPrincipal;
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
    public ResponseEntity<PostDetailsDto> createPost(@AuthenticationPrincipal AccountPrincipal principal,
                                                     @Valid @RequestBody CreatePostDto createPostDto
    ) {
        return postService.createPost(principal, createPostDto);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Object> createComment(@PathVariable UUID id,
                                                @Valid @RequestBody CreatePostDto createPostDto,
                                                @AuthenticationPrincipal AccountPrincipal principal
    ) {
        return postService.createComment(id, createPostDto, principal);
    }

    @PostMapping("/{id}/likes")
    public ResponseEntity<Object> createLike(@PathVariable UUID id,
                                             @AuthenticationPrincipal AccountPrincipal principal
    ) {
        return likeService.createLike(id, principal);
    }

    @DeleteMapping("/{id}/likes")
    public ResponseEntity<Object> deleteLike(@PathVariable UUID id,
                                             @AuthenticationPrincipal AccountPrincipal principal
    ) {
        return likeService.deleteLike(id, principal);
    }

    @GetMapping()
    public ResponseEntity<PagedModel<PostDetailsDto>> getLatestPosts(@RequestParam(defaultValue = "1") int page,
                                                                     @AuthenticationPrincipal AccountPrincipal principal
    ) {
        return postService.getLatestPosts(page, principal);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<PagedModel<PostDetailsDto>> getPaginatedComments(@PathVariable("id") UUID postId,
                                                                           @RequestParam(defaultValue = "1") int page,
                                                                           @AuthenticationPrincipal AccountPrincipal principal
    ) {
        return postService.getPaginatedComments(postId, page, principal);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDetailsDto> getPostInfo(@PathVariable UUID id,
                                                      @AuthenticationPrincipal AccountPrincipal principal
    ) {
        return postService.getPostInfo(id, principal);
    }

    @GetMapping("/by-user/{id}")
    public ResponseEntity<PagedModel<PostDetailsDto>> getPaginatedUserPosts(@PathVariable UUID id,
                                                                            @RequestParam(defaultValue = "1") int page,
                                                                            @AuthenticationPrincipal AccountPrincipal principal
    ) {
        return postService.getPaginatedUserPosts(id, page, principal);
    }

    @GetMapping("/by-user/{id}/replies")
    public ResponseEntity<PagedModel<PostDetailsDto>> getPaginatedUserReplies(@PathVariable UUID id,
                                                                              @RequestParam(defaultValue = "1") int page,
                                                                              @AuthenticationPrincipal AccountPrincipal principal
    ) {
        return postService.getPaginatedUserReplies(id, page, principal);
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
