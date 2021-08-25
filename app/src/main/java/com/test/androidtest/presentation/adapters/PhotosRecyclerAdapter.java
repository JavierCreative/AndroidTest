package com.test.androidtest.presentation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.test.androidtest.databinding.ItemPhotoFeedBinding;
import com.test.androidtest.domain.models.remote.Photo;
import com.test.androidtest.presentation.adapters.interfaces.OnItemInteractionListener;
import com.test.androidtest.presentation.adapters.interfaces.OnProfileInteraction;

import java.util.List;

public class PhotosRecyclerAdapter extends RecyclerView.Adapter<PhotosRecyclerAdapter.ViewHolder>
{
    private List<Photo> mDataset;
    private Context mContext;
    private OnItemInteractionListener mListener;
    private OnProfileInteraction mProfileListener;

    public PhotosRecyclerAdapter(List<Photo> dataset, Context context, OnItemInteractionListener listener)
    {
        this.mContext = context;
        this.mDataset = dataset;
        this.mListener = listener;
    }

    public void setDataset(List<Photo> data)
    {
        mDataset.addAll(data);
    }

    public void setItem(Photo data, int position)
    {
        mDataset.set(position, data);
    }

    public void addOnProfileInteractionListener(OnProfileInteraction listener)
    {
        this.mProfileListener = listener;
    }

    @NonNull
    @Override
    public PhotosRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        ItemPhotoFeedBinding binding = ItemPhotoFeedBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotosRecyclerAdapter.ViewHolder holder, int position)
    {
        holder.bind(position);
    }

    @Override
    public int getItemCount()
    {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private ItemPhotoFeedBinding mBinding;

        public ViewHolder(@NonNull ItemPhotoFeedBinding itemView)
        {
            super(itemView.getRoot());
            mBinding = itemView;
        }

        private void bind(int position)
        {
            mBinding.likeCheckbox.setOnCheckedChangeListener(null);
            mBinding.favoriteCheckbox.setOnCheckedChangeListener(null);
            Photo item = mDataset.get(position);
            mBinding.likeCheckbox.setChecked(item.isLikedByUser());
            mBinding.authorTextview.setText(item.getUser().getName());
            mBinding.authorNicknameTextview.setText("@ "+item.getUser().getUsername());
            mBinding.favoriteCheckbox.setChecked(item.isSaved());

            mBinding.likeCheckbox.setText(String.valueOf(item.getLikes()));

            RequestOptions options = new RequestOptions()
                    .centerCrop();
            Glide.with(mContext).load(item.getUrls().getSmall()).apply(options).into(mBinding.imageImageview);

            RequestOptions optionsUser = new RequestOptions()
                    .centerCrop();
            Glide.with(mContext).load(item.getUser().getProfileImage().getLarge()).apply(optionsUser).into(mBinding.authorImageview);

            mBinding.likeCheckbox.setOnCheckedChangeListener((buttonView, isChecked) ->
            {
                item.setLikedByUser(isChecked);
                int likes;
                if (isChecked)
                {
                    likes = item.getLikes() + 1;
                }
                else
                {
                    likes = item.getLikes() - 1;
                }
                item.setLikes(likes);
                mBinding.likeCheckbox.setText(String.valueOf(item.getLikes()));
                mDataset.set(position, item);
            });

            mBinding.favoriteCheckbox.setOnCheckedChangeListener(((buttonView, isChecked) ->
            {
                item.setSaved(isChecked);
                mListener.onSavePhoto(item, position, isChecked);
                mDataset.set(position, item);
            }));

            mBinding.containerCardview.setOnClickListener(v -> mListener.onItemClicked(item));

            if (mProfileListener != null)
            {
                mBinding.authorTextview.setOnClickListener(v -> mProfileListener.showProfile(item));
                mBinding.authorImageview.setOnClickListener(v -> mProfileListener.showProfile(item));
            }
        }
    }
}
