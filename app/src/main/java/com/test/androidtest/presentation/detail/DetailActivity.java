package com.test.androidtest.presentation.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.test.androidtest.databinding.ActivityDetailBinding;
import com.test.androidtest.presentation.base.BaseActivity;
import com.test.androidtest.presentation.profile.ProfileActivity;

public class DetailActivity extends BaseActivity<ActivityDetailBinding>
{
    private static final String USERNAME_KEY = "com.test.androidtest.presentation.detail.DetailActivity.UsernameKey";
    private static final String NAME_KEY = "com.test.androidtest.presentation.detail.DetailActivity.NameKey";
    private static final String PHOTO_KEY = "com.test.androidtest.presentation.detail.DetailActivity.PhotoKey";
    private static final String USER_PHOTO_KEY = "com.test.androidtest.presentation.detail.DetailActivity.UserPhotoKey";
    private static final String TOTAL_PHOTOS_KEY = "com.test.androidtest.presentation.detail.DetailActivity.TotalPhotosKey";
    private static final String TOTAL_LIKES_KEY = "com.test.androidtest.presentation.detail.DetailActivity.TotalLikesKey";

    private String mUsername;
    private int mTotalLikes;
    private int mTotalPhotos;
    private String mUserPhoto;
    private String mName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mBinding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setup();
    }

    private void setup()
    {
        setupData();
        setupCloseListener();
        setupImageListener();
    }

    private void setupData()
    {
        Intent data = getIntent();
        mUsername = data.getStringExtra(USERNAME_KEY);
        mName = data.getStringExtra(NAME_KEY);
        String photo = data.getStringExtra(PHOTO_KEY);
        mUserPhoto = data.getStringExtra(USER_PHOTO_KEY);
        mTotalLikes = data.getIntExtra(TOTAL_LIKES_KEY, 0);
        mTotalPhotos = data.getIntExtra(TOTAL_PHOTOS_KEY, 0);

        mBinding.authorTextview.setText(mName);
        mBinding.authorNicknameTextview.setText(mUsername);

        RequestOptions optionsImage = new RequestOptions()
                .centerCrop();
        Glide.with(this).load(photo).apply(optionsImage).into(mBinding.imageImageview);

        RequestOptions optionsUser = new RequestOptions()
                .centerCrop();
        Glide.with(this).load(mUserPhoto).apply(optionsUser).into(mBinding.authorImageview);
    }

    private void setupCloseListener()
    {
        mBinding.closeImageview.setOnClickListener(v -> this.finish());
    }

    private void setupImageListener()
    {
        mBinding.authorImageview.setOnClickListener(v ->
        {
            Intent profile = ProfileActivity.getProfileIntent(this, mUsername, mName, mTotalLikes, mTotalPhotos, mUserPhoto);
            startActivity(profile);
        });
    }

    public static Intent getDetailIntent(Context context, String photo, String userPhoto, String username, String name, int likes, int photos)
    {
        Intent detail = new Intent(context, DetailActivity.class);
        detail.putExtra(PHOTO_KEY, photo);
        detail.putExtra(USER_PHOTO_KEY, userPhoto);
        detail.putExtra(USERNAME_KEY, username);
        detail.putExtra(NAME_KEY, name);
        detail.putExtra(TOTAL_LIKES_KEY, likes);
        detail.putExtra(TOTAL_PHOTOS_KEY, photos);
        return detail;
    }
}