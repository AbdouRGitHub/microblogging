package com.abdou.microblogging.account.exception;

import java.util.UUID;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException() {
    }

    public AccountNotFoundException(UUID id) {
        super("Could not find account with id #" + id);
    }
}
