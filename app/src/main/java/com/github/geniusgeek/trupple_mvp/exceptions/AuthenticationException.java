package com.github.geniusgeek.trupple_mvp.exceptions;

/**
 * Created by DOTECH on 28/04/2016.
 */
public  class AuthenticationException extends Exception {
    public AuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public AuthenticationException(String msg) {
        super(msg);
    }
}
