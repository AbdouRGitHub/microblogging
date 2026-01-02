package com.abdou.microblogging.like;

import com.abdou.microblogging.account.Account;
import com.abdou.microblogging.like.dto.LikeDetailsDto;
import com.abdou.microblogging.post.Post;
import com.abdou.microblogging.post.PostRepository;
import com.abdou.microblogging.post.exception.PostNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    LikeService(LikeRepository likeRepository, PostRepository postRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
    }

    public ResponseEntity<Object> createLike(UUID postId, Account account) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
        if (isLikedByAccount(postId, account)) {
            return ResponseEntity.status(409)
                    .body("You already liked this post.");
        }
        Like like = new Like(post, account);
        likeRepository.save(like);
        return ResponseEntity.ok(like);
    }

    public int getNumberOfLikes(UUID postId) {
        return likeRepository.countByPostId(postId);
    }

    public boolean isLikedByAccount(UUID postId, Account account) {
        return likeRepository.isUserLiked(account.getId(), postId);
    }

    public LikeDetailsDto getLikeDetails(UUID postId, Account account) {
        if (account == null) {
            return LikeDetailsDto.toDto(getNumberOfLikes(postId));
        } else {
            return LikeDetailsDto.toDto(getNumberOfLikes(postId),
                    isLikedByAccount(postId, account));
        }
    }
}
