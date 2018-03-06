package ru.nikitazhelonkin.coinbalance.data.validator;


import java.util.regex.Pattern;

public class BCHAddressValidator implements AddressValidator {

    private static final String BCH_LEGACY_REGEX = "^[13][a-km-zA-HJ-NP-Z0-9]{26,33}$";

    @Override
    public boolean isValid(String address) {
        return Pattern.compile(BCH_LEGACY_REGEX).matcher(address).matches();
    }
}
