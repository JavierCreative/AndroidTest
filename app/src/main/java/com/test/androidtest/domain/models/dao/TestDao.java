package com.test.androidtest.domain.models.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.test.androidtest.domain.models.local.Photo;

import java.util.List;

@Dao
public interface TestDao
{
    @Insert
    long insertPhoto(Photo photo);

    @Delete
    int delete(Photo photo);

    @Query("SELECT * FROM Photos")
    List<Photo> getPhotos();
}
