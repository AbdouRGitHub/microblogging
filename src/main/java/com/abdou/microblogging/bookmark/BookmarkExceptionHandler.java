package com.abdou.microblogging.bookmark;

import com.abdou.microblogging.bookmark.exception.BookmarkAlreadyExistException;
import com.abdou.microblogging.bookmark.exception.BookmarkNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BookmarkExceptionHandler {
    @ExceptionHandler(BookmarkAlreadyExistException.class)
    ResponseEntity<String> bookmarkAlreadyExist(BookmarkAlreadyExistException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(BookmarkNotFoundException.class)
    ResponseEntity<String> bookmarkNotFound(BookmarkNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

}
