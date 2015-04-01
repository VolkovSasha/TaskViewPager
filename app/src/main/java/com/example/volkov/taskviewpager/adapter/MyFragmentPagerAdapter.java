package com.example.volkov.taskviewpager.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.volkov.taskviewpager.StatePage;
import com.example.volkov.taskviewpager.global.Constants;
import com.example.volkov.taskviewpager.fragments.PageFragments;
import com.example.volkov.taskviewpager.widget.OnSwipeListener;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private OnSwipeListener mListener;

    public MyFragmentPagerAdapter(FragmentManager _fm, OnSwipeListener _lis) {
        super(_fm);
        mListener = _lis;
    }

    @Override
    public Fragment getItem(int _position) {
        return PageFragments.newInstance(StatePage.values()[_position], mListener);
    }

    @Override
    public int getCount() {
        return Constants.PAGE_COUNT;
    }

}