package ru.nikitazhelonkin.cryptobalance.presentation.main;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.nikitazhelonkin.cryptobalance.R;
import ru.nikitazhelonkin.cryptobalance.data.entity.WalletViewModel;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    public MainAdapter() {

    }

    private List<WalletViewModel> mData;

    public void setData(List<WalletViewModel> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.list_item_wallet, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.coin_icon)
        ImageView mImageView;
        @BindView(R.id.balance)
        TextView mBalanceView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(WalletViewModel walletViewModel) {
            mBalanceView.setText(walletViewModel.getBalance());
            mImageView.setImageResource(walletViewModel.getCoinIconResId());
        }
    }
}
