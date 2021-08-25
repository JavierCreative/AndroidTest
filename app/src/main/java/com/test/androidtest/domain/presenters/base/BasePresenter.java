package com.test.androidtest.domain.presenters.base;

public class BasePresenter<V extends InterfaceView> implements InterfacePresenter<V>
{
    protected V mView;

    @Override
    public void attach(V view)
    {
        mView = view;
    }

    protected V getView()
    {
        return mView;
    }
}
