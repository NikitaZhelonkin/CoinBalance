package ru.nikitazhelonkin.coinbalance.ui.widget;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.util.AttributeSet;

public class AppBarBehavior extends AppBarLayout.Behavior {

    private boolean mDragEnabled = true;

    private DragCallback mDragCallback = new DragCallback() {
        @Override
        public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
            return mDragEnabled;
        }
    };

    public AppBarBehavior() {
        setDragCallback(mDragCallback);
    }

    public AppBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDragCallback(mDragCallback);
    }

    public void setDragEnabled(boolean scrollEnabled) {
        mDragEnabled = scrollEnabled;
    }



}
