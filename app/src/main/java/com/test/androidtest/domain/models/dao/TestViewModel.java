package com.test.androidtest.domain.models.dao;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.test.androidtest.data.database.TestDatabase;
import com.test.androidtest.domain.models.local.Photo;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class TestViewModel extends AndroidViewModel
{
    private TestDao mTestDao;
    private Application mApplication;

    public TestViewModel(@NonNull Application application)
    {
        super(application);
        mApplication = application;
        mTestDao = TestDatabase.getInstance(application).mTestDao();
    }

    public long insertPhoto(Photo photo)
    {
        try {
            return new AsyncTask<Void, Void, Long>()
            {
                @Override
                protected Long doInBackground(Void... voids)
                {
                    return mTestDao.insertPhoto(photo);
                }
            }.execute().get();
        }
        catch (InterruptedException | ExecutionException e)
        {
            e.printStackTrace();
            return Long.MIN_VALUE;
        }
    }

    public int deletePhoto(Photo photo)
    {
        try {
            return new AsyncTask<Void, Void, Integer>()
            {
                @Override
                protected Integer doInBackground(Void... voids)
                {
                    return mTestDao.delete(photo);
                }
            }.execute().get();
        }
        catch (InterruptedException | ExecutionException e)
        {
            e.printStackTrace();
            return Integer.MIN_VALUE;
        }
    }

    public List<Photo> getPhotos()
    {
        try {
            return new AsyncTask<Void, Void, List<Photo>>()
            {
                @Override
                protected List<Photo> doInBackground(Void... voids)
                {
                    return mTestDao.getPhotos();
                }
            }.execute().get();
        }
        catch (InterruptedException | ExecutionException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
