package ru.nikitazhelonkin.coinbalance.data.validator;


import java.util.regex.Pattern;

public class DOGEAddressValidator implements AddressValidator {

    private static final String DOGE_REGEX = "^D{1}[5-9A-HJ-NP-U]{1}[1-9A-HJ-NP-Za-km-z]{32}$";

    @Override
    public boolean isValid(String address) {
        return Pattern.compile(DOGE_REGEX).matcher(address).matches();
    }
}
