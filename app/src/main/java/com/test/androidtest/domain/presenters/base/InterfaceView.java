package com.test.androidtest.domain.presenters.base;

public interface InterfaceView
{
    void showProgress();
    void hideProgress();
    void onError(String... args);
}
