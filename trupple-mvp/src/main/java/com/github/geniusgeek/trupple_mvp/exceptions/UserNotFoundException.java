package com.github.geniusgeek.trupple_mvp.exceptions;


/**
 * This is a checked exception that allows users to recover from user not found exception
 * when a query on the user database is performed using {@Code Integer} userId
 * Created by Genius on 12/7/2015.
 */
public final class UserNotFoundException extends Exception {

    /**
     * create an {@link UserNotFoundException}
     * @param msg error message
      */
    public UserNotFoundException(String msg) {
        super(msg);
    }

    /**
     * create an {@link UserNotFoundException}
     * @param msg error message
     * @param t throwable
     */
    public UserNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    @Override
    public String toString() {

        return super.toString();
    }
}
