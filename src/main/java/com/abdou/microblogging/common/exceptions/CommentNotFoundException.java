package com.abdou.microblogging.common.exceptions;

import java.util.UUID;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(UUID id) {
        super("Could not find comment with id #" + id);
    }
}
