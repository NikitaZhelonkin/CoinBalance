package ru.nikitazhelonkin.cryptobalance.presentation.add;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.nikitazhelonkin.cryptobalance.R;
import ru.nikitazhelonkin.cryptobalance.data.entity.Coin;
import ru.nikitazhelonkin.cryptobalance.data.repository.CoinRepository;

public class CoinAutoCompleteAdapter extends BaseAdapter implements Filterable {

    private CoinRepository mCoinRepository;
    private List<Coin> mCoinList;

    public CoinAutoCompleteAdapter(CoinRepository coinRepository) {
        mCoinRepository = coinRepository;
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
    public Filter getFilter() {
        return new android.widget.Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    List<Coin> coins = mCoinRepository.search(constraint.toString()).blockingGet();
                    filterResults.values = coins;
                    filterResults.count = coins.size();
                }
                return filterResults;
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    setCoinList((List<Coin>) results.values);
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}
