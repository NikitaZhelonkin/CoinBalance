package ru.nikitazhelonkin.cryptobalance.data;


import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import ru.nikitazhelonkin.cryptobalance.data.prefs.Prefs;

public class AppSettings {

    private static final Subject<Class<?>> ON_CHANGE = PublishSubject.create();

    public Observable<Class<?>> observe() {
        return ON_CHANGE.filter(aClass -> aClass.equals(getClass()));
    }

    private static final String CURRENCY = "currency";

    private Prefs mPrefs;

    public static AppSettings get(Context context) {
        return new AppSettings(Prefs.get(context));
    }

    @Inject
    public AppSettings(Prefs prefs) {
        mPrefs = prefs;
    }

    public String getCurrency() {
        return mPrefs.getString(CURRENCY, "USD");
    }

    public void setCurrency(@NonNull String currency) {
        mPrefs.putString(CURRENCY, currency);
        notifyChange();
    }

    public void notifyChange() {
        ON_CHANGE.onNext(getClass());
    }
}
