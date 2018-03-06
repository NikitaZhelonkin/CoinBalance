package ru.nikitazhelonkin.coinbalance.presentation.main;


import android.support.v7.util.DiffUtil;

import ru.nikitazhelonkin.coinbalance.data.entity.MainViewModel;

public class MainDiffCallback extends DiffUtil.Callback {

    public MainViewModel oldModel;
    private MainViewModel newModel;

    public MainDiffCallback(MainViewModel oldModel, MainViewModel newModel) {
        this.oldModel = oldModel;
        this.newModel = newModel;
    }

    @Override
    public int getOldListSize() {
        return oldModel != null ? oldModel.getWalletCount() : 0;
    }

    @Override
    public int getNewListSize() {
        return newModel != null ? newModel.getWalletCount() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldModel.getWallet(oldItemPosition).equals(newModel.getWallet(newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }
}
