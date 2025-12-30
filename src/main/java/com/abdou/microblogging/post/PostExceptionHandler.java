package com.abdou.microblogging.post;

import com.abdou.microblogging.post.exception.CommentNotFoundException;
import com.abdou.microblogging.post.exception.PostNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class PostExceptionHandler {
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<String> postNotFound(PostNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<String> commentNotFound(CommentNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
