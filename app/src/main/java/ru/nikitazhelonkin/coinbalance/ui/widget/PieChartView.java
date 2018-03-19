package ru.nikitazhelonkin.coinbalance.ui.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ru.nikitazhelonkin.coinbalance.utils.ListUtils;

public class PieChartView extends View {

    public static final int OTHER_COLOR = Color.parseColor("#cccccc");

    public static final float MIN_PERCENT = 2;

    private static final int STROKE_WIDTH = 2;//dp

    private DataSet mDataSet;

    private Path mClipPath;

    private Paint mPaint;
    private Paint mStrokePaint;
    private Paint mBackgroundPaint;

    private RectF mRectF;


    public PieChartView(Context context) {
        super(context);
        init(context);
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {

        float stokeWidth = context.getResources().getDisplayMetrics().density * STROKE_WIDTH;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);

        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setColor(Color.BLACK);
        mStrokePaint.setStrokeWidth(stokeWidth);

        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(Color.WHITE);
        mBackgroundPaint.setStyle(Paint.Style.FILL);

        if (isInEditMode()) {
            setData(new ArrayList<>(Arrays.asList(
                    new PieEntry("Label 1", 10, Color.RED),
                    new PieEntry("Label 2", 20, Color.BLUE),
                    new PieEntry("Label 3", 30, Color.YELLOW),
                    new PieEntry("Label 4", 50, Color.GREEN)))
            );
        }
    }

    public void setData(List<PieEntry> pieEntries) {
        mDataSet = new DataSet(pieEntries);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mDataSet == null) {
            return;
        }
        float lastAngle = 0;


        int saveCount = canvas.save();
        canvas.clipPath(mClipPath, Region.Op.DIFFERENCE);

        //draw background circle
        canvas.drawCircle(mRectF.centerX(), mRectF.centerY(), mRectF.width() / 2, mBackgroundPaint);

        //draw all except little percents
        for (int i = 0; i < mDataSet.pieEntries.size(); i++) {
            PieEntry entry = mDataSet.pieEntries.get(i);
            float percent = mDataSet.entryPercent(entry);
            if (percent >= MIN_PERCENT) {
                float sweepAngle = 360 * percent / 100f;
                mPaint.setColor(entry.color);
                canvas.drawArc(mRectF, lastAngle, sweepAngle, true, mPaint);
                canvas.drawArc(mRectF, lastAngle, sweepAngle, true, mStrokePaint);
                lastAngle += sweepAngle;
            }
        }
        //draw other
        if (lastAngle < 360) {
            mPaint.setColor(OTHER_COLOR);
            canvas.drawArc(mRectF, lastAngle, 360 - lastAngle, true, mPaint);
            canvas.drawArc(mRectF, lastAngle, 360 - lastAngle, true, mStrokePaint);
        }
        canvas.restoreToCount(saveCount);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF = new RectF(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), +getHeight() - getPaddingBottom());
        mClipPath = new Path();
        mClipPath.addCircle(mRectF.centerX(), mRectF.centerY(), mRectF.width() / 3, Path.Direction.CW);
    }

    private static class DataSet {

        private List<PieEntry> pieEntries;
        private float totalValue;

        public DataSet(List<PieEntry> pieEntries) {
            this.pieEntries = pieEntries;
            Collections.sort(this.pieEntries, (t1, t2) -> {
                float v1 = t1.value;
                float v2 = t2.value;
                return v1 > v2 ? -1 : v1 < v2 ? 1 : 0;
            });
            this.totalValue = ListUtils.reduce(pieEntries, totalValue, (aFloat, pieEntry) -> aFloat + pieEntry.value);
        }

        public float entryPercent(PieEntry pieEntry) {
            return pieEntry.value / totalValue * 100;
        }
    }

    public static class PieEntry {
        private String label;
        private float value;
        private int color;

        public PieEntry(String label, float value, int color) {
            this.label = label;
            this.value = value;
            this.color = color;
        }

    }
}
