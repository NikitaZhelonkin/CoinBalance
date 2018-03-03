package ru.nikitazhelonkin.cryptobalance.ui.widget;


import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import ru.nikitazhelonkin.cryptobalance.ui.text.TypefaceHelper;
import ru.nikitazhelonkin.cryptobalance.ui.text.TypefaceView;

public class TFTextView  extends AppCompatTextView implements TypefaceView {

    public TFTextView(Context context) {
        super(context);
    }

    public TFTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TFTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypefaceHelper.create(this).applyFromAttributes(attrs, android.R.attr.textViewStyle, 0);
    }
}
