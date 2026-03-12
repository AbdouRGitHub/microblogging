package com.abdou.microblogging.bookmark.exception;

public class BookmarkAlreadyExistException extends RuntimeException {
    public BookmarkAlreadyExistException(String message) {
        super(message);
    }
}
