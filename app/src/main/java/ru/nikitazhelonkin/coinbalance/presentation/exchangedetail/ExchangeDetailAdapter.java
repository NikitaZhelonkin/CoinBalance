package ru.nikitazhelonkin.coinbalance.presentation.exchangedetail;


import android.support.v7.widget.RecyclerView;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Currency;
import java.util.List;
import java.util.Locale;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.nikitazhelonkin.coinbalance.R;
import ru.nikitazhelonkin.coinbalance.data.entity.Coin;
import ru.nikitazhelonkin.coinbalance.data.entity.ExchangeBalance;
import ru.nikitazhelonkin.coinbalance.data.entity.ExchangeDetailViewModel;
import ru.nikitazhelonkin.coinbalance.ui.text.Spanner;
import ru.nikitazhelonkin.coinbalance.utils.AppNumberFormatter;

public class ExchangeDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private ExchangeDetailViewModel mModel;

    public void setData(ExchangeDetailViewModel model) {
        mModel = model;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_HEADER:
                return new HeaderViewHolder(inflater.inflate(R.layout.header_exchange_detail, parent, false));
            case TYPE_ITEM:
                return new ViewHolder(inflater.inflate(R.layout.list_item_asset_simple, parent, false));
            default:
                throw new IllegalArgumentException("Wrong viewType " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).bind(position);
        } else if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).bind();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_ITEM;
    }


    @Override
    public int getItemCount() {
        return mModel == null ? 0 : getAssetCount() + 1;
    }

    private int getAssetCount() {
        return mModel == null ? 0 : mModel.getExchangeBalanceList().size();
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.fiat_value)
        TextView mFiatValue;
        @BindView(R.id.crypto_value)
        TextView mCryptoValue;
        @BindView(R.id.assets_label)
        View mAssetsLabel;
        @BindView(R.id.empty_view)
        View mEmptyView;
        @BindColor(R.color.colorTextSecondary)
        int mTextColorSecondary;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind() {
            List<ExchangeBalance> balanceList = mModel.getExchangeBalanceList();
            Currency currency = Currency.getInstance(mModel.getPrices().getCurrency());

            float currencyBalance = mModel.getBalance(balanceList);
            float balance = currencyBalance / mModel.getPriceValue(Coin.BTC.getTicker());

            String currencyBalanceStr = AppNumberFormatter.format(currencyBalance);

            mFiatValue.setText(String.format(Locale.US, "%s %s", currency.getSymbol(), currencyBalanceStr));

            Spanner.from(String.format(Locale.US, "%.4f %s", balance, Coin.BTC.getTicker()))
                    .style(Coin.BTC.getTicker(), new ForegroundColorSpan(mTextColorSecondary))
                    .applyTo(mCryptoValue);

            mEmptyView.setVisibility(balanceList.size() == 0 ? View.VISIBLE : View.GONE);
            mAssetsLabel.setVisibility(balanceList.size() == 0 ? View.GONE : View.VISIBLE);
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
            ExchangeBalance balance = mModel.getExchangeBalanceList().get(position - 1);
            mAssetNameView.setText(balance.getCoinTicker());
            mAssetValueView.setText(String.format(Locale.US, "%.4f", balance.getBalance()));
        }
    }
}
