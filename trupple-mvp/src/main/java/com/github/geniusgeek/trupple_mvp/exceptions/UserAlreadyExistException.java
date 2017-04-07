package com.github.geniusgeek.trupple_mvp.exceptions;

/**
 * Created by Genius on 12/9/2015.
 */
public final class UserAlreadyExistException extends AuthenticationException {
    /**
     * create an {@link UserAlreadyExistException}
     * @param msg error message
     * @param t throwable
     */
    public UserAlreadyExistException(String msg, Throwable t) {
        super(msg, t);
    }

    /**
     * create a {@link UserAlreadyExistException}
     * @param msg error message
     */
    public UserAlreadyExistException(String msg) {
        super(msg);
    }
}