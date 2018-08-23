package ru.nikitazhelonkin.coinbalance.ui.widget;

import android.database.Observable;
import android.view.View;
import android.view.ViewGroup;

public class DynamicViewGroupDelegate {

    ViewGroup mViewGroup;

    public DynamicViewGroupDelegate(ViewGroup viewGroup) {
        mViewGroup = viewGroup;
    }

    public static abstract class Adapter {

        AdapterDataObservable mObservable = new AdapterDataObservable();

        public abstract int getCount();

        protected abstract View onCreateView(ViewGroup parent);

        protected abstract void onBindView(View view, int position, Object payload);

        private void registerDataSetObserver(AdapterDataObserver observer) {
            mObservable.registerObserver(observer);
        }

        private void unregisterDataSetObserver(AdapterDataObserver observer) {
            mObservable.unregisterObserver(observer);
        }

        public void notifyDataChanged() {
            notifyDataChanged(null);
        }

        public void notifyDataChanged(Object payload) {
            mObservable.notifyChanged(payload);
        }
    }


    static abstract class AdapterDataObserver {
        public abstract void onChanged(Object payload);
    }

    static class AdapterDataObservable extends Observable<AdapterDataObserver> {

        public void notifyChanged(Object payload) {
            for (AdapterDataObserver a : mObservers) {
                a.onChanged(payload);
            }
        }
    }

    private Adapter mAdapter;

    private AdapterDataObserver mAdapterDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged(Object payload) {
            populateViews(payload);
        }
    };

    public void setAdapter(Adapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mAdapterDataObserver);
        }
        mAdapter = adapter;
        mAdapter.registerDataSetObserver(mAdapterDataObserver);
        mAdapter = adapter;
        populateViews(null);
    }

    public Adapter getAdapter() {
        return mAdapter;
    }

    private void populateViews(Object payload) {
        int itemsCount = mAdapter.getCount();
        int viewsCount = mViewGroup.getChildCount();
        int diff = viewsCount - itemsCount;
        for (int i = 0; i < Math.abs(diff); i++) {
            if (diff > 0) {
                mViewGroup.removeViewAt(0);
            } else {
                View v = mAdapter.onCreateView(mViewGroup);
                mViewGroup.addView(v);
            }
        }
        for (int i = 0; i < mViewGroup.getChildCount(); i++) {
            mAdapter.onBindView(mViewGroup.getChildAt(i), i, payload);
        }
    }
}