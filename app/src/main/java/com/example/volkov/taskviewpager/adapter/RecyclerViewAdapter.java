package com.example.volkov.taskviewpager.adapter;

import android.content.Context;
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
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private List<ListModel>                 mListModel;
    private Context                         mContext;

    public RecyclerViewAdapter(Context _context, List<ListModel> _listModel){
        mListModel          = _listModel;
        mContext            = _context;
    }

    public void addItem(int position, ListModel model) {
        mListModel.add(position, model);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup _viewGroup, int _position) {
        View view = LayoutInflater.from(_viewGroup.getContext()).inflate(R.layout.item_list_model, _viewGroup, false);
        return new ViewHolder(view, mListModel.get(_position));
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
                    switch (event.getAction()) {
                        case DragEvent.ACTION_DRAG_STARTED:
                            return true;
                        case DragEvent.ACTION_DRAG_ENTERED:
                            return true;
                        case DragEvent.ACTION_DRAG_EXITED:
                            return true;
                        case DragEvent.ACTION_DROP:
                            ListModel model = Variables.DRAG_OBJECT;
                            Log.w(RecyclerViewAdapter.class.getName(), "DRAG END: " + model.getTitle());
                            addItem(getPosition(), model);
                            Variables.DRAG_OBJECT = null;
                            return true;
                        case DragEvent.ACTION_DRAG_ENDED:
                            return true;
                    }
                    return false;
                }
            });

        }
    }
}