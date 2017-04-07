package com.github.geniusgeek.trupple_mvp.common;

/**
 * A builder Factory Interface to create Object of types @param<T>
 * Created by Genius on 11/16/2015.
 * @param <T> the type of object to create a builder for
 */
public interface Builder<T> {
    //T build(T type);

    /**build the object
     * @return<T> the Object
     */
    T build();

}
