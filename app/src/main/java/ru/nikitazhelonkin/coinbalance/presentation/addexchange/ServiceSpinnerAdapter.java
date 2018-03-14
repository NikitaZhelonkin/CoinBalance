package ru.nikitazhelonkin.coinbalance.presentation.addexchange;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.nikitazhelonkin.coinbalance.R;
import ru.nikitazhelonkin.coinbalance.data.entity.ExchangeService;

public class ServiceSpinnerAdapter extends BaseAdapter {

    private List<ExchangeService> mServiceList;

    public ServiceSpinnerAdapter() {

    }

    public void setServiceList(List<ExchangeService> serviceList) {
        mServiceList = serviceList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mServiceList == null ? 0 : mServiceList.size();
    }

    @Override
    public ExchangeService getItem(int i) {
        return mServiceList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mServiceList.get(i).getName().hashCode();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            convertView = inflater.inflate(R.layout.list_item_exchange_service, viewGroup, false);
        }
        TextView exchangeName = (TextView) convertView.findViewById(R.id.exchange_service_name);
        ImageView exchangeIcon = (ImageView) convertView.findViewById(R.id.exchange_service_icon);
        ExchangeService exchangeService = getItem(i);
        exchangeName.setText(exchangeService.getName());
        exchangeIcon.setImageResource(exchangeService.getIconResId());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
