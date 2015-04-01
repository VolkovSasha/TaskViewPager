package com.example.volkov.taskviewpager.fragments;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.volkov.taskviewpager.R;
import com.example.volkov.taskviewpager.StatePage;
import com.example.volkov.taskviewpager.adapter.RecyclerViewAdapter;
import com.example.volkov.taskviewpager.global.Constants;
import com.example.volkov.taskviewpager.model.ListModel;
import com.example.volkov.taskviewpager.servise.RetroFitServise;
import com.example.volkov.taskviewpager.widget.OnScrollListener;
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
    private OnSwipeListener             listener;

    private void setListener(OnSwipeListener _lis) {
        listener = _lis;
    }

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
        mPageFragment.setListener(_lis);
        return mPageFragment;
    }

    public void initCreate() {
        final StatePage pageState = (StatePage) getArguments().getSerializable(Constants.ARG_STATE_PAGE);
        getAdapterService().getList(pageState.name().toLowerCase(), this);
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
        return mView;
    }

    @Override
    public void success(List<ListModel> listModels, Response response) {
        mAdapter = new RecyclerViewAdapter(getActivity(), listModels);
        mAdapter.setOnSwipeListener(listener);
        mAdapter.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScroll(int scrollTo) {
                switch (scrollTo) {
                    case OnScrollListener.SCROLL_UP:
                        Log.w("SCROLL", "UP");
                        mRV.smoothScrollBy(0, - 100);
                        break;
                    case OnScrollListener.SCROLL_DOWN:
                        Log.w("SCROLL", "DOWN");
                        mRV.smoothScrollBy(0, 100);
                }
            }
        });
        mRV.setAdapter(mAdapter);
    }

    @Override
    public void failure(RetrofitError error) {

    }

}