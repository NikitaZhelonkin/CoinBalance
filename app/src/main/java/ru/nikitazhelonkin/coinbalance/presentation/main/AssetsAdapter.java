package ru.nikitazhelonkin.coinbalance.presentation.main;


import android.support.v7.widget.RecyclerView;
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
import ru.nikitazhelonkin.coinbalance.data.entity.AssetItem;
import ru.nikitazhelonkin.coinbalance.ui.widget.PieChartView;
import ru.nikitazhelonkin.coinbalance.ui.widget.TintDrawableTextView;
import ru.nikitazhelonkin.coinbalance.utils.AppNumberFormatter;
import ru.nikitazhelonkin.coinbalance.utils.ChartColorPallet;

public class AssetsAdapter extends RecyclerView.Adapter<AssetsAdapter.ViewHolder> {

    private List<AssetItem> mData;

    public AssetsAdapter() {
    }

    public void setData(List<AssetItem> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).hashCode();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.list_item_asset, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mData.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.asset_percent)
        TextView mAssetPercentView;
        @BindView(R.id.asset_name)
        TextView mAssetNameView;
        @BindView(R.id.asset_value)
        TextView mAssetValueView;
        @BindView(R.id.asset_coin_value)
        TextView mAssetCoinValueView;
        @BindView(R.id.asset_price)
        TextView mAssetPriceView;
        @BindView(R.id.asset_change24)
        TintDrawableTextView mAssetChange24View;

        @BindView(R.id.asset_color)
        View mAssetColorView;


        @BindColor(R.color.color_trend_up)
        int mColorTrendUp;
        @BindColor(R.color.color_trend_down)
        int mColorTrendDown;
        @BindColor(R.color.color_trend_none)
        int mColorTrendNone;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(AssetItem assetItem, int position) {
            Currency currency = Currency.getInstance(assetItem.getCurrency());
            float change24 = assetItem.getChange24();
            int trendColor = change24 > 0 ? mColorTrendUp : change24 < 0 ? mColorTrendDown : mColorTrendNone;
            int trendIcon = change24 >= 0 ? R.drawable.ic_trending_up_24dp : R.drawable.ic_trending_down_24dp;
            mAssetNameView.setText(assetItem.getCoin());
            String currencyBalanceStr = AppNumberFormatter.format(assetItem.getCurrencyBalance());
            mAssetValueView.setText(String.format(Locale.US, "%s %s", currency.getSymbol(), currencyBalanceStr));
            mAssetCoinValueView.setText(String.format(Locale.US, "%.4f", assetItem.getBalance()));
            mAssetPercentView.setText(String.format(Locale.US, "%.1f %%", assetItem.getPercent()));
            mAssetColorView.setBackgroundColor(assetItem.getPercent() >= PieChartView.MIN_PERCENT ?
                    ChartColorPallet.colorForPosition(position) : PieChartView.OTHER_COLOR);

            String priceStr = AppNumberFormatter.format(assetItem.getPrice());
            mAssetPriceView.setText(String.format(Locale.US, "%s %s", currency.getSymbol(), priceStr));
            mAssetChange24View.setText(String.format(Locale.US, "%.1f %%", Math.abs(assetItem.getChange24())));
            mAssetChange24View.setTextColor(trendColor);
            mAssetChange24View.setCompoundDrawableTint(trendColor);
            mAssetChange24View.setCompoundDrawablesWithIntrinsicBounds(trendIcon, 0, 0, 0);
        }


    }
}
