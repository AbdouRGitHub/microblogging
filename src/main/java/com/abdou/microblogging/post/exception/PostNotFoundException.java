package com.abdou.microblogging.post.exception;

import java.util.UUID;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(UUID id) {
        super("Could not find post with id #" + id);
    }
}
