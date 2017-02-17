package com.github.geniusgeek.trupple_mvp.utils;

/**
 * Created by Genius on 11/16/2015.
 */
public interface Builder<T> {
    T build(T type);

    T build();

}
