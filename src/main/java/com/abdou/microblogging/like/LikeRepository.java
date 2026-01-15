package com.abdou.microblogging.like;

import com.abdou.microblogging.account.Account;
import com.abdou.microblogging.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface LikeRepository extends JpaRepository<Like, UUID>, PagingAndSortingRepository<Like, UUID> {
    @Query("SELECT COUNT(l) FROM Like l WHERE l.post.id = :postId")
    int countByPostId(UUID postId);

    @Query("SELECT COUNT(l) > 0 FROM Like l WHERE l.account.id = :accountId AND l.post.id = :postId")
    boolean isUserLiked(UUID accountId, UUID postId);

    @Query("SELECT l FROM Like l WHERE l.account = :account AND l.post = :post")
    Like findByAccountAndPost(Account account, Post post);
}