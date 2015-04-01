package com.example.volkov.taskviewpager;

import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.example.volkov.taskviewpager.adapter.MyFragmentPagerAdapter;
import com.example.volkov.taskviewpager.global.Constants;
import com.example.volkov.taskviewpager.widget.OnSwipeListener;


public class MainActivity extends ActionBarActivity implements ViewPager.OnPageChangeListener {

    private ViewPager       mPager;
    private PagerAdapter    mPagerAdapter;
    private Boolean         swipe = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPager();
    }

    private void initPager() {
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), new OnSwipeListener() {
            @Override
            public void onSwipe(int swipeTo) {
                switch (swipeTo) {
                    case OnSwipeListener.SWIPE_RIGHT:
                        if(swipe) {
                            if (mPager.getCurrentItem() < mPagerAdapter.getCount())
                                mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                            handlerSwipe();
                        }
                        break;
                    case OnSwipeListener.SWIPE_LEFT:
                        if (swipe) {
                            if (mPager.getCurrentItem() > 0)
                                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
                            handlerSwipe();
                        }
                }
            }
        });
        mPager.setOffscreenPageLimit(Constants.PAGE_COUNT);
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(this);
    }

    private void handlerSwipe(){
        swipe = false;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipe = true;
            }
        }, 1000);
    }

    private void titleChange() {
        setTitle(getResources().getStringArray(R.array.titles)[mPager.getCurrentItem()]);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        titleChange();
    }
}