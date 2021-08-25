package com.test.androidtest.presentation.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.test.androidtest.presentation.onboarding.OnboardingGenericFragment;

import java.util.ArrayList;
import java.util.List;

public class OnboardingFragmentAdapter extends FragmentStateAdapter
{
    private List<OnboardingGenericFragment> mFragments;

    public OnboardingFragmentAdapter(@NonNull FragmentActivity fragmentActivity)
    {
        super(fragmentActivity);
        mFragments = new ArrayList<>();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position)
    {
        return mFragments.get(position);
    }

    @Override
    public int getItemCount()
    {
        return mFragments.size();
    }

    public void addFragment(OnboardingGenericFragment fragment)
    {
        mFragments.add(fragment);
    }
}
