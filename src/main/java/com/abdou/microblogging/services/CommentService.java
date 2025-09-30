package com.abdou.microblogging.services;

import com.abdou.microblogging.dto.comment.CommentDto;
import com.abdou.microblogging.entities.Account;
import com.abdou.microblogging.entities.Comment;
import com.abdou.microblogging.entities.Post;
import com.abdou.microblogging.exceptions.CommentNotFoundException;
import com.abdou.microblogging.exceptions.PostNotFoundException;
import com.abdou.microblogging.repositories.CommentRepository;
import com.abdou.microblogging.repositories.PostRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

        Comment newComment = new Comment(commentDto.content(), post, account);

        Comment saved = commentRepository.save(newComment);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    public ResponseEntity<Comment> replyToComment(UUID id, CommentDto commentDto, Account account) {
        Comment parent = commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException(id));

        Comment replyComment = new Comment(commentDto.content(), account, parent);

        Comment saved = commentRepository.save(replyComment);

        return new ResponseEntity<>(saved, HttpStatus.CREATED);

    }

    public ResponseEntity<?> deleteComment(UUID id, Account account) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException(id));

        if (comment.getAccount().getId().equals(account.getId())) {
            commentRepository.delete(comment);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    public ResponseEntity<Comment> editComment() {
        return null;
    }

    public ResponseEntity<PagedModel<Comment>> getPaginatedComments(UUID postId, int page) {
        boolean postExists = postRepository.existsById(postId);

        if (postExists) {
            return ResponseEntity.ok().body(new PagedModel<>(commentRepository.findAllByPostId(postId, Pageable.ofSize(10).withPage(page - 1))));
        } else {
            throw new PostNotFoundException(postId);
        }
    }
}
