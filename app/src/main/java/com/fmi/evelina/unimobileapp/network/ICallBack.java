package com.fmi.evelina.unimobileapp.network;

/**
 * An interface for success and error hadlers
 */
public interface ICallBack<T> {
    void onSuccess(T data);
    void onFail(String msg);
}
