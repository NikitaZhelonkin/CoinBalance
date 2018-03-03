package ru.nikitazhelonkin.cryptobalance.ui.widget;


import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class DynamicLinearLayout extends LinearLayout {

    private DynamicViewGroupDelegate mDynamicViewGroupDelegate;

    public DynamicLinearLayout(Context context) {
        super(context);
        init(context);
    }

    public DynamicLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DynamicLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DynamicLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mDynamicViewGroupDelegate = new DynamicViewGroupDelegate(this);
    }

    public void setAdapter(DynamicViewGroupDelegate.Adapter adapter) {
        mDynamicViewGroupDelegate.setAdapter(adapter);
    }

    public DynamicViewGroupDelegate.Adapter getAdapter() {
        return mDynamicViewGroupDelegate.getAdapter();
    }

}
