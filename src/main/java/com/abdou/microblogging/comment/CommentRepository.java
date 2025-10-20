package com.abdou.microblogging.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID>, PagingAndSortingRepository<Comment, UUID> {
    Page<Comment> findByPostId(UUID postId, Pageable pageable);

    Page<Comment> findByParentId(UUID parentId, Pageable pageable);
}
