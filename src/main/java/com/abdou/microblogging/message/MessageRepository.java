package com.abdou.microblogging.message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID>, PagingAndSortingRepository<Message, UUID> {
    @Query("SELECT m FROM Message m WHERE m.parent IS NULL ORDER BY m.createdAt ASC")
    Page<Message> findLatestPosts(Pageable pageable);

    @Query("SELECT m FROM Message m WHERE m.parent.id = :parentId ORDER BY m.createdAt ASC")
    Page<Message> findLatestComments(Pageable pageable,
                                     @Param("parentId") UUID parentId
    );
}
