package ru.nikitazhelonkin.cryptobalance;


import android.app.Application;
import android.content.Context;

import ru.nikitazhelonkin.cryptobalance.di.AppComponent;
import ru.nikitazhelonkin.cryptobalance.di.AppModule;
import ru.nikitazhelonkin.cryptobalance.di.DaggerAppComponent;

public class App extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
