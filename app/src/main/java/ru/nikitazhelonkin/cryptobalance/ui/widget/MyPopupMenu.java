package ru.nikitazhelonkin.cryptobalance.ui.widget;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

@SuppressLint("RestrictedApi")
public class MyPopupMenu {

    private final Context mContext;
    private final MenuBuilder mMenu;
    private final MenuPopupHelper mPopup;

    private OnMenuItemClickListener mMenuItemClickListener;
    private OnDismissListener mOnDismissListener;

    public MyPopupMenu(Context context, View anchor) {
        this(context, anchor, Gravity.NO_GRAVITY);
    }

    public MyPopupMenu(Context context, View anchor, int gravity) {
        this(context, anchor, gravity, android.support.v7.appcompat.R.attr.popupMenuStyle, 0);
    }

    @SuppressLint("RestrictedApi")
    public MyPopupMenu(Context context, View anchor, int gravity, int popupStyleAttr, int popupStyleRes) {
        mContext = context;
        mMenu = new MenuBuilder(context);
        mMenu.setCallback(new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                if (mMenuItemClickListener != null) {
                    return mMenuItemClickListener.onMenuItemClick(item);
                }
                return false;
            }

            @Override
            public void onMenuModeChange(MenuBuilder menu) {
            }
        });
        mPopup = new MenuPopupHelper(context, mMenu, anchor, false, popupStyleAttr, popupStyleRes);
        mPopup.setGravity(gravity);
        mPopup.setOnDismissListener(() -> {
            if (mOnDismissListener != null) {
                mOnDismissListener.onDismiss(MyPopupMenu.this);
            }
        });
    }


    public void setForceShowIcon(boolean showIcon){
        mPopup.setForceShowIcon(showIcon);
    }

    public void setGravity(int gravity) {
        mPopup.setGravity(gravity);
    }

    public int getGravity() {
        return mPopup.getGravity();
    }

    @NonNull
    public Menu getMenu() {
        return mMenu;
    }

    @NonNull
    public MenuInflater getMenuInflater() {
        return new SupportMenuInflater(mContext);
    }

    public void inflate(@MenuRes int menuRes) {
        getMenuInflater().inflate(menuRes, mMenu);
    }

    public void show() {
        mPopup.show();
    }

    public void dismiss() {
        mPopup.dismiss();
    }

    public void setOnMenuItemClickListener(@Nullable OnMenuItemClickListener listener) {
        mMenuItemClickListener = listener;
    }

    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        mOnDismissListener = listener;
    }

    public interface OnMenuItemClickListener {
        boolean onMenuItemClick(MenuItem item);
    }

    public interface OnDismissListener {
        void onDismiss(MyPopupMenu menu);
    }

}
