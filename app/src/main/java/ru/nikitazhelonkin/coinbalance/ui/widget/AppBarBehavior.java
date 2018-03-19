package ru.nikitazhelonkin.coinbalance.ui.widget;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

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

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes, int type) {
        if(!mDragEnabled){
            return false;
        }
        return super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes, type);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        if(!mDragEnabled){
            return;
        }
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed, int type) {
        if(!mDragEnabled){
            return;
        }
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
    }
}
