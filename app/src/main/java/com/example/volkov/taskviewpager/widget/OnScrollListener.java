package com.example.volkov.taskviewpager.widget;

/**
 * Created by Pavilion on 01.04.2015.
 */
public interface OnScrollListener {

    public final static int SCROLL_UP       = 1;
    public final static int SCROLL_DOWN     = 2;

    public void onScroll(int scrollTo);
}
