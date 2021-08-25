package com.test.androidtest.presentation.main;

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
import com.test.androidtest.databinding.FragmentFavoritesBinding;
import com.test.androidtest.domain.models.dao.TestViewModel;
import com.test.androidtest.domain.models.remote.Photo;
import com.test.androidtest.domain.models.remote.Url;
import com.test.androidtest.domain.models.remote.User;
import com.test.androidtest.presentation.adapters.PhotosRecyclerAdapter;
import com.test.androidtest.presentation.adapters.interfaces.OnItemInteractionListener;
import com.test.androidtest.presentation.adapters.interfaces.OnProfileInteraction;
import com.test.androidtest.presentation.adapters.interfaces.PaginationListener;
import com.test.androidtest.presentation.base.BaseFragment;
import com.test.androidtest.presentation.detail.DetailActivity;
import com.test.androidtest.presentation.profile.ProfileActivity;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends BaseFragment<FragmentFavoritesBinding> implements OnProfileInteraction, OnItemInteractionListener
{
    private PhotosRecyclerAdapter mAdapter;
    private TestViewModel mViewModel;
    private List<Photo> mDataset;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mBinding = FragmentFavoritesBinding.inflate(LayoutInflater.from(mContext), container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        setup();
    }

    private void setup()
    {
        mViewModel = ViewModelProviders.of(this).get(TestViewModel.class);
        mDataset = getDataset();
        mAdapter = new PhotosRecyclerAdapter(mDataset, mContext, this);
        mAdapter.addOnProfileInteractionListener(this);
        setupRecyclerView();
    }

    private void setupRecyclerView()
    {
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        mBinding.photosRecyclerview.setLayoutManager(manager);
        mBinding.photosRecyclerview.setHasFixedSize(false);
        mBinding.photosRecyclerview.setAdapter(mAdapter);
    }

    private List<Photo> getDataset()
    {
        List<Photo> dataset = new ArrayList<>();
        List<com.test.androidtest.domain.models.local.Photo> photos = mViewModel.getPhotos();

        for (com.test.androidtest.domain.models.local.Photo photo : photos)
        {
            Photo remote = new Photo();
            remote.setId(photo.getPhotoId());
            remote.setSaved(photo.getSaved());
            remote.setDescription(photo.getDescription());
            remote.setLikedByUser(photo.getLikedByUser());
            remote.setLikes(photo.getLikes());
            remote.setUrls(new Gson().fromJson(photo.getUrls(), Url.class));
            remote.setUser(new Gson().fromJson(photo.getUser(), User.class));
            dataset.add(remote);
        }
        return dataset;
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

            Integer id = (int) mViewModel.insertPhoto(photo);
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
            mViewModel.deletePhoto(photo);
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