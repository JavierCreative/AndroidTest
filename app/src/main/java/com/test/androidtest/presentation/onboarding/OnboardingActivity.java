package com.test.androidtest.presentation.onboarding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayoutMediator;
import com.test.androidtest.databinding.ActivityOnboardingBinding;
import com.test.androidtest.presentation.adapters.OnboardingFragmentAdapter;
import com.test.androidtest.presentation.base.BaseActivity;
import com.test.androidtest.presentation.main.MainActivity;

public class OnboardingActivity extends BaseActivity<ActivityOnboardingBinding>
{
    private OnboardingFragmentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mBinding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setup();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        tearDown();
    }

    private void setup()
    {
        setupPager();
        setupGetStartedListener();
    }

    private void setupPager()
    {
        mAdapter = new OnboardingFragmentAdapter(this);

        mAdapter.addFragment(createFragment(0));
        mAdapter.addFragment(createFragment(1));
        mAdapter.addFragment(createFragment(2));

        mBinding.slidesViewpager.setAdapter(mAdapter);

        mBinding.slidesViewpager.setPageTransformer((page, position) ->
        {
            page.setTranslationX(page.getWidth() * -position);

            if(position <= -1.0F || position >= 1.0F)
            {
                page.setAlpha(0.0F);
            }
            else if(position == 0.0F )
            {
                page.setAlpha(1.0F);
            }
            else {
                page.setAlpha(1.0F - Math.abs(position));
            }
        });

        TabLayoutMediator mediator = new TabLayoutMediator(mBinding.tabsTablayout, mBinding.slidesViewpager, (tab, position) ->
        {
        });
        mediator.attach();
    }

    private OnboardingGenericFragment createFragment(int index)
    {
        OnboardingGenericFragment fragment = new OnboardingGenericFragment();
        fragment.setItem(index);
        return fragment;
    }

    private void setupGetStartedListener()
    {
        mBinding.getStartedButton.setOnClickListener(v ->
        {
            Intent main = new Intent(this, MainActivity.class);
            startActivity(main);
            this.finish();
        });
    }

    private void tearDown()
    {
        mBinding = null;
        mAdapter = null;
    }
}