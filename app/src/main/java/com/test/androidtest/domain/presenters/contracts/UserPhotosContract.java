package com.test.androidtest.domain.presenters.contracts;

import com.test.androidtest.domain.models.remote.Photo;
import com.test.androidtest.domain.presenters.base.InterfacePresenter;
import com.test.androidtest.domain.presenters.base.InterfaceView;

import java.util.List;

public interface UserPhotosContract
{
    interface UserPhotosInterfaceView extends InterfaceView
    {
        void onSuccessGetPhotos(List<Photo> photos);
    }

    interface UserPhotosInterfacePresenter<V extends UserPhotosInterfaceView> extends InterfacePresenter<V>
    {
        void getUserPhotos(String username, int page, int items);
    }
}
