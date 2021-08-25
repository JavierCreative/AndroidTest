package com.test.androidtest.domain.presenters.contracts;

import com.test.androidtest.domain.models.remote.Photo;
import com.test.androidtest.domain.presenters.base.InterfacePresenter;
import com.test.androidtest.domain.presenters.base.InterfaceView;

import java.util.List;

public interface PhotosContract
{
    interface PhotosInterfaceView extends InterfaceView
    {
        void onSuccessGetPhotos(List<Photo> photos);
    }

    interface PhotosInterfacePresenter<V extends PhotosContract.PhotosInterfaceView> extends InterfacePresenter<V>
    {
        void getPhotos(int page, int items);
    }
}
