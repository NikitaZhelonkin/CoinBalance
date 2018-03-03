package ru.nikitazhelonkin.cryptobalance.ui.widget;


import android.database.Observable;
import android.view.View;
import android.view.ViewGroup;

import ru.nikitazhelonkin.cryptobalance.R;


public class DynamicViewGroupDelegate {

    ViewGroup mViewGroup;

    public DynamicViewGroupDelegate(ViewGroup viewGroup) {
        mViewGroup = viewGroup;
    }

    public static abstract class Adapter<T extends ViewHolder> {

        AdapterDataObservable mObservable = new AdapterDataObservable();

        public abstract int getCount();

        protected abstract T onCreateViewHolder(ViewGroup parent);

        protected abstract void onBindViewHolder(T viewHolder, int position, Object payload);

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

    @SuppressWarnings("unchecked")
    private void populateViews(Object payload) {
        int itemsCount = mAdapter.getCount();
        int viewsCount = mViewGroup.getChildCount();
        int diff = viewsCount - itemsCount;
        for (int i = 0; i < Math.abs(diff); i++) {
            if (diff > 0) {
                mViewGroup.removeView(mViewGroup.getChildAt(0));
            } else {
                ViewHolder vh = mAdapter.onCreateViewHolder(mViewGroup);
                vh.itemView.setTag( vh);
                mViewGroup.addView(vh.itemView, vh.itemView.getLayoutParams());
            }
        }
        for (int i = 0; i < mViewGroup.getChildCount(); i++) {
            ViewHolder vh = (ViewHolder) mViewGroup.getChildAt(i).getTag();
            mAdapter.onBindViewHolder(vh, i, payload);
        }
    }

    public abstract static class ViewHolder {
        protected View itemView;

        public ViewHolder(View view) {
            itemView = view;
        }
    }

}
