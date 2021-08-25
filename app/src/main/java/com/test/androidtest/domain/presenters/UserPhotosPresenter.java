package com.test.androidtest.domain.presenters;

import com.test.androidtest.data.remote.ServiceGenerator;
import com.test.androidtest.data.remote.Services;
import com.test.androidtest.domain.models.remote.Photo;
import com.test.androidtest.domain.presenters.base.BasePresenter;
import com.test.androidtest.domain.presenters.contracts.UserPhotosContract;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserPhotosPresenter<V extends UserPhotosContract.UserPhotosInterfaceView> extends BasePresenter<V> implements UserPhotosContract.UserPhotosInterfacePresenter<V>
{
    @Override
    public void getUserPhotos(String username, int page, int items)
    {
        getView().showProgress();
        Call<List<Photo>> call = ServiceGenerator.createService(Services.class).getUserPhotos(username, page, items);
        call.enqueue(new Callback<List<Photo>>()
        {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response)
            {
                getView().hideProgress();
                if (response.isSuccessful())
                {
                    getView().onSuccessGetPhotos(response.body());
                }
                else
                {
                    getView().onError("an error has occurred, try again later");
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t)
            {
                getView().hideProgress();
                getView().onError("an error has occurred, try again later");
            }
        });
    }
}
