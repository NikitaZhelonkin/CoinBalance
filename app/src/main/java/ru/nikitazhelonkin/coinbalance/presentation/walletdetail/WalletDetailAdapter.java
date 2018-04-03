package ru.nikitazhelonkin.coinbalance.presentation.walletdetail;


import android.support.v7.widget.RecyclerView;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Currency;
import java.util.Locale;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.nikitazhelonkin.coinbalance.R;
import ru.nikitazhelonkin.coinbalance.data.entity.Coin;
import ru.nikitazhelonkin.coinbalance.data.entity.Token;
import ru.nikitazhelonkin.coinbalance.data.entity.Wallet;
import ru.nikitazhelonkin.coinbalance.data.entity.WalletDetailViewModel;
import ru.nikitazhelonkin.coinbalance.ui.text.Spanner;
import ru.nikitazhelonkin.coinbalance.utils.AppNumberFormatter;
import ru.nikitazhelonkin.coinbalance.utils.L;

public class WalletDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public interface Callback {
        void onWalletAddressClick();
    }

    private WalletDetailViewModel mWalletModel;

    private Callback mCallback;

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public WalletDetailAdapter() {

    }

    public void setData(WalletDetailViewModel data) {
        mWalletModel = data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_HEADER:
                return new HeaderViewHolder(inflater.inflate(R.layout.header_wallet_detail, parent, false));
            case TYPE_ITEM:
                return new ViewHolder(inflater.inflate(R.layout.list_item_asset_simple, parent, false));
            default:
                throw new IllegalArgumentException("Wrong viewType " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case TYPE_HEADER:
                ((HeaderViewHolder) holder).bind();
                break;
            case TYPE_ITEM:
                ((ViewHolder) holder).bind(position);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mWalletModel == null ? 0 : mWalletModel.getTokenList().size() + 1;
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.wallet_address)
        TextView mWalletAddressView;
        @BindView(R.id.fiat_value)
        TextView mFiatValue;
        @BindView(R.id.crypto_value)
        TextView mCryptoValue;
        @BindColor(R.color.colorTextSecondary)
        int mTextColorSecondary;
        @BindView(R.id.tokens_label)
        View mTokenLabel;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind() {
            Wallet wallet = mWalletModel.getWallet();
            Coin coin = Coin.forTicker(wallet.getCoinTicker());
            if (coin == null)
                return;

            mWalletAddressView.setText(wallet.getAddress());

            Currency currency = Currency.getInstance(mWalletModel.getCurrency());

            float fiatBalance = mWalletModel.getPrices().getPriceValue(coin.getTicker()) * wallet.getBalance();
            String fiatBalanceStr = AppNumberFormatter.format(fiatBalance);

            mFiatValue.setText(String.format(Locale.US, "%s %s", currency.getSymbol(), fiatBalanceStr));

            Spanner.from(String.format(Locale.US, "%.4f %s", wallet.getBalance(), coin.getTicker()))
                    .style(coin.getTicker(), new ForegroundColorSpan(mTextColorSecondary))
                    .applyTo(mCryptoValue);
            mTokenLabel.setVisibility(mWalletModel.getTokenList().size() == 0 ? View.GONE : View.VISIBLE);
        }


        @OnClick(R.id.wallet_address)
        public void onWalletAddressClick(View v) {
            if (mCallback != null) {
                mCallback.onWalletAddressClick();
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.asset_name)
        TextView mAssetNameView;
        @BindView(R.id.asset_value)
        TextView mAssetValueView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(int position) {
            Token token = mWalletModel.getTokenList().get(position - 1);
            mAssetNameView.setText(token.getTokenTicker());
            mAssetValueView.setText(String.format(Locale.US, "%.4f", token.getBalance()));
        }
    }
}
