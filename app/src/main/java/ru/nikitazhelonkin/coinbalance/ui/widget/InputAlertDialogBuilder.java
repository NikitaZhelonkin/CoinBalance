package ru.nikitazhelonkin.coinbalance.ui.widget;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import ru.nikitazhelonkin.coinbalance.R;

public class InputAlertDialogBuilder extends AlertDialogBuilder {

    public interface InputCallback{
        void onInput(DialogInterface dialog, CharSequence text);
    }

    private boolean mSoftInputVisible = false;

    private EditText mInput;

    private DialogInterface.OnClickListener mOnPositiveClickListener;

    private InputCallback mInputCallback;

    public InputAlertDialogBuilder(Context context) {
        super(context);
    }

    public InputAlertDialogBuilder(Context context, int theme) {
        super(context, theme);
    }

    @SuppressLint("InflateParams")
    public InputAlertDialogBuilder input(String hint, String text, InputCallback inputCallback) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_input, null);
        mInput = (EditText) v.findViewById(android.R.id.edit);
        mInput.setHint(hint);
        mInput.setText(text);
        mInput.setSelection(text != null ? text.length() : 0);
        mInputCallback = inputCallback;
        setView(v);
        return this;
    }

    public InputAlertDialogBuilder softInputVisible(boolean softInputVisible) {
        mSoftInputVisible = softInputVisible;
        return this;
    }

    @Override
    public AlertDialog.Builder setPositiveButton(CharSequence text, DialogInterface.OnClickListener listener) {
        mOnPositiveClickListener = listener;
        return super.setPositiveButton(text, mInternalOnPositiveClickListener);
    }

    @Override
    public AlertDialog.Builder setPositiveButton(int textId, DialogInterface.OnClickListener listener) {
        return setPositiveButton(getContext().getText(textId), listener);
    }

    @Override
    public AlertDialog create() {
        AlertDialog dialog = super.create();
        if (mSoftInputVisible) {
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
        return dialog;
    }

    private DialogInterface.OnClickListener mInternalOnPositiveClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (mOnPositiveClickListener != null) {
                mOnPositiveClickListener.onClick(dialog, which);
            }
            if (mInputCallback != null) {
                mInputCallback.onInput(dialog, mInput.getText());
            }
        }
    };


}
