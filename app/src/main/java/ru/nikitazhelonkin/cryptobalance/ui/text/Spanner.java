package ru.nikitazhelonkin.cryptobalance.ui.text;


import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.MovementMethod;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Spanner {

    private String mText;

    private Spannable mSpannable;


    public static Spanner from(String text){
        return new Spanner(text);
    }

    public static Spanner from(Context context, int textResId){
        return from(context.getString(textResId));
    }

    public static Spanner from(CharSequence charSequence) {
        if (charSequence instanceof Spannable) {
            return new Spanner((Spannable) charSequence);
        } else {
            return new Spanner(charSequence.toString());
        }
    }

    public Spannable getSpannable() {
        return mSpannable;
    }

    public String getText() {
        return mText;
    }

    private Spanner(@NonNull String text){
        mText = text;
        mSpannable = new SpannableString(mText);
    }

    private Spanner(@NonNull Spannable spannable){
        mText = spannable.toString();
        mSpannable = spannable;
    }

    public Spanner styleWord(String word, Object... spans) {
        styleRegex("\\b" + word.toLowerCase() + "\\b", spans);
        return this;
    }

    public Spanner style(String toSpan, Object... spans) {
        applySpans(mSpannable, toSpan, spans);
        return this;
    }

    public Spanner styleRegexBounds(String regex, Object... spans) {
        String text = mText;
        Matcher matcher = Pattern.compile(regex).matcher(text);
        while (matcher.find()) {
            String toSpan = matcher.group(1);
            mText = mText.replace(matcher.group(0), toSpan);
            mSpannable = new SpannableString(mText);
            applySpans(mSpannable, toSpan, spans);
        }
        return this;
    }

    public Spanner styleRegex(String regex, Object... spans) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(mSpannable.toString().toLowerCase());
        while (matcher.find()) {
            int end = matcher.end();
            int start = matcher.start();
            styleRange(start, end, spans);
        }
        return this;
    }

    public Spanner styleRange(int start, int end, Object... spans) {
        applySpans(mSpannable, start, end, spans);
        return this;
    }

    public void applyTo(TextView textView) {
        textView.setText(mSpannable);
    }

    public void applyTo(TextView textView, MovementMethod movementMethod) {
        textView.setText(mSpannable);
        textView.setMovementMethod(movementMethod);
    }

    private static void applySpans(Spannable spannable, String toSpan, Object... spans) {
        int start = spannable.toString().toLowerCase().indexOf(toSpan.toLowerCase());
        if (start == -1) {
            return;
        }
        int end = start + toSpan.length();
        applySpans(spannable, start, end, spans);
    }

    private static void applySpans(Spannable spannable, int start, int end, Object... spans) {
        for (Object span : spans) {
            spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

}

