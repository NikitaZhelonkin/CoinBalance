package ru.nikitazhelonkin.coinbalance.presentation.main;


import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Currency;
import java.util.Locale;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.nikitazhelonkin.coinbalance.R;
import ru.nikitazhelonkin.coinbalance.data.entity.Coin;
import ru.nikitazhelonkin.coinbalance.data.entity.MainViewModel;
import ru.nikitazhelonkin.coinbalance.data.entity.Wallet;
import ru.nikitazhelonkin.coinbalance.ui.text.Spanner;
import ru.nikitazhelonkin.coinbalance.ui.widget.MyPopupMenu;
import ru.nikitazhelonkin.coinbalance.ui.widget.itemtouchhelper.ItemTouchHelperAdapter;
import ru.nikitazhelonkin.coinbalance.ui.widget.itemtouchhelper.ItemTouchHelperViewHolder;
import ru.nikitazhelonkin.coinbalance.utils.AppNumberFormatter;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> implements
        ItemTouchHelperAdapter {

    private MainViewModel mData;


    public interface Callback {
        void onMenuItemClick(Wallet wallet, int itemId);

        void onItemClick(Wallet wallet);

        void onStartDragging();

        void onStopDragging();
    }

    private Callback mCallback;

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public void setData(MainViewModel data) {
        MainViewModel oldData = mData;
        mData = data;
        DiffUtil.calculateDiff(new MainDiffCallback(oldData, data)).dispatchUpdatesTo(this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.list_item_wallet_new, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mData.getWallet(position));
    }


    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        mData.swapWallets(fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.getWalletCount() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        @BindView(R.id.coin_icon)
        ImageView imageView;
        @BindView(R.id.currency_balance)
        TextView currencyBalance;
        @BindView(R.id.balance)
        TextView balanceView;
        @BindView(R.id.wallet_name)
        TextView walletName;
        @BindView(R.id.status_indicator)
        View statusIndicator;
        @BindColor(R.color.colorTextSecondary)
        int mTextColorSecondary;
        @BindColor(R.color.color_wallet_error)
        int mWalletErrorColor;
        @BindColor(R.color.color_wallet_pending)
        int mWalletPendingColor;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(Wallet wallet) {
            Coin coin = mData.getCoin(wallet.getCoinTicker());
            Currency currency = Currency.getInstance(mData.getCurrency());
            boolean statusOk = wallet.getStatus() == Wallet.STATUS_OK;
            boolean statusError = wallet.getStatus() == Wallet.STATUS_ERROR;

            float balance = mData.getPrice(coin.getTicker()) * wallet.getBalance();
            String currencyBalanceStr = AppNumberFormatter.format(balance);

            currencyBalance.setText(String.format(Locale.US, "%s %s", currency.getSymbol(), currencyBalanceStr));

            Spanner.from(String.format(Locale.US, "%.4f %s", wallet.getBalance(), coin.getTicker()))
                    .style(coin.getTicker(), new ForegroundColorSpan(mTextColorSecondary))
                    .applyTo(balanceView);

            walletName.setText(TextUtils.isEmpty(wallet.getAlias()) ?
                    getContext().getString(R.string.my_wallet_format, coin.getName()) :
                    wallet.getAlias());
            walletName.setEllipsize(TextUtils.isEmpty(wallet.getAlias()) ? TextUtils.TruncateAt.MIDDLE :
                    TextUtils.TruncateAt.END);
            imageView.setImageResource(coin.getIconResId());

            statusIndicator.setVisibility(statusOk ? View.GONE : View.VISIBLE);
            statusIndicator.setBackgroundColor(statusError ? mWalletErrorColor : mWalletPendingColor);
            itemView.setOnClickListener(statusOk ? null : this::onItemClick);
        }

        public void onItemClick(View v) {
            if (getAdapterPosition() == RecyclerView.NO_POSITION)
                return;
            if (mCallback != null) {
                mCallback.onItemClick(mData.getWallet(getAdapterPosition()));
            }
        }

        @OnClick(R.id.context_menu)
        public void onContextMenuClick(View v) {
            final MyPopupMenu popup = new MyPopupMenu(getContext(), v, GravityCompat.END);
            popup.setForceShowIcon(true);
            popup.inflate(R.menu.wallet);
            popup.setOnMenuItemClickListener(this::onMenuItemClick);
            popup.show();
        }

        private boolean onMenuItemClick(MenuItem menuItem) {
            if (getAdapterPosition() == RecyclerView.NO_POSITION)
                return false;
            if (mCallback != null) {
                mCallback.onMenuItemClick(mData.getWallet(getAdapterPosition()), menuItem.getItemId());
            }
            return true;
        }

        private Context getContext() {
            return itemView.getContext();
        }

        @Override
        public void onItemSelected() {
            itemView.setAlpha(0.5f);
            if (mCallback != null) {
                mCallback.onStartDragging();
            }
        }

        @Override
        public void onItemClear() {
            itemView.setAlpha(1f);
            if (mCallback != null) {
                mCallback.onStopDragging();
            }
        }
    }


}
