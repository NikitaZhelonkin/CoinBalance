package ru.nikitazhelonkin.coinbalance.ui.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import ru.nikitazhelonkin.coinbalance.R;

public class TintDrawableTextView extends AppCompatTextView {

    private int mCompoundDrawableTint;

    public TintDrawableTextView(Context context) {
        super(context);
        init(context, null);
    }

    public TintDrawableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TintDrawableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, new int[]{R.attr.iconTint});
        mCompoundDrawableTint = a.getColor(0, -1);

        applyTint();

        a.recycle();
    }

    public void setCompoundDrawableTint(int compoundDrawableTint) {
        mCompoundDrawableTint = compoundDrawableTint;
        applyTint();
    }

    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        applyTint();
    }

    private void applyTint() {
        if (mCompoundDrawableTint != -1) {
            Drawable[] drawables = getCompoundDrawables();
            super.setCompoundDrawablesWithIntrinsicBounds(
                    TintHelper.applyTint(drawables[0], mCompoundDrawableTint),
                    TintHelper.applyTint(drawables[1], mCompoundDrawableTint),
                    TintHelper.applyTint(drawables[2], mCompoundDrawableTint),
                    TintHelper.applyTint(drawables[3], mCompoundDrawableTint)
            );
        }
    }
}
