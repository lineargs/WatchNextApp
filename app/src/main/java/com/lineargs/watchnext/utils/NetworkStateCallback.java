package com.lineargs.watchnext.utils;

public interface NetworkStateCallback {
    void onLoading();
    void onSuccess();
    void onError(String message);
}
