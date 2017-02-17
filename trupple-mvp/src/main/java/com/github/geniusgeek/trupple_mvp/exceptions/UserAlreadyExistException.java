package com.github.geniusgeek.trupple_mvp.exceptions;

/**
 * Created by Genius on 12/9/2015.
 */
public final class UserAlreadyExistException extends AuthenticationException {
    public UserAlreadyExistException(String msg, Throwable t) {
        super(msg, t);
    }

    public UserAlreadyExistException(String msg) {
        super(msg);
    }
}