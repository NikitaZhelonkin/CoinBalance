package ru.nikitazhelonkin.coinbalance.ui.text;


import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

public class FontSpan extends MetricAffectingSpan {

    private final Typeface typeface;

    public FontSpan(Typeface typeface) {
        this.typeface = typeface;
    }

    @Override
    public void updateDrawState(final TextPaint drawState) {
        apply(drawState);
    }

    @Override
    public void updateMeasureState(final TextPaint paint) {
        apply(paint);
    }

    private void apply(final Paint paint) {
        paint.setTypeface(typeface);
    }
}
