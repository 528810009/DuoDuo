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
import com.guheng.duoduo.Object.EntityItem;
import com.guheng.duoduo.Object.EntityList;
import com.guheng.duoduo.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter {
    private static final String TAG = "RecyclerViewAdapter";
    private OnItemClickListener mOnItemClickListener;
    private Context mContext;
    private EntityList mEntityList;
    private int mSelectedPosition; // 选择行的索引

    public RecyclerViewAdapter(Context context, EntityList entityList) {
        this.mContext = context;
        this.setRecyclerViewListEntity(entityList);
        this.mSelectedPosition = -1;
    }

    public void setRecyclerViewListEntity(EntityList entityList) {
        this.mEntityList = entityList;
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
        if (this.mEntityList.isEmpty()) {
            return;
        }

        final int pos = holder.getAdapterPosition();

        EntityItem entity = this.mEntityList.get(pos);
        if (entity == null) {
            return;
        }

        ((RecyclerViewHolder) holder).mTvDate.setText(entity.getDate());
        ((RecyclerViewHolder) holder).mTvSummarized.setText(entity.getSummarized());

        if (this.mEntityList.getIsClickList().get(pos)) {
            ((RecyclerViewHolder) holder).itemView.setBackgroundColor(Color.parseColor("#9e9e9e"/*灰色*/));
        } else {
            ((RecyclerViewHolder) holder).itemView.setBackgroundColor(Color.parseColor("#ffffff"/*白色*/));
        }

        final OnItemClickListener onItemClickListener = this.mOnItemClickListener;
        final List<Boolean> isClickList = this.mEntityList.getIsClickList();
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setSelectedPosition(pos);

                    for (int i = 0; i < isClickList.size(); i++) {
                        isClickList.set(i, false);
                    }

                    isClickList.set(pos, true);

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
        return this.mEntityList.size();
    }

    public void setSelectedPosition(int selectedPosition) {
        this.mSelectedPosition = selectedPosition;
    }

    public int getSelectedPosition() {
        return mSelectedPosition;
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
