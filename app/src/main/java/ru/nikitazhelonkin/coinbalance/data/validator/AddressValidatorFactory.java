package ru.nikitazhelonkin.coinbalance.data.validator;


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
        } else if ("ETC".equalsIgnoreCase(coin)) {
            return new ETHAddressValidator();
        } else if ("LTC".equalsIgnoreCase(coin)) {
            return new LTCAddressValidator();
        } else if ("NEO".equalsIgnoreCase(coin)) {
            return new NEOAddressValidator();
        } else if ("XRP".equalsIgnoreCase(coin)) {
            return new XRPAddressValidator();
        }else if("ADA".equalsIgnoreCase(coin)){
            return new CardanoAddressValidator();
        }else if("ZEC".equalsIgnoreCase(coin)){
            return new ZECAddressValidator();
        }else if("DOGE".equalsIgnoreCase(coin)){
            return new DOGEAddressValidator();
        }else if("XEM".equalsIgnoreCase(coin)){
            return new XEMAddressValidator();
        }else if("XLM".equalsIgnoreCase(coin)){
            return new XLMAddressValidator();
        }
        return null;
    }
}
