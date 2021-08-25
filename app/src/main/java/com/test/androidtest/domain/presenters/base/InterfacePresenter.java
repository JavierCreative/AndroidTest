package com.test.androidtest.domain.presenters.base;

public interface InterfacePresenter<V extends InterfaceView>
{
    void attach(V view);
}
