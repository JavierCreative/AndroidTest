package com.test.androidtest.presentation.adapters.interfaces;

import com.test.androidtest.domain.models.remote.Photo;

public interface OnItemInteractionListener
{
    void onSavePhoto(Photo item, int position, boolean checked);
    void onItemClicked(Photo item);
}
