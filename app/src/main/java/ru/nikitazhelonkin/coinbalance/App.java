package ru.nikitazhelonkin.coinbalance;


import android.app.Application;
import android.content.Context;

import ru.nikitazhelonkin.coinbalance.di.AppComponent;
import ru.nikitazhelonkin.coinbalance.di.AppModule;
import ru.nikitazhelonkin.coinbalance.di.DaggerAppComponent;

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
