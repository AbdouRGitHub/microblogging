package com.abdou.microblogging.post.exception;

import java.util.UUID;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(UUID id) {
        super("Could not find comment with id #" + id);
    }
}
