package com.abdou.microblogging.exceptions;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(long id) {
        super("Could not find account with id #" + id);
    }
}
