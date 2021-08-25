package com.test.androidtest.presentation.base;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class BaseFragment<V> extends Fragment
{
    protected V mBinding;
    protected Context mContext;
    protected FragmentManager mFragmentManager;

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mFragmentManager = getParentFragmentManager();
    }
}
