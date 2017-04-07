package com.github.geniusgeek.trupple_mvp.exceptions;

/** An exception to flag due to failed authentication
 * Created by Genius on 28/04/2016.
 */
public  class AuthenticationException extends Exception {

    /**
     * create an AuthenticationException
     * @param msg error message
     * @param t throwable
     */
    public AuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    /**
     * create an AuthenticationException
     * @param msg error message
     */
    public AuthenticationException(String msg) {
        super(msg);
    }
}
