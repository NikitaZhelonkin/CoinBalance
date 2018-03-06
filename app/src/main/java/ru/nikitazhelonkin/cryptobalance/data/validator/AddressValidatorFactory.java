package ru.nikitazhelonkin.cryptobalance.data.validator;


import android.support.annotation.Nullable;

public class AddressValidatorFactory {

    @Nullable
    public static AddressValidator forCoin(String coin) {
        if ("BTC".equalsIgnoreCase(coin)) {
            return new BTCAddressValidator();
        } else if ("BCH".equalsIgnoreCase(coin)) {
            return new BCHAddressValidator();
        } else if ("DASH".equalsIgnoreCase(coin)) {
            return new DASHAddressValidator();
        } else if ("ETH".equalsIgnoreCase(coin)) {
            return new ETHAddressValidator();
        } else if ("LTC".equalsIgnoreCase(coin)) {
            return new LTCAddressValidator();
        } else if ("NEO".equalsIgnoreCase(coin)) {
            return new NEOAddressValidator();
        } else if ("XRP".equalsIgnoreCase(coin)) {
            return new XRPAddressValidator();
        }
        return null;
    }
}
