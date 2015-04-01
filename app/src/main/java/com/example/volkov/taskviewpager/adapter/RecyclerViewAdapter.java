package com.example.volkov.taskviewpager.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.volkov.taskviewpager.R;
import com.example.volkov.taskviewpager.global.Constants;
import com.example.volkov.taskviewpager.global.Variables;
import com.example.volkov.taskviewpager.model.ListModel;
import com.example.volkov.taskviewpager.widget.OnScrollListener;
import com.example.volkov.taskviewpager.widget.OnSwipeListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private List<ListModel>                 mListModel;
    private Context                         mContext;
    private final int                       mScreenSize, mScreenSizeY;
    private OnSwipeListener                 mSwipeListener = null;
    private OnScrollListener                mScrollListener = null;

    public RecyclerViewAdapter(Context _context, List<ListModel> _listModel){
        mListModel          = _listModel;
        mContext            = _context;
        Point mPoint        = new Point();
        ((Activity)mContext).getWindowManager().getDefaultDisplay().getSize(mPoint);
        mScreenSize         = mPoint.x;
        mScreenSizeY        = mPoint.y;
    }

    public void addItem(int position, ListModel model) {
        mListModel.add(position, model);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup _viewGroup, int i) {
        View view = LayoutInflater.from(_viewGroup.getContext()).inflate(R.layout.item_list_model, _viewGroup, false);
        return new ViewHolder(view, mListModel.get(i));
    }

    @Override
    public void onBindViewHolder(ViewHolder _viewHolder, int _position) {
        ListModel _model = mListModel.get(_position);
        _viewHolder.tvOne.setText(_model.getTitle());
        _viewHolder.tvTwo.setText(_model.getSubtitle());
        Picasso.with(mContext)
                .load(Constants.BASE_URL + _model.getPicture())
                .into(_viewHolder.ivImage);
    }

    @Override
    public int getItemCount() {
        return mListModel.size();
    }

    public void setOnSwipeListener(OnSwipeListener listener) {
        mSwipeListener = listener;
    }

    public void setOnScrollListener(OnScrollListener listener){
        mScrollListener = listener;
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
            if (mScrollListener != null)
                mScrollListener.onScroll(OnScrollListener.SCROLL_DOWN);
        } else if (y < (mScreenSizeY * 0.3f) && y > 0)
            if (mScrollListener != null)
                mScrollListener.onScroll(OnScrollListener.SCROLL_UP);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivImage;
        public TextView tvOne, tvTwo;

        public ViewHolder(final View itemView, final ListModel model) {
            super(itemView);
            ivImage     = (ImageView) itemView.findViewById(R.id.cv_image);
            tvOne       = (TextView) itemView.findViewById(R.id.cv_tv_one);
            tvTwo       = (TextView) itemView.findViewById(R.id.cv_tv_two);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Variables.DRAG_OBJECT = mListModel.get(getPosition());
                    Log.w(RecyclerViewAdapter.class.getName(), "DRAG START: " + mListModel.get(getPosition()).getTitle());
                    mListModel.remove(getPosition());
                    notifyDataSetChanged();
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(((ViewGroup)v).getChildAt(0));
                    v.startDrag(null, shadowBuilder, null, 0);
                    return false;
                }
            });

            itemView.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    float x = event.getX();
                    float y = event.getY() + v.getTop();
                    Log.i("DRAG", "y="+y);
                    if(Variables.DRAG_OBJECT != null) {
                        checkX(x);
                        checkY(y);
                    }
                    switch (event.getAction()) {
                        case DragEvent.ACTION_DRAG_STARTED:
                        case DragEvent.ACTION_DRAG_ENTERED:
                        case DragEvent.ACTION_DRAG_EXITED:

                            break;
                        case DragEvent.ACTION_DROP:
                            ListModel model = Variables.DRAG_OBJECT;
                            Log.w(RecyclerViewAdapter.class.getName(), "DRAG END: " + model.getTitle());
                            addItem(getPosition(), model);
                            Variables.DRAG_OBJECT = null;

                            break;
                        case DragEvent.ACTION_DRAG_ENDED:
                        default:
                            break;
                    }
                    return true;
                }
            });

        }
    }
}