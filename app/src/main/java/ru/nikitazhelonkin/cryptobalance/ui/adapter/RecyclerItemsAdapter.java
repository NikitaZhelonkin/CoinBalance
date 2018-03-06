package ru.nikitazhelonkin.cryptobalance.ui.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;

public class RecyclerItemsAdapter extends RecyclerView.Adapter<RecyclerItemsAdapter.AbsViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(IListItem iListItem);
    }

    private List<IListItem> mData;

    private OnItemClickListener mOnItemClickListener;

    public void setData(List<IListItem> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getType();
    }

    @Override
    public AbsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        IListItem item = getItemOfType(viewType);
        if (item != null) {
            return item.onCreateViewHolder(inflater, parent);
        } else {
            throw new IllegalArgumentException("Unsupported view type " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(AbsViewHolder holder, int position) {
        IListItem iListItem = mData.get(position);
        holder.setOnItemClickListener(mOnItemClickListener);
        holder.bind(iListItem);
    }


    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }


    private IListItem getItemOfType(int type) {
        if (mData == null) {
            return null;
        }
        for (IListItem item : mData) {
            if (item.getType() == type) {
                return item;
            }
        }
        return null;
    }

    public abstract static class AbsViewHolder extends RecyclerView.ViewHolder {

        private OnItemClickListener mOnItemClickListener;

        public AbsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            mOnItemClickListener = onItemClickListener;
        }

        public void bind(IListItem iListItem){
            if(iListItem.isClickable()){
                itemView.setOnClickListener(v -> onItemClick(iListItem));
            }
        }

        protected void onItemClick(IListItem iListItem) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(iListItem);
            }
        }
    }

    public interface IListItem {

        int getId();

        int getType();

        boolean isClickable();

        AbsViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent);
    }


}