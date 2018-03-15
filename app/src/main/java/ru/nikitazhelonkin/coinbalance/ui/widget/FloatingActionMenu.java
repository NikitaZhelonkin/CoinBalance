package ru.nikitazhelonkin.coinbalance.ui.widget;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;

import ru.nikitazhelonkin.coinbalance.R;

public class FloatingActionMenu extends ViewGroup {

    private static final int ANIMATION_DURATION = 300;
    private static final float COLLAPSED_PLUS_ROTATION = 0f;
    private static final float EXPANDED_PLUS_ROTATION = 90f + 45f;

    private int mButtonSpacing;
    private int mAddButtonSpacing;

    private boolean mExpanded;

    private AnimatorSet mExpandAnimation = new AnimatorSet().setDuration(ANIMATION_DURATION);
    private AnimatorSet mCollapseAnimation = new AnimatorSet().setDuration(ANIMATION_DURATION);
    private FloatingActionButton mAddButton;
    private RotatingDrawable mRotatingDrawable;
    private int mButtonsCount;

    private int mAddButtonId;

    private OnFloatingActionsMenuUpdateListener mListener;

    public interface OnFloatingActionsMenuUpdateListener {
        void onMenuExpanded();

        void onMenuCollapsed();
    }

    public FloatingActionMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FloatingActionMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);

    }

    private void init(Context context, AttributeSet attributeSet) {

        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.FloatingActionMenu);
        mAddButtonId = a.getResourceId(R.styleable.FloatingActionMenu_add_btn, -1);
        mAddButtonSpacing = a.getDimensionPixelSize(R.styleable.FloatingActionMenu_add_btn_spacing, 0);
        mButtonSpacing = a.getDimensionPixelSize(R.styleable.FloatingActionMenu_btn_spacing, 0);
        a.recycle();

        if (mAddButtonId == -1) {
            throw new IllegalArgumentException("Add button not defined");
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        initAddButton(getContext());

        bringChildToFront(mAddButton);

        mButtonsCount = getChildCount();

    }

    public void setOnFloatingActionsMenuUpdateListener(OnFloatingActionsMenuUpdateListener listener) {
        mListener = listener;
    }

    private void initAddButton(Context context) {

        mAddButton = (FloatingActionButton) findViewById(mAddButtonId);

        if (mAddButton == null) {
            throw new IllegalArgumentException("Cannot find add button with id" + mAddButtonId);
        }

        Drawable drawable = mAddButton.getDrawable();
        if(drawable!=null){
            final RotatingDrawable rotatingDrawable = new RotatingDrawable(drawable);
            mRotatingDrawable = rotatingDrawable;

            mAddButton.setImageDrawable(rotatingDrawable);

            final OvershootInterpolator interpolator = new OvershootInterpolator();

            final ObjectAnimator collapseAnimator = ObjectAnimator.ofFloat(rotatingDrawable, "rotation", EXPANDED_PLUS_ROTATION, COLLAPSED_PLUS_ROTATION);
            final ObjectAnimator expandAnimator = ObjectAnimator.ofFloat(rotatingDrawable, "rotation", COLLAPSED_PLUS_ROTATION, EXPANDED_PLUS_ROTATION);

            collapseAnimator.setInterpolator(interpolator);
            expandAnimator.setInterpolator(interpolator);

            mExpandAnimation.play(expandAnimator);
            mCollapseAnimation.play(collapseAnimator);

        }


        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });

        mButtonsCount++;
    }

    public void addButton(View button) {
        addView(button, mButtonsCount - 1);
        mButtonsCount++;
    }

    public void removeButton(View button) {
        removeView(button);
        mButtonsCount--;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int width = 0;
        int height = 0;

        int maxButtonWidth = 0;


        for (int i = 0; i < mButtonsCount; i++) {
            View child = getChildAt(i);

            LayoutParams params = (LayoutParams) child.getLayoutParams();

            maxButtonWidth = Math.max(maxButtonWidth, child.getMeasuredWidth() + params.leftMargin + params.rightMargin);
            height += child.getMeasuredHeight();

        }

        width = maxButtonWidth;

        width += getPaddingLeft() + getPaddingRight();

        height += mButtonSpacing * (mButtonsCount - 2) + mAddButtonSpacing;

        height += getPaddingTop() + getPaddingBottom();

        height = adjustForOvershoot(height);


        width = resolveSize(width, widthMeasureSpec);
        height = resolveSize(height, heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    private int adjustForOvershoot(int dimension) {
        return dimension * 12 / 10;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        MarginLayoutParams lp = (MarginLayoutParams) mAddButton.getLayoutParams();
        int addButtonTop = b - t - mAddButton.getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - lp.bottomMargin;
        int addButtonLeft = r - l - getPaddingRight() - mAddButton.getMeasuredWidth() - lp.getMarginEnd();
        mAddButton.layout(addButtonLeft, addButtonTop, addButtonLeft + mAddButton.getMeasuredWidth(), addButtonTop + mAddButton.getMeasuredHeight());

        int nextY = addButtonTop - mAddButtonSpacing;

        for (int i = mButtonsCount - 1; i >= 0; i--) {
            final View child = getChildAt(i);

            if (child == mAddButton) continue;

            LayoutParams params = (LayoutParams) child.getLayoutParams();

            int childX = r - l - getPaddingRight() - child.getMeasuredWidth() - params.rightMargin;
            int childY = nextY - child.getMeasuredHeight();
            child.layout(childX, childY, childX + child.getMeasuredWidth(), childY + child.getMeasuredHeight());

            float collapsedTranslation = addButtonTop - childY;
            float expandedTranslation = 0f;

            child.setTranslationY(mExpanded ? expandedTranslation : collapsedTranslation);
            child.setAlpha(mExpanded ? 1f : 0f);
            child.setEnabled(mExpanded);

            params.mCollapseDir.setFloatValues(expandedTranslation, collapsedTranslation);
            params.mExpandDir.setFloatValues(collapsedTranslation, expandedTranslation);
            params.setAnimationsTarget(child);

            nextY = childY - mButtonSpacing;
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(super.generateDefaultLayoutParams());
    }


    private static Interpolator sExpandInterpolator = new OvershootInterpolator();
    private static Interpolator sCollapseInterpolator = new DecelerateInterpolator(3f);
    private static Interpolator sAlphaExpandInterpolator = new DecelerateInterpolator();

    private class LayoutParams extends ViewGroup.MarginLayoutParams {

        private ObjectAnimator mExpandDir = new ObjectAnimator();
        private ObjectAnimator mExpandAlpha = new ObjectAnimator();
        private ObjectAnimator mCollapseDir = new ObjectAnimator();
        private ObjectAnimator mCollapseAlpha = new ObjectAnimator();
        private boolean animationsSetToPlay;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            init();
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
            init();
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
            init();
        }

        private void init() {
            mExpandDir.setInterpolator(sExpandInterpolator);
            mExpandAlpha.setInterpolator(sAlphaExpandInterpolator);
            mCollapseDir.setInterpolator(sCollapseInterpolator);
            mCollapseAlpha.setInterpolator(sCollapseInterpolator);

            mCollapseAlpha.setProperty(View.ALPHA);
            mCollapseAlpha.setFloatValues(1f, 0f);

            mExpandAlpha.setProperty(View.ALPHA);
            mExpandAlpha.setFloatValues(0f, 1f);

            mCollapseDir.setProperty(View.TRANSLATION_Y);
            mExpandDir.setProperty(View.TRANSLATION_Y);
        }

        public void setAnimationsTarget(View view) {
            if (view == mAddButton) {
                return;
            }
            mCollapseAlpha.setTarget(view);
            mCollapseDir.setTarget(view);
            mExpandAlpha.setTarget(view);
            mExpandDir.setTarget(view);

            // Now that the animations have targets, set them to be played
            if (!animationsSetToPlay) {

                mCollapseAnimation.playTogether(mCollapseDir, mCollapseAlpha);
                mExpandAnimation.playTogether(mExpandDir,mExpandAlpha);
                animationsSetToPlay = true;
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (mExpanded) {
                collapse();
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    public void collapse() {
        collapse(false);
    }

    public void collapseImmediately() {
        collapse(true);
    }


    public void toggle() {
        if (mExpanded) {
            collapse();
        } else {
            expand();
        }
    }

    public void collapse(boolean immediately) {
        if (mExpanded) {
            mExpanded = false;
            mCollapseAnimation.setDuration(immediately ? 0 : ANIMATION_DURATION);
            mCollapseAnimation.start();
            mExpandAnimation.cancel();

            setChildrenEnabled(false);

            if (mListener != null) {
                mListener.onMenuCollapsed();
            }
        }
    }


    public void expand() {
        if (!mExpanded) {
            mExpanded = true;
            mCollapseAnimation.cancel();
            mExpandAnimation.start();

            setChildrenEnabled(true);

            if (mListener != null) {
                mListener.onMenuExpanded();
            }
        }
    }

    public boolean isExpanded() {
        return mExpanded;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        mAddButton.setEnabled(enabled);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.mExpanded = mExpanded;

        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState savedState = (SavedState) state;
            mExpanded = savedState.mExpanded;

            if (mRotatingDrawable != null) {
                mRotatingDrawable.setRotation(mExpanded ? EXPANDED_PLUS_ROTATION : COLLAPSED_PLUS_ROTATION);
            }

            super.onRestoreInstanceState(savedState.getSuperState());
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    private static class RotatingDrawable extends LayerDrawable {
        public RotatingDrawable(Drawable drawable) {
            super(new Drawable[]{drawable});
        }

        private float mRotation;

        @SuppressWarnings("UnusedDeclaration")
        public float getRotation() {
            return mRotation;
        }

        @SuppressWarnings("UnusedDeclaration")
        public void setRotation(float rotation) {
            mRotation = rotation;
            invalidateSelf();
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.save();
            canvas.rotate(mRotation, getBounds().centerX(), getBounds().centerY());
            super.draw(canvas);
            canvas.restore();
        }
    }


    private void setChildrenEnabled(boolean enabled) {
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) != null && getChildAt(i) != mAddButton) {
                getChildAt(i).setEnabled(enabled);
            }
        }
    }

    public static class SavedState extends View.BaseSavedState {
        public boolean mExpanded;

        public SavedState(Parcelable parcel) {
            super(parcel);
        }

        private SavedState(Parcel in) {
            super(in);
            mExpanded = in.readInt() == 1;
        }

        @Override
        public void writeToParcel(@NonNull Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(mExpanded ? 1 : 0);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

}
