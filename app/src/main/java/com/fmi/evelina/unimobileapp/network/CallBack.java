package com.fmi.evelina.unimobileapp.network;

/**
 * An interface for success and error hadlers
 */
public interface CallBack<T> {
    void onSuccess(T data);
    void onFail(String msg);
}
