package com.anakinfoxe.popularmovies.async;

/**
 * Created by xing on 1/26/16.
 */
public class AsyncResult<T> {

    private T result = null;
    private String errorMsg = null;

    public AsyncResult(T result) {
        this.result = result;
    }

    public AsyncResult(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getResult() {
        return result;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public boolean hasError() {
        return errorMsg != null;
    }
}
