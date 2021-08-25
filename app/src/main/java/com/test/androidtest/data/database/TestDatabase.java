package com.test.androidtest.data.database;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.test.androidtest.domain.models.dao.TestDao;
import com.test.androidtest.domain.models.local.Photo;

@Database(version = 1, entities = { Photo.class})
public abstract class TestDatabase extends RoomDatabase
{
    public abstract TestDao mTestDao();
    private static TestDatabase mDatabaseInstance;
    private static final String NAME_DATABASE = "TestDatabase";
    private final MutableLiveData<Boolean> mDatabaseIsCreated = new MutableLiveData<>();

    public static TestDatabase getInstance(final Context context)
    {
        if (mDatabaseInstance == null)
        {
            synchronized (TestDatabase.class)
            {
                mDatabaseInstance = buildDatabase(context.getApplicationContext());
                mDatabaseInstance.updateDatabaseCreated(context.getApplicationContext());
            }
        }
        return mDatabaseInstance;
    }

    private static TestDatabase buildDatabase(final Context appContext)
    {
        return Room.databaseBuilder(appContext, TestDatabase.class, NAME_DATABASE)
                .fallbackToDestructiveMigration()
                .build();
    }

    private void updateDatabaseCreated(final Context context)
    {
        if (context.getDatabasePath(NAME_DATABASE).exists())
        {
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated()
    {
        mDatabaseIsCreated.postValue(true);
    }

    public Void clearAllData()
    {
        return mDatabaseInstance.clearAllData();
    }

    public LiveData<Boolean> getDatabaseCreated()
    {
        return mDatabaseIsCreated;
    }

    public static void destroyInstance()
    {
        mDatabaseInstance = null;
    }
}
