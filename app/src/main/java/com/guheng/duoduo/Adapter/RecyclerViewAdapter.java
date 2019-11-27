package com.guheng.duoduo.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bt.mylibrary.TimeLineMarkerView;
import com.guheng.duoduo.Object.RecyclerViewItemEntity;
import com.guheng.duoduo.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter {
    private static final String TAG = "RecyclerViewAdapter";
    private OnItemClickListener mOnItemClickListener;
    private Context mContext;
    private List<RecyclerViewItemEntity> mRecyclerViewItemEntityList;
    private List<Boolean> mIsClickList;//控件是否被点击,默认为false，如果被点击，改变值，控件根据值改变自身颜色
    private int mSelectedPosition; // 选择行的索引

    public RecyclerViewAdapter(Context context, List<RecyclerViewItemEntity> recyclerViewItemEntityList) {
        this.mContext = context;
        this.mIsClickList = new ArrayList<>();
        this.setRecyclerViewItemEntityList(recyclerViewItemEntityList);
        mSelectedPosition = -1;
    }

    public void setRecyclerViewItemEntityList(List<RecyclerViewItemEntity> recyclerViewItemEntityList) {
        this.mRecyclerViewItemEntityList = recyclerViewItemEntityList;
        this.mIsClickList.clear();
        for (int i = 0; i < this.mRecyclerViewItemEntityList.size(); i++) {
            this.mIsClickList.add(false);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.recycler_view_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (mRecyclerViewItemEntityList == null) {
            return;
        }
        RecyclerViewItemEntity entity = this.mRecyclerViewItemEntityList.get(position);
        if (entity == null) {
            return;
        }

        ((RecyclerViewHolder) holder).mTvDate.setText(entity.getYearMonthDayHourMinuteSecondString());
        ((RecyclerViewHolder) holder).mTvSummarized.setText(entity.getSummarized());

        if (this.mIsClickList.get(position)) {
            ((RecyclerViewHolder) holder).itemView.setBackgroundColor(Color.parseColor("#9e9e9e"/*灰色*/));
        } else {
            ((RecyclerViewHolder) holder).itemView.setBackgroundColor(Color.parseColor("#ffffff"/*白色*/));
        }

        final OnItemClickListener onItemClickListener = this.mOnItemClickListener;
        final List<Boolean> isClickList = this.mIsClickList;
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setSelectedPosition(position);

                    for (int i = 0; i < isClickList.size(); i++) {
                        isClickList.set(i, false);
                    }

                    isClickList.set(position, true);

                    notifyDataSetChanged();

                    onItemClickListener.onItemClick(holder.itemView, holder.getLayoutPosition());
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.mRecyclerViewItemEntityList == null ? 0 : this.mRecyclerViewItemEntityList.size();
    }

    protected void setSelectedPosition(int selectedPosition) {
        this.mSelectedPosition = selectedPosition;
    }

    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    public RecyclerViewItemEntity remove(int position) {
        if (position == -1) {
            return null;
        }

        if (this.mRecyclerViewItemEntityList == null) {
            return null;
        }

        RecyclerViewItemEntity entity = this.mRecyclerViewItemEntityList.remove(position);
        if (entity != null) {
            setSelectedPosition(-1);

            for (int i = 0; i < this.mIsClickList.size(); i++) {
                this.mIsClickList.set(i, false);
            }
        }

        notifyDataSetChanged();

        return entity;
    }

    public void clear() {
        if (this.mRecyclerViewItemEntityList == null) {
            return;
        }

        this.mRecyclerViewItemEntityList.clear();

        notifyDataSetChanged();
    }

    /*
     * Holder Class
     */
    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public static final String TAG = "RecyclerViewHolder";
        private TextView mTvDate;
        private TimeLineMarkerView mTvMarker;
        private TextView mTvSummarized;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mTvDate = itemView.findViewById(R.id.tv_date);
            this.mTvMarker = itemView.findViewById(R.id.tv_marker);
            this.mTvSummarized = itemView.findViewById(R.id.tv_summarized);
        }
    }

    /*
     * Interface
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }
}
