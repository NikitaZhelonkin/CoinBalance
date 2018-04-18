package ru.nikitazhelonkin.coinbalance.data.validator;


import android.text.TextUtils;

public class DOGEAddressValidator implements AddressValidator {

    //legacy format
    private static final String DOGE_REGEX = "^D{1}[5-9A-HJ-NP-U]{1}[1-9A-HJ-NP-Za-km-z]{32}$";

    @Override
    public boolean isValid(String address) {
        return !TextUtils.isEmpty(address);
    }
}
