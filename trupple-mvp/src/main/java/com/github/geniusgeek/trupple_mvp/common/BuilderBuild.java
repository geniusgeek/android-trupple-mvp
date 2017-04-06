package com.github.geniusgeek.trupple_mvp.common;


/**
 * Created by Genius
 * this is a buildBuilder Generic interface that build builders, its sort of builder factory
 *
 * @param <B> the builder object
 * @param <R> the return type of the object to build
 */
public interface BuilderBuild<B extends Builder<R>, R> {
    R build(B builder);

}
