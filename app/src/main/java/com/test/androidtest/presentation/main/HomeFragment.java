package com.test.androidtest.presentation.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.test.androidtest.R;
import com.test.androidtest.databinding.FragmentHomeBinding;
import com.test.androidtest.domain.models.dao.TestViewModel;
import com.test.androidtest.domain.models.remote.Photo;
import com.test.androidtest.domain.presenters.PhotosPresenter;
import com.test.androidtest.domain.presenters.contracts.PhotosContract;
import com.test.androidtest.presentation.adapters.PhotosRecyclerAdapter;
import com.test.androidtest.presentation.adapters.interfaces.OnItemInteractionListener;
import com.test.androidtest.presentation.adapters.interfaces.OnProfileInteraction;
import com.test.androidtest.presentation.adapters.interfaces.PaginationListener;
import com.test.androidtest.presentation.base.BaseFragment;
import com.test.androidtest.presentation.detail.DetailActivity;
import com.test.androidtest.presentation.profile.ProfileActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment<FragmentHomeBinding> implements PhotosContract.PhotosInterfaceView, OnItemInteractionListener, OnProfileInteraction
{
    private PhotosPresenter<HomeFragment> mPhotosPresenter;
    private PhotosRecyclerAdapter mAdapter;
    private int mCurrentPage = 1;
    private boolean mIsLastPage = false;
    private int mItemsPerPage = 10;
    private boolean nIsLoading = false;
    private List<Photo> mDataset;
    private TestViewModel mViewmodel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mBinding = FragmentHomeBinding.inflate(LayoutInflater.from(mContext), container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        setup();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        tearDown();
    }

    private void setup()
    {
        mPhotosPresenter = new PhotosPresenter<>();
        mPhotosPresenter.attach(this);
        mViewmodel = ViewModelProviders.of(this).get(TestViewModel.class);
        mDataset = new ArrayList<>();
        mAdapter = new PhotosRecyclerAdapter(mDataset, mContext, this);
        mAdapter.addOnProfileInteractionListener(this);
        setupRecyclerView();
        getItems();
    }

    private void tearDown()
    {
        mPhotosPresenter = null;
        mDataset = null;
        mAdapter = null;
    }

    private void setupRecyclerView()
    {
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
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
        mPhotosPresenter.getPhotos(mCurrentPage, mItemsPerPage);
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
        AlertDialog alert = new AlertDialog.Builder(mContext)
                .setTitle(R.string.error_string)
                .setMessage(args[0])
                .setPositiveButton(R.string.accept_string, ((dialog, which) -> dialog.dismiss()))
                .create();
        alert.show();
    }

    @Override
    public void onSavePhoto(Photo item, int position, boolean checked)
    {
        if (checked)
        {
            com.test.androidtest.domain.models.local.Photo photo = new com.test.androidtest.domain.models.local.Photo();
            photo.setLikes(item.getLikes());
            photo.setDescription(item.getDescription());
            photo.setLikedByUser(item.isLikedByUser());
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
            photo.setLikedByUser(item.isLikedByUser());
            photo.setSaved(item.isSaved());
            photo.setUser(new Gson().toJson(item.getUser()));
            photo.setUrls(new Gson().toJson(item.getUrls()));
            photo.setPhotoId(item.getId());
            mViewmodel.deletePhoto(photo);
        }
    }

    @Override
    public void onItemClicked(Photo item)
    {
        Intent detail = DetailActivity.getDetailIntent(mContext, item.getUrls().getRegular(), item.getUser().getProfileImage().getLarge(), item.getUser().getUsername(), item.getUser().getName(), item.getUser().getTotalLikes(), item.getUser().getTotalPhotos());
        startActivity(detail);
    }

    @Override
    public void showProfile(Photo photo)
    {
        Intent profile = ProfileActivity.getProfileIntent(mContext, photo.getUser().getUsername(), photo.getUser().getName(), photo.getUser().getTotalLikes(), photo.getUser().getTotalPhotos(), photo.getUser().getProfileImage().getLarge());
        startActivity(profile);
    }
}