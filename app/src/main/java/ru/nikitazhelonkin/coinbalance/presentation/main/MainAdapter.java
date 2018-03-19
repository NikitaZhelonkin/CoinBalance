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
import java.util.List;
import java.util.Locale;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.nikitazhelonkin.coinbalance.R;
import ru.nikitazhelonkin.coinbalance.data.entity.Coin;
import ru.nikitazhelonkin.coinbalance.data.entity.Exchange;
import ru.nikitazhelonkin.coinbalance.data.entity.ExchangeBalance;
import ru.nikitazhelonkin.coinbalance.data.entity.MainViewModel;
import ru.nikitazhelonkin.coinbalance.data.entity.Wallet;
import ru.nikitazhelonkin.coinbalance.ui.text.Spanner;
import ru.nikitazhelonkin.coinbalance.ui.widget.MyPopupMenu;
import ru.nikitazhelonkin.coinbalance.ui.widget.itemtouchhelper.ItemTouchHelperAdapter;
import ru.nikitazhelonkin.coinbalance.ui.widget.itemtouchhelper.ItemTouchHelperViewHolder;
import ru.nikitazhelonkin.coinbalance.utils.AppNumberFormatter;

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        ItemTouchHelperAdapter {

    private static final int TYPE_WALLET = 0;
    private static final int TYPE_EXCHANGE = 1;

    private MainViewModel mData;

    public interface Callback {

        void onWalletMenuItemClick(Wallet wallet, int itemId);

        void onExchangeMenuItemClick(Exchange exchange, int itemId);

        void onWalletItemClick(Wallet wallet);

        void onExchangeItemClick(Exchange exchange);

        void onStartDragging();

        void onStopDragging();
    }

    public MainAdapter(){
        setHasStableIds(true);
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_WALLET:
                return new WalletViewHolder(inflater.inflate(R.layout.list_item_wallet, parent, false));
            case TYPE_EXCHANGE:
                return new ExchangeViewHolder(inflater.inflate(R.layout.list_item_exchange, parent, false));
            default:
                throw new IllegalArgumentException("Unsupported viewType:" + viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case TYPE_WALLET:
                ((WalletViewHolder) holder).bind((Wallet) mData.getItem(position));
                break;
            case TYPE_EXCHANGE:
                ((ExchangeViewHolder) holder).bind((Exchange) mData.getItem(position));
                break;
            default:
                throw new IllegalArgumentException("Unsupported viewType:" + viewType);
        }
    }

    @Override
    public long getItemId(int position) {
        int result = mData.getItem(position).getId();
        result = 31 * result +  getItemViewType(position);
        return result;
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.getItem(position) instanceof Wallet) {
            return TYPE_WALLET;
        } else {
            return TYPE_EXCHANGE;
        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        mData.swapItems(fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.getItems().size() : 0;
    }

    public class WalletViewHolder extends BaseViewHolder {

        @BindView(R.id.coin_icon)
        ImageView imageView;
        @BindView(R.id.currency_balance)
        TextView currencyBalanceView;
        @BindView(R.id.balance)
        TextView balanceView;
        @BindView(R.id.wallet_name)
        TextView walletName;
        @BindView(R.id.status_indicator)
        View statusIndicator;
        @BindColor(R.color.colorTextSecondary)
        int mTextColorSecondary;
        @BindColor(R.color.color_error)
        int mErrorColor;
        @BindColor(R.color.color_pending)
        int mPendingColor;

        public WalletViewHolder(View itemView) {
            super(itemView);
        }

        private void bind(Wallet wallet) {
            Coin coin = mData.getCoin(wallet.getCoinTicker());
            Currency currency = Currency.getInstance(mData.getCurrency());
            boolean statusOk = wallet.getStatus() == Wallet.STATUS_OK;
            boolean statusPending = wallet.getStatus() == Wallet.STATUS_NONE;

            float currencyBalance = mData.getPriceValue(coin.getTicker()) * wallet.getBalance();
            String currencyBalanceStr = AppNumberFormatter.format(currencyBalance);

            currencyBalanceView.setText(String.format(Locale.US, "%s %s", currency.getSymbol(), currencyBalanceStr));
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
            statusIndicator.setBackgroundColor(statusPending ? mPendingColor : mErrorColor);
            itemView.setOnClickListener(statusOk ? null : this::onItemClick);
        }

        public void onItemClick(View v) {
            if (getAdapterPosition() == RecyclerView.NO_POSITION)
                return;
            if (mCallback != null) {
                mCallback.onWalletItemClick((Wallet) mData.getItem(getAdapterPosition()));
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
                mCallback.onWalletMenuItemClick((Wallet) mData.getItem(getAdapterPosition()), menuItem.getItemId());
            }
            return true;
        }

    }

    public class ExchangeViewHolder extends BaseViewHolder {

        @BindView(R.id.exchange_icon)
        ImageView imageView;
        @BindView(R.id.currency_balance)
        TextView currencyBalanceView;
        @BindView(R.id.balance)
        TextView balanceView;
        @BindView(R.id.exchange_name)
        TextView walletName;
        @BindView(R.id.exchange_assets)
        TextView exchangeAssets;
        @BindView(R.id.status_indicator)
        View statusIndicator;
        @BindColor(R.color.colorTextSecondary)
        int mTextColorSecondary;
        @BindColor(R.color.color_error)
        int mErrorColor;
        @BindColor(R.color.color_pending)
        int mPendingColor;

        public ExchangeViewHolder(View itemView) {
            super(itemView);
        }

        private void bind(Exchange exchange) {
            Currency currency = Currency.getInstance(mData.getCurrency());
            boolean statusOk = exchange.getStatus() == Wallet.STATUS_OK;
            boolean statusPending = exchange.getStatus() == Wallet.STATUS_NONE;

            List<ExchangeBalance> balanceList = mData.getExchangeBalances(exchange.getId());
            float currencyBalance = mData.getBalance(balanceList);
            float balance = currencyBalance / mData.getPriceValue(Coin.BTC.getTicker());
            String currencyBalanceStr = AppNumberFormatter.format(currencyBalance);
            int assetsCount = balanceList != null ? balanceList.size() : 0;
            String assetCountString = getContext().getResources().getQuantityString(R.plurals.assets_count, assetsCount, assetsCount);
            exchangeAssets.setText(assetCountString);
            currencyBalanceView.setText(String.format(Locale.US, "%s %s", currency.getSymbol(), currencyBalanceStr));

            Spanner.from(String.format(Locale.US, "%.4f %s", balance, Coin.BTC.getTicker()))
                    .style(Coin.BTC.getTicker(), new ForegroundColorSpan(mTextColorSecondary))
                    .applyTo(balanceView);

            walletName.setText(TextUtils.isEmpty(exchange.getTitle()) ?
                    exchange.getService().getName() :
                    exchange.getTitle());

            walletName.setEllipsize(TextUtils.TruncateAt.END);

            imageView.setImageResource(exchange.getService().getIconResId());

            statusIndicator.setVisibility(statusOk ? View.GONE : View.VISIBLE);
            statusIndicator.setBackgroundColor(statusPending ? mPendingColor : mErrorColor);
            itemView.setOnClickListener(statusOk ? null : this::onItemClick);
        }

        public void onItemClick(View v) {
            if (getAdapterPosition() == RecyclerView.NO_POSITION)
                return;
            if (mCallback != null) {
                mCallback.onExchangeItemClick((Exchange) mData.getItem(getAdapterPosition()));
            }
        }

        @OnClick(R.id.context_menu)
        public void onContextMenuClick(View v) {
            final MyPopupMenu popup = new MyPopupMenu(getContext(), v, GravityCompat.END);
            popup.setForceShowIcon(true);
            popup.inflate(R.menu.exchange);
            popup.setOnMenuItemClickListener(this::onMenuItemClick);
            popup.show();
        }

        private boolean onMenuItemClick(MenuItem menuItem) {
            if (getAdapterPosition() == RecyclerView.NO_POSITION)
                return false;
            if (mCallback != null) {
                mCallback.onExchangeMenuItemClick((Exchange) mData.getItem(getAdapterPosition()), menuItem.getItemId());
            }
            return true;
        }
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected Context getContext() {
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
