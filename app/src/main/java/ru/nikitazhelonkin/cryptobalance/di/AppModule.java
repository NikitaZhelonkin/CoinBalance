package ru.nikitazhelonkin.cryptobalance.di;


import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import ru.nikitazhelonkin.cryptobalance.BuildConfig;
import ru.nikitazhelonkin.cryptobalance.data.api.BTCApiService;
import ru.nikitazhelonkin.cryptobalance.data.api.ChainsoApiService;
import ru.nikitazhelonkin.cryptobalance.data.api.ChainzApiService;
import ru.nikitazhelonkin.cryptobalance.data.api.CryptoCompareApiService;
import ru.nikitazhelonkin.cryptobalance.data.api.ETHApiService;
import ru.nikitazhelonkin.cryptobalance.data.api.XRPApiService;
import ru.nikitazhelonkin.cryptobalance.data.api.interceptor.LoggingInterceptor;
import ru.nikitazhelonkin.cryptobalance.data.db.AppDatabase;
import ru.nikitazhelonkin.cryptobalance.data.prefs.Prefs;
import ru.nikitazhelonkin.cryptobalance.data.repository.CoinRepository;
import ru.nikitazhelonkin.cryptobalance.data.repository.WalletRepository;

@Module
public class AppModule {

    private Context mContext;

    public AppModule(Context context) {
        mContext = context.getApplicationContext();
    }

    @Provides
    Context provideContext() {
        return mContext;
    }

    @Provides
    @Singleton
    @NonNull
    ObjectMapper provideObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    @Provides
    @Singleton
    @NonNull
    AppDatabase provideAppDatabase() {
        return Room.databaseBuilder(mContext, AppDatabase.class, "app-database").build();
    }

    @Provides
    @Singleton
    @NonNull
    OkHttpClient provideHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(new LoggingInterceptor());
        }
        return builder.build();
    }

    @Provides
    @Singleton
    @NonNull
    CryptoCompareApiService provideCryptocompareApiService(OkHttpClient httpClient, ObjectMapper objectMapper) {
        return new Retrofit.Builder()
                .baseUrl("https://min-api.cryptocompare.com")
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create(objectMapper)).build()
                .create(CryptoCompareApiService.class);
    }

    @Provides
    @Singleton
    @NonNull
    BTCApiService provideBTCApiService(OkHttpClient httpClient, ObjectMapper objectMapper) {
        return new Retrofit.Builder()
                .baseUrl("https://blockchain.info")
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create(objectMapper)).build()
                .create(BTCApiService.class);
    }

    @Provides
    @Singleton
    @NonNull
    ETHApiService provideETHApiService(OkHttpClient httpClient, ObjectMapper objectMapper) {
        return new Retrofit.Builder()
                .baseUrl("https://api.etherscan.io")
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create(objectMapper)).build()
                .create(ETHApiService.class);
    }

    @Provides
    @Singleton
    @NonNull
    ChainzApiService provideChainzApiService(OkHttpClient httpClient, ObjectMapper objectMapper) {
        return new Retrofit.Builder()
                .baseUrl("https://chainz.cryptoid.info")
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create(objectMapper)).build()
                .create(ChainzApiService.class);
    }

    @Provides
    @Singleton
    @NonNull
    ChainsoApiService provideChainSoApiService(OkHttpClient httpClient, ObjectMapper objectMapper) {
        return new Retrofit.Builder()
                .baseUrl("https://chain.so")
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create(objectMapper)).build()
                .create(ChainsoApiService.class);
    }

    @Provides
    @Singleton
    @NonNull
    XRPApiService provideXRPApiService(OkHttpClient httpClient, ObjectMapper objectMapper) {
        return new Retrofit.Builder()
                .baseUrl("https://data.ripple.com")
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create(objectMapper)).build()
                .create(XRPApiService.class);
    }

    @Provides
    @Singleton
    CoinRepository provideCoinRepository() {
        return new CoinRepository();
    }

    @Provides
    @Singleton
    WalletRepository provideWalletRepository(AppDatabase database) {
        return new WalletRepository(database);
    }

    @Provides
    Prefs providePrefs(Context context){
        return Prefs.get(context);
    }

}
