package com.abdou.microblogging.repositories;

import com.abdou.microblogging.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID>, PagingAndSortingRepository<Post, UUID> {
}
