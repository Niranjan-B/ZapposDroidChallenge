package com.ninja.domain;

import io.reactivex.Observable;

/**
 * Created by niranjanb on 04/02/17.
 */

public abstract class UseCase<T> {
    public abstract Observable<T> buildObservable();

    public Observable<T> execute() {
        return buildObservable();
    }
}
