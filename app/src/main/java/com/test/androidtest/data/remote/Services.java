package com.test.androidtest.data.remote;

import com.test.androidtest.domain.models.remote.Photo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Services
{
    String API_KEY = "Yy_X3lZde_KPiCa7Mua4Q4L96aAy8egMviyvcnLO4WA";

    @Headers("Authorization: Client-ID "+API_KEY)
    @GET(EndPoints.GET.PHOTOS)
    Call<List<Photo>> getPhotos(@Query("page") int page, @Query("per_page") int items);

    @Headers("Authorization: Client-ID "+API_KEY)
    @GET(EndPoints.GET.USER_PHOTOS)
    Call<List<Photo>> getUserPhotos(@Path ("username") String username, @Query("page") int page, @Query("per_page") int items);
}
