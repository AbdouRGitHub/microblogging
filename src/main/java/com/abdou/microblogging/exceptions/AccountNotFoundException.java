package com.abdou.microblogging.exceptions;

import java.util.UUID;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(UUID id) {
        super("Could not find account with id #" + id);
    }
}
