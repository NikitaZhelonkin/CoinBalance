package ru.nikitazhelonkin.cryptobalance.presentation.main;


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

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.nikitazhelonkin.cryptobalance.R;
import ru.nikitazhelonkin.cryptobalance.data.entity.Coin;
import ru.nikitazhelonkin.cryptobalance.data.entity.MainViewModel;
import ru.nikitazhelonkin.cryptobalance.data.entity.Wallet;
import ru.nikitazhelonkin.cryptobalance.ui.text.FontSpan;
import ru.nikitazhelonkin.cryptobalance.ui.text.Spanner;
import ru.nikitazhelonkin.cryptobalance.ui.text.Typefaces;
import ru.nikitazhelonkin.cryptobalance.ui.widget.MyPopupMenu;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private MainViewModel mData;

    public interface Callback {
        void onMenuItemClick(Wallet wallet, int itemId);
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
    public int getItemCount() {
        return mData != null ? mData.getWalletCount() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.coin_icon)
        ImageView imageView;
        @BindView(R.id.currency_balance)
        TextView currencyBalance;
        @BindView(R.id.balance)
        TextView balanceView;
        @BindView(R.id.wallet_name)
        TextView walletName;
        @BindColor(R.color.colorTextSecondary)
        int mTextColorSecondary;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(Wallet wallet) {
            Coin coin = mData.getCoin(wallet.getCoinTicker());
            Currency currency = Currency.getInstance(mData.getCurrency());
            NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
            format.setCurrency(currency);

            String currencyBalanceStr = format.format(mData.getPrice(coin.getTicker()) * wallet.getBalance())
                    .replace(currency.getSymbol(), "");

            Spanner.from(String.format(Locale.US, "%s %s", currencyBalanceStr, currency.getSymbol()))
                    .style(currency.getSymbol()
                            , new ForegroundColorSpan(mTextColorSecondary))
                    .style(currency.getSymbol(), new FontSpan(Typefaces.getTypeface(getContext(),
                            R.string.font_roboto_regular)))
                    .applyTo(currencyBalance);

            Spanner.from(String.format(Locale.US, "%f %s", wallet.getBalance(), coin.getTicker()))
                    .style(coin.getTicker(), new ForegroundColorSpan(mTextColorSecondary))
                    .applyTo(balanceView);

            walletName.setText(TextUtils.isEmpty(wallet.getAlias()) ? wallet.getAddress() : wallet.getAlias());
            walletName.setEllipsize(TextUtils.isEmpty(wallet.getAlias()) ? TextUtils.TruncateAt.MIDDLE :
                    TextUtils.TruncateAt.END);
            imageView.setImageResource(coin.getIconResId());
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

    }


}
