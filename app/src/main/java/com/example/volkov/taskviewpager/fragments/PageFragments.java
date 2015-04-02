package com.example.volkov.taskviewpager.fragments;

import java.util.List;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.volkov.taskviewpager.R;
import com.example.volkov.taskviewpager.StatePage;
import com.example.volkov.taskviewpager.adapter.RecyclerViewAdapter;
import com.example.volkov.taskviewpager.global.Constants;
import com.example.volkov.taskviewpager.global.Variables;
import com.example.volkov.taskviewpager.model.ListModel;
import com.example.volkov.taskviewpager.servise.RetroFitServise;
import com.example.volkov.taskviewpager.widget.OnSwipeListener;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PageFragments extends Fragment implements Callback<List<ListModel>> {
    private RecyclerViewAdapter         mAdapter;
    private RecyclerView                mRV;
    private RecyclerView.LayoutManager  mLayoutManager;
    private Activity                    mActivity;
    private int                       mScreenSize, mScreenSizeY;
    private OnSwipeListener                 mSwipeListener = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    public static PageFragments newInstance(StatePage _statePage, OnSwipeListener _lis) {
        PageFragments mPageFragment = new PageFragments();
        Bundle mArguments = new Bundle();
        mArguments.putSerializable(Constants.ARG_STATE_PAGE, _statePage);
        mPageFragment.setArguments(mArguments);
        mPageFragment.setOnSwipeListener(_lis);
        return mPageFragment;
    }

    public void initCreate() {
        final StatePage pageState = (StatePage) getArguments().getSerializable(Constants.ARG_STATE_PAGE);
        getAdapterService().getList(pageState.name().toLowerCase(), this);
    }

    public void setOnSwipeListener(OnSwipeListener listener) {
        mSwipeListener = listener;
    }

    private RetroFitServise getAdapterService() {
        return new RestAdapter.Builder()
                .setEndpoint(Constants.BASE_URL)
                .build().create(RetroFitServise.class);
    }

    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container,
                             Bundle _savedInstanceState) {
        initCreate();
        View mView = _inflater.inflate(R.layout.fragment_general, _container, false);
        mRV = (RecyclerView) mView.findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        mRV.setLayoutManager(mLayoutManager);
        Point mPoint        = new Point();
        mActivity.getWindowManager().getDefaultDisplay().getSize(mPoint);
        mScreenSize         = mPoint.x;
        mScreenSizeY        = mPoint.y;
        mRV.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                Log.w("DRAG EVENT", "ON FRAGMENT");
                switch(event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        return true;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        return true;
                    case DragEvent.ACTION_DRAG_EXITED:
                        return true;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        float x = event.getX();
                        float y = event.getY();
                        checkX(x);
                        checkY(y);
                        return true;
                    case DragEvent.ACTION_DROP:
                        return true;
                    case DragEvent.ACTION_DRAG_ENDED:
                        return true;
                }
                return false;
            }
        });
        return mView;
    }

    private void checkX(float x) {
        if(x > (mScreenSize * 0.8f) && x < mScreenSize) {
            if (mSwipeListener != null)
                mSwipeListener.onSwipe(OnSwipeListener.SWIPE_RIGHT);
        } else if(x < (mScreenSize * 0.2f) && x > 0) {
            if(mSwipeListener != null)
                mSwipeListener.onSwipe(OnSwipeListener.SWIPE_LEFT);
        }
    }

    private void checkY(float y){
        if (y > (mScreenSizeY * 0.7f) && y < mScreenSizeY){
            mRV.smoothScrollBy(0, 200);
        } else if (y < (mScreenSizeY * 0.3f) && y > 0)
            mRV.smoothScrollBy(0, -200);
    }


    @Override
    public void success(List<ListModel> listModels, Response response) {
        mAdapter = new RecyclerViewAdapter(getActivity(), listModels);
        mRV.setAdapter(mAdapter);
    }

    @Override
    public void failure(RetrofitError error) {

    }

}