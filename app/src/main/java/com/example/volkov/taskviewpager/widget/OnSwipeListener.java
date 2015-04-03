package com.example.volkov.taskviewpager.widget;

public interface OnSwipeListener {

    public final static int SWIPE_RIGHT     = 1;
    public final static int SWIPE_LEFT      = 2;

    public void onSwipe(int swipeTo);
}
