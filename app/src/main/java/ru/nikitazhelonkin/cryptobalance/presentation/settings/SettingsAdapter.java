package ru.nikitazhelonkin.cryptobalance.presentation.settings;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import ru.nikitazhelonkin.cryptobalance.R;
import ru.nikitazhelonkin.cryptobalance.ui.adapter.RecyclerItemsAdapter;

public class SettingsAdapter extends RecyclerItemsAdapter {

    private static final int ITEM_DEFAULT = 0;
    private static final int ITEM_FOOTER = 1;

    public interface Callback {
        void onAddressClick(String address);
    }

    private Callback mCallback;

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public SettingsAdapter() {

    }

    public void invalidateItems(Context context, String currency) {
        List<IListItem> items = new ArrayList<>();
        items.add(new DefaultItem(R.id.settings_currency,
                R.drawable.ic_euro_symbol_24dp,
                context.getString(R.string.settings_currency), currency));
        items.add(new DefaultItem(R.id.settings_import,
                R.drawable.ic_import_24dp,
                context.getString(R.string.settings_import_config)));
        items.add(new DefaultItem(R.id.settings_export,
                R.drawable.ic_export_24dp,
                context.getString(R.string.settings_export_config)));
        items.add(new DefaultItem(R.id.settings_report, R.drawable.ic_email_24dp,
                context.getString(R.string.settings_report_error)));
        items.add(new DefaultItem(R.id.settings_share, R.drawable.ic_share_24dp,
                context.getString(R.string.settings_share_app)));
        items.add(new DefaultItem(R.id.settings_rate, R.drawable.ic_rate_24dp,
                context.getString(R.string.settings_rate_app)));
        items.add(new FooterItem());
        setData(items);
    }

    public static class DefaultItem implements IListItem {

        private int id;
        private String title;
        private String value;
        private int iconResId;


        public DefaultItem(int id, int iconResId, String title) {
            this(id, iconResId, title, null);
        }

        public DefaultItem(int id, int iconResId, String title, String value) {
            this.id = id;
            this.iconResId = iconResId;
            this.title = title;
            this.value = value;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public int getType() {
            return ITEM_DEFAULT;
        }

        @Override
        public boolean isClickable() {
            return true;
        }

        @Override
        public AbsViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
            return new ViewHolder(inflater.inflate(R.layout.list_item_settings, parent, false));
        }

        public class ViewHolder extends AbsViewHolder {

            @BindView(R.id.title)
            TextView mTitleView;
            @BindView(R.id.value)
            TextView mValueView;

            public ViewHolder(View itemView) {
                super(itemView);
            }

            @Override
            public void bind(IListItem iListItem) {
                super.bind(iListItem);
                DefaultItem item = (DefaultItem) iListItem;
                mTitleView.setText(item.title);
                mValueView.setText(item.value);
                mTitleView.setCompoundDrawablesRelativeWithIntrinsicBounds(item.iconResId, 0, 0, 0);
            }
        }
    }

    public class FooterItem implements IListItem {

        @Override
        public int getId() {
            return R.id.settings_footer;
        }

        @Override
        public int getType() {
            return ITEM_FOOTER;
        }

        @Override
        public boolean isClickable() {
            return false;
        }

        @Override
        public AbsViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
            return new ViewHolder(inflater.inflate(R.layout.list_item_settings_footer, parent, false));
        }

        public class ViewHolder extends AbsViewHolder {

            public ViewHolder(View itemView) {
                super(itemView);
            }

            @Override
            public void bind(IListItem iListItem) {
                super.bind(iListItem);

            }

            @OnClick(R.id.btc_address_layout)
            public void onBTCClick(View v) {
                onAddressClick(v.getContext().getString(R.string.btc_address));
            }

            @OnClick(R.id.eth_address_layout)
            public void onETHClick(View v) {
                onAddressClick(v.getContext().getString(R.string.eth_address));
            }

            @OnClick(R.id.ltc_address_layout)
            public void onLTCClick(View v) {
                onAddressClick(v.getContext().getString(R.string.ltc_address));
            }

            public void onAddressClick(String address) {
                if (mCallback != null) {
                    mCallback.onAddressClick(address);
                }
            }

        }
    }
}
