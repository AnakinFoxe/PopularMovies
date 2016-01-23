package com.anakinfoxe.popularmovies.async;

/**
 * Created by xing on 1/17/16.
 */
public interface AsyncResponse<T> {

    void processFinish(T output);
}
