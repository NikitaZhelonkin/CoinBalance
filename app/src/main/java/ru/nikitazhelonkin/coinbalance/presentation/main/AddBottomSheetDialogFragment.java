package ru.nikitazhelonkin.coinbalance.presentation.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.nikitazhelonkin.coinbalance.R;
import ru.nikitazhelonkin.coinbalance.presentation.addexchange.AddExchangeActivity;
import ru.nikitazhelonkin.coinbalance.presentation.addwallet.AddWalletActivity;
import ru.nikitazhelonkin.coinbalance.ui.widget.DynamicLinearLayout;
import ru.nikitazhelonkin.coinbalance.ui.widget.DynamicViewGroupDelegate;

public class AddBottomSheetDialogFragment extends BottomSheetDialogFragment {

    public static AddBottomSheetDialogFragment create() {
        return new AddBottomSheetDialogFragment();
    }

    @BindView(R.id.items_container)
    DynamicLinearLayout mDynamicLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        Adapter adapter = new Adapter();
        adapter.setData(new ArrayList<>(Arrays.asList(
                new Item(R.id.add_wallet, R.drawable.ic_wallet_24dp, R.string.wallet),
                new Item(R.id.add_exchange, R.drawable.ic_exchange_24dp, R.string.exchange)
        )));
        mDynamicLayout.setAdapter(adapter);
        adapter.setOnItemClickListener(item -> {
            dismiss();
            switch (item.id){
                case R.id.add_wallet:
                    navigateToAddWalletView();
                    break;
                case R.id.add_exchange:
                    navigateToAddExchangeView();
                    break;
            }
        });
    }

    public void navigateToAddWalletView() {
        startActivity(AddWalletActivity.createIntent(getContext()));
    }

    public void navigateToAddExchangeView() {
        startActivity(AddExchangeActivity.createIntent(getContext()));
    }

    private static class Adapter extends DynamicViewGroupDelegate.Adapter {

        public interface OnItemClickListener {
            void onItemClick(Item item);
        }

        private List<Item> mData;

        private OnItemClickListener mOnItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            mOnItemClickListener = onItemClickListener;
        }

        public void setData(List<Item> data) {
            mData = data;
            notifyDataChanged();
        }

        @Override
        public int getCount() {
            return mData == null ? 0 : mData.size();
        }

        @Override
        protected View onCreateView(ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return inflater.inflate(R.layout.list_item_add, parent, false);
        }

        @Override
        protected void onBindView(View view, int position, Object payload) {
            Item item = mData.get(position);
            ImageView imageView = view.findViewById(R.id.icon);
            TextView titleView = view.findViewById(R.id.title);
            imageView.setImageResource(item.iconResId);
            titleView.setText(item.titleResId);
            view.setTag(position);
            view.setOnClickListener(v -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(mData.get((Integer) v.getTag()));
                }
            });
        }

    }

    private static class Item {
        public int id;
        public int iconResId;

        public int titleResId;

        public Item(int id, int iconResId, int titleResId) {
            this.id = id;
            this.iconResId = iconResId;
            this.titleResId = titleResId;
        }
    }
}
