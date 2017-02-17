package com.github.geniusgeek.trupple_mvp.common;

import java.util.concurrent.Semaphore;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func0;

/**
 * Created by root on 1/12/17.
 */

public final class OnErrorRetryCache<T> {

    private final Observable<T> deferred;
    private final Semaphore singlePermit = new Semaphore(1);
    private Observable<T> cache = null;
    private Observable<T> inProgress = null;
    private OnErrorRetryCache(final Observable<T> source) {
        deferred = Observable.defer(new Func0<Observable<T>>() {
            @Override
            public Observable<T> call() {
                return createWhenObserverSubscribes(source);
            }
        });
    }

    public static <T> Observable<T> from(Observable<T> source) {
        return new OnErrorRetryCache<>(source).deferred;
    }

    private Observable<T> createWhenObserverSubscribes(Observable<T> source) {
        singlePermit.acquireUninterruptibly();

        Observable<T> cached = cache;
        if (cached != null) {
            singlePermit.release();
            return cached;
        }

        inProgress = source
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        onSuccess();
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        onTermination();
                    }
                })
                .replay()
                .autoConnect();

        return inProgress;
    }

    private void onSuccess() {
        cache = inProgress;
    }

    private void onTermination() {
        inProgress = null;
        singlePermit.release();
    }
}
