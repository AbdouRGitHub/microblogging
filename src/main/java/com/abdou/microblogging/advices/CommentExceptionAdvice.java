package com.abdou.microblogging.advices;

import com.abdou.microblogging.exceptions.CommentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice

public class CommentExceptionAdvice {

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<String> commentNotFound(CommentNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
