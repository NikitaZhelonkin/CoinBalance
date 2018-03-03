package ru.nikitazhelonkin.cryptobalance.presentation.add;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.nikitazhelonkin.cryptobalance.R;
import ru.nikitazhelonkin.cryptobalance.data.entity.Coin;

public class CoinSpinnerAdapter extends BaseAdapter {

    private List<Coin> mCoinList;

    public CoinSpinnerAdapter() {

    }

    public void setCoinList(List<Coin> coinList) {
        mCoinList = coinList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mCoinList == null ? 0 : mCoinList.size();
    }

    @Override
    public Coin getItem(int i) {
        return mCoinList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mCoinList.get(i).getTicker().hashCode();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            convertView = inflater.inflate(R.layout.list_item_coin, viewGroup, false);
        }
        TextView coinName = (TextView) convertView.findViewById(R.id.coin_name);
        ImageView coinIcon = (ImageView) convertView.findViewById(R.id.coin_icon);
        Coin coin = getItem(i);
        coinName.setText(coin.getName());
        coinIcon.setImageResource(coin.getIconResId());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
