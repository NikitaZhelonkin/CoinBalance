package ru.nikitazhelonkin.coinbalance;


import android.app.Application;
import android.content.Context;

import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

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
        YandexMetrica.activate(getApplicationContext(),
                YandexMetricaConfig.newConfigBuilder(BuildConfig.YANDEX_METRICA_API_KEY).build());
        YandexMetrica.enableActivityAutoTracking(this);
    }

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
