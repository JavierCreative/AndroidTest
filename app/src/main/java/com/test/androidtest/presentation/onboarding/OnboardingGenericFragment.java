package com.test.androidtest.presentation.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.test.androidtest.R;
import com.test.androidtest.databinding.FragmentOnboardingGenericBinding;
import com.test.androidtest.presentation.base.BaseFragment;

public class OnboardingGenericFragment extends BaseFragment<FragmentOnboardingGenericBinding>
{
    private int mItem;
    private final int[] IMAGES = {R.drawable.plant_1, R.drawable.plant_2, R.drawable.plant_3};
    private final int[] TITLES = {R.string.title_1_onboarding_string, R.string.title_2_onboarding_string, R.string.title_3_onboarding_string};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mBinding = FragmentOnboardingGenericBinding.inflate(LayoutInflater.from(mContext), container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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
        mBinding.titleTextview.setText(TITLES[mItem]);
        mBinding.imageImageview.setImageResource(IMAGES[mItem]);
        if (mItem == 2)
        {
            mBinding.imageImageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

    private void tearDown()
    {
        mContext = null;
        mBinding = null;
    }

    public void setItem(int item)
    {
        this.mItem = item;
    }
}
