package com.abdou.microblogging.advices;

import com.abdou.microblogging.exceptions.PostNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class PostExceptionAdvice {
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<String> postNotFound(PostNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
