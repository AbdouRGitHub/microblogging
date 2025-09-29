package com.abdou.microblogging.repositories;

import com.abdou.microblogging.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID>, PagingAndSortingRepository<Comment, UUID> {
}
