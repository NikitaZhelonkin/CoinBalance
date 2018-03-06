package ru.nikitazhelonkin.coinbalance.ui.text;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import ru.nikitazhelonkin.coinbalance.R;

public class TypefaceHelper {

    private TypefaceView mView;

    private Context mContext;

    public static TypefaceHelper create(TypefaceView typefaceView){
        return new TypefaceHelper(typefaceView);
    }

    private TypefaceHelper(TypefaceView typefaceView) {
        mView = typefaceView;
        mContext = typefaceView.getContext();
    }

    public void applyFromAttributes(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.TFView, defStyleAttr, defStyleRes);

        try {
            String typefaceName = a.getString(R.styleable.TFView_font);
            Typeface typeface = Typefaces.getTypeface(mContext, typefaceName);
            if (typeface != null) {
                mView.setTypeface(typeface);
            }
        } finally {
            a.recycle();
        }
    }
}
