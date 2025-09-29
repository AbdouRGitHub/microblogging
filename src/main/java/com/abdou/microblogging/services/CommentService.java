package com.abdou.microblogging.services;

import com.abdou.microblogging.dto.comment.CommentDto;
import com.abdou.microblogging.entities.Account;
import com.abdou.microblogging.entities.Comment;
import com.abdou.microblogging.entities.Post;
import com.abdou.microblogging.exceptions.PostNotFoundException;
import com.abdou.microblogging.repositories.CommentRepository;
import com.abdou.microblogging.repositories.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public ResponseEntity<Comment> createComment(UUID id, CommentDto commentDto, Account account) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));

        Comment newComment = new Comment(commentDto.content(),post, account);

        Comment saved = commentRepository.save(newComment);
        return new ResponseEntity<>(saved,HttpStatus.CREATED);
    }

    public ResponseEntity<Comment> replyToComment(UUID id, CommentDto commentDto, Account account) {
        Comment parent = commentRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));

    }

    public ResponseEntity<?> deleteComment() {

    }

    public void editComment() {

    }
}
