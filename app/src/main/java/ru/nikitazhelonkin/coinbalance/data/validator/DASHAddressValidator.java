package ru.nikitazhelonkin.coinbalance.data.validator;


import java.util.regex.Pattern;

public class DASHAddressValidator implements AddressValidator {

    private static final String DASH_REGEX = "^X[1-9A-HJ-NP-Za-km-z]{32,34}$";

    @Override
    public boolean isValid(String address) {
        return Pattern.compile(DASH_REGEX).matcher(address).matches();
    }
}
