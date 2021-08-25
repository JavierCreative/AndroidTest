package com.test.androidtest.presentation.profile;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.test.androidtest.R;
import com.test.androidtest.databinding.ActivityProfileBinding;
import com.test.androidtest.domain.models.dao.TestViewModel;
import com.test.androidtest.domain.models.remote.Photo;
import com.test.androidtest.domain.presenters.UserPhotosPresenter;
import com.test.androidtest.domain.presenters.contracts.UserPhotosContract;
import com.test.androidtest.presentation.adapters.PhotosRecyclerAdapter;
import com.test.androidtest.presentation.adapters.interfaces.OnItemInteractionListener;
import com.test.androidtest.presentation.adapters.interfaces.PaginationListener;
import com.test.androidtest.presentation.base.BaseActivity;
import com.test.androidtest.presentation.detail.DetailActivity;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends BaseActivity<ActivityProfileBinding> implements UserPhotosContract.UserPhotosInterfaceView, OnItemInteractionListener
{
    private static final String USERNAME_KEY = "com.test.androidtest.presentation.profile.ProfileActivity.UsernameKey";
    private static final String NAME_KEY = "com.test.androidtest.presentation.profile.ProfileActivity.NameKey";
    private static final String TOTAL_LIKES_KEY = "com.test.androidtest.presentation.profile.ProfileActivity.TotalLikesPhotos";
    private static final String TOTAL_PHOTOS_KEY = "com.test.androidtest.presentation.profile.ProfileActivity.TotalPhotosKey";
    private static final String PHOTOS_KEY = "com.test.androidtest.presentation.profile.ProfileActivity.PhotoKey";

    private UserPhotosPresenter<ProfileActivity> mPresenter;
    private PhotosRecyclerAdapter mAdapter;
    private int mCurrentPage = 1;
    private boolean mIsLastPage = false;
    private int mItemsPerPage = 10;
    private boolean nIsLoading = false;
    private List<Photo> mDataset;
    private String mUsername = "";
    private TestViewModel mViewmodel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setup();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tearDown();
    }

    private void setup()
    {
        mPresenter = new UserPhotosPresenter<>();
        mPresenter.attach(this);

        mViewmodel = ViewModelProviders.of(this).get(TestViewModel.class);

        mDataset = new ArrayList<>();
        mAdapter = new PhotosRecyclerAdapter(mDataset, this, this);

        setupData();
        setupRecyclerView();
        getItems();
    }

    private void tearDown()
    {
        mPresenter = null;
        mDataset = null;
        mAdapter = null;
    }

    private void setupRecyclerView()
    {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mBinding.photosRecyclerview.setLayoutManager(manager);
        mBinding.photosRecyclerview.setHasFixedSize(false);
        mBinding.photosRecyclerview.setAdapter(mAdapter);
        mBinding.photosRecyclerview.addOnScrollListener(new PaginationListener(manager)
        {
            @Override
            protected void loadMoreItems()
            {
                nIsLoading = true;
                mCurrentPage++;
                getItems();
            }
            @Override
            public boolean isLastPage()
            {
                return mIsLastPage;
            }
            @Override
            public boolean isLoading()
            {
                return nIsLoading;
            }
        });
    }

    private void getItems()
    {
        mPresenter.getUserPhotos(mUsername, mCurrentPage, mItemsPerPage);
    }

    private void setupData()
    {
        Intent data = getIntent();
        mUsername = data.getStringExtra(USERNAME_KEY);
        String name = data.getStringExtra(NAME_KEY);
        String photo = data.getStringExtra(PHOTOS_KEY);
        int likes = data.getIntExtra(TOTAL_LIKES_KEY, 0);
        int photos = data.getIntExtra(TOTAL_PHOTOS_KEY, 0);

        mBinding.authorTextview.setText(name);
        mBinding.totalLikesTextview.setText(String.valueOf(likes));
        mBinding.totalPhotosTextview.setText(String.valueOf(photos));

        RequestOptions optionsUser = new RequestOptions()
                .centerCrop();
        Glide.with(this).load(photo).apply(optionsUser).into(mBinding.authorImageview);
    }

    @Override
    public void onSuccessGetPhotos(List<Photo> photos)
    {
        nIsLoading = false;
        if (mCurrentPage >= mItemsPerPage)
        {
            mIsLastPage = true;
        }
        mAdapter.setDataset(photos);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showProgress()
    {
        mBinding.progressCircularprogress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress()
    {
        mBinding.progressCircularprogress.setVisibility(View.GONE);
    }

    @Override
    public void onError(String... args)
    {
        AlertDialog alert = new AlertDialog.Builder(this)
                .setTitle(R.string.error_string)
                .setMessage(args[0])
                .setPositiveButton(R.string.accept_string, ((dialog, which) -> dialog.dismiss()))
                .create();
        alert.show();
    }

    public static Intent getProfileIntent(Context context, String username, String name, int likes, int photos, String photo)
    {
        Intent profile = new Intent(context, ProfileActivity.class);
        profile.putExtra(USERNAME_KEY, username);
        profile.putExtra(NAME_KEY, name);
        profile.putExtra(TOTAL_LIKES_KEY, likes);
        profile.putExtra(TOTAL_PHOTOS_KEY, photos);
        profile.putExtra(PHOTOS_KEY, photo);
        return profile;
    }

    @Override
    public void onSavePhoto(Photo item, int position, boolean checked)
    {
        if (checked)
        {
            com.test.androidtest.domain.models.local.Photo photo = new com.test.androidtest.domain.models.local.Photo();
            photo.setLikes(item.getLikes());
            photo.setDescription(item.getDescription());
            photo.setLikedByUser(true);
            photo.setSaved(true);
            photo.setUser(new Gson().toJson(item.getUser()));
            photo.setUrls(new Gson().toJson(item.getUrls()));

            Integer id = (int) mViewmodel.insertPhoto(photo);
            item.setId(id);
            mAdapter.setItem(item, position);
            mAdapter.notifyItemChanged(position);
        }
        else
        {
            com.test.androidtest.domain.models.local.Photo photo = new com.test.androidtest.domain.models.local.Photo();
            photo.setLikes(item.getLikes());
            photo.setDescription(item.getDescription());
            photo.setLikedByUser(true);
            photo.setSaved(true);
            photo.setUser(new Gson().toJson(item.getUser()));
            photo.setUrls(new Gson().toJson(item.getUrls()));
            photo.setPhotoId(item.getId());
            mViewmodel.deletePhoto(photo);
        }
    }

    @Override
    public void onItemClicked(Photo item)
    {
        Intent detail = DetailActivity.getDetailIntent(this, item.getUrls().getRegular(), item.getUser().getProfileImage().getLarge(), item.getUser().getUsername(), item.getUser().getName(), item.getUser().getTotalLikes(), item.getUser().getTotalPhotos());
        startActivity(detail);
    }
}