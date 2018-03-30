package ru.nikitazhelonkin.coinbalance.presentation.main;


import android.support.annotation.Nullable;
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
        return oldModel != null ? oldModel.getItems().size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newModel != null ? newModel.getItems().size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldModel.getItem(oldItemPosition).equals(newModel.getItem(newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return "";
    }
}
