package ru.nikitazhelonkin.coinbalance.di;


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
import ru.nikitazhelonkin.coinbalance.BuildConfig;
import ru.nikitazhelonkin.coinbalance.data.api.interceptor.LoggingInterceptor;
import ru.nikitazhelonkin.coinbalance.data.api.response.DogeApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.CryptoCompareApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.AdaApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.BCHChainApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.BTCApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.ChainsoApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.ChainzApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.ETCApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.ETHApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.EthplorerApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.GasTrackerApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.NEMApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.NeoScanApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.XLMApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.XRPApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.ZChainApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.BinanceApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.BitfinexApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.BittrexApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.KrakenApiService;
import ru.nikitazhelonkin.coinbalance.data.api.service.exchange.PoloniexApiService;
import ru.nikitazhelonkin.coinbalance.data.db.AppDatabase;
import ru.nikitazhelonkin.coinbalance.data.db.migration.Migration1_2;
import ru.nikitazhelonkin.coinbalance.data.prefs.Prefs;

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
        return Room.databaseBuilder(mContext, AppDatabase.class, "app-database")
                .addMigrations(new Migration1_2())
                .build();
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
        return provideApiService("https://min-api.cryptocompare.com", CryptoCompareApiService.class, httpClient, objectMapper);
    }

    @Provides
    @Singleton
    @NonNull
    BTCApiService provideBTCApiService(OkHttpClient httpClient, ObjectMapper objectMapper) {
        return provideApiService("https://blockchain.info", BTCApiService.class, httpClient, objectMapper);
    }

    @Provides
    @Singleton
    @NonNull
    ETHApiService provideETHApiService(OkHttpClient httpClient, ObjectMapper objectMapper) {
        return provideApiService("https://api.etherscan.io", ETHApiService.class, httpClient, objectMapper);
    }

    @Provides
    @Singleton
    @NonNull
    EthplorerApiService provideEthplorerApiService(OkHttpClient httpClient, ObjectMapper objectMapper) {
        return provideApiService("https://api.ethplorer.io", EthplorerApiService.class, httpClient, objectMapper);
    }

    @Provides
    @Singleton
    @NonNull
    ChainzApiService provideChainzApiService(OkHttpClient httpClient, ObjectMapper objectMapper) {
        return provideApiService("https://chainz.cryptoid.info", ChainzApiService.class, httpClient, objectMapper);
    }

    @Provides
    @Singleton
    @NonNull
    ChainsoApiService provideChainSoApiService(OkHttpClient httpClient, ObjectMapper objectMapper) {
        return provideApiService("https://chain.so", ChainsoApiService.class, httpClient, objectMapper);
    }

    @Provides
    @Singleton
    @NonNull
    XRPApiService provideXRPApiService(OkHttpClient httpClient, ObjectMapper objectMapper) {
        return provideApiService("https://data.ripple.com", XRPApiService.class, httpClient, objectMapper);
    }

    @Provides
    @Singleton
    @NonNull
    BCHChainApiService provideBCHChainApiService(OkHttpClient httpClient, ObjectMapper objectMapper) {
        return provideApiService("https://bch-chain.api.btc.com/", BCHChainApiService.class, httpClient, objectMapper);
    }

    @Provides
    @Singleton
    @NonNull
    ETCApiService provideETCApiService(OkHttpClient httpClient, ObjectMapper objectMapper) {
        return provideApiService("https://etcchain.com", ETCApiService.class, httpClient, objectMapper);
    }

    @Provides
    @Singleton
    @NonNull
    GasTrackerApiService provideGasTrackerApiService(OkHttpClient httpClient, ObjectMapper objectMapper) {
        return provideApiService("https://api.gastracker.io", GasTrackerApiService.class, httpClient, objectMapper);
    }

    @Provides
    @Singleton
    @NonNull
    DogeApiService provideDogeApiService(OkHttpClient httpClient, ObjectMapper objectMapper) {
        return provideApiService("https://dogechain.info", DogeApiService.class, httpClient, objectMapper);
    }

    @Provides
    @Singleton
    @NonNull
    NEMApiService provideNemApiService(OkHttpClient httpClient, ObjectMapper objectMapper) {
        return provideApiService("http://chain.nem.ninja/", NEMApiService.class, httpClient, objectMapper);
    }

    @Provides
    @Singleton
    @NonNull
    XLMApiService provideXLMApiService(OkHttpClient httpClient, ObjectMapper objectMapper) {
        return provideApiService("https://horizon.stellar.org", XLMApiService.class, httpClient, objectMapper);
    }

    @Provides
    @Singleton
    @NonNull
    AdaApiService provideAdaApiService(OkHttpClient httpClient, ObjectMapper objectMapper) {
        return provideApiService("https://cardanoexplorer.com", AdaApiService.class, httpClient, objectMapper);
    }

    @Provides
    @Singleton
    @NonNull
    NeoScanApiService provideNeoScanApiService(OkHttpClient httpClient, ObjectMapper objectMapper) {
        return provideApiService("https://neoscan.io", NeoScanApiService.class, httpClient, objectMapper);
    }

    @Provides
    @Singleton
    @NonNull
    ZChainApiService provideZChainApiService(OkHttpClient httpClient, ObjectMapper objectMapper) {
        return provideApiService("https://api.zcha.in", ZChainApiService.class, httpClient, objectMapper);
    }

    @Provides
    @Singleton
    @NonNull
    BitfinexApiService provideBitfinexApiService(OkHttpClient httpClient, ObjectMapper objectMapper){
        return provideApiService("https://api.bitfinex.com", BitfinexApiService.class, httpClient, objectMapper);
    }

    @Provides
    @Singleton
    @NonNull
    BittrexApiService provideBittrexApiService(OkHttpClient httpClient, ObjectMapper objectMapper){
        return provideApiService("https://bittrex.com", BittrexApiService.class, httpClient, objectMapper);
    }

    @Provides
    @Singleton
    @NonNull
    BinanceApiService provideBinanceApiService(OkHttpClient httpClient, ObjectMapper objectMapper){
        return provideApiService("https://api.binance.com", BinanceApiService.class, httpClient, objectMapper);
    }

    @Provides
    @Singleton
    @NonNull
    KrakenApiService provideKrakenApiService(OkHttpClient httpClient, ObjectMapper objectMapper){
        return provideApiService("https://api.kraken.com", KrakenApiService.class, httpClient, objectMapper);
    }

    @Provides
    @Singleton
    @NonNull
    PoloniexApiService providePoloniexApiService(OkHttpClient httpClient, ObjectMapper objectMapper){
        return provideApiService("https://poloniex.com", PoloniexApiService.class, httpClient, objectMapper);
    }




    @Provides
    Prefs providePrefs(Context context){
        return Prefs.get(context);
    }

    private <T> T provideApiService(String url, Class<T> tClass, OkHttpClient httpClient, ObjectMapper objectMapper){
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create(objectMapper)).build()
                .create(tClass);
    }

}
