package com.abdou.microblogging.auth;

public record LoginRequest(
        String username,
        String password
) {
}
