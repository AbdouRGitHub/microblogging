package com.abdou.microblogging.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID>, PagingAndSortingRepository<Post, UUID> {
    @Query("SELECT m FROM Post m WHERE m.parent IS NULL ORDER BY m.createdAt ASC")
    Page<Post> findLatestPosts(Pageable pageable);

    @Query("SELECT m FROM Post m WHERE m.parent.id = :parentId ORDER BY m.createdAt ASC")
    Page<Post> findLatestComments(Pageable pageable,
                                  @Param("parentId") UUID parentId
    );
}
