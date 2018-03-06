package ru.nikitazhelonkin.cryptobalance.data.validator;


import java.util.regex.Pattern;

public class BCHAddressValidator implements AddressValidator {

    private static final String BCH_LEGACY_REGEX = "^[13][a-km-zA-HJ-NP-Z1-9]{33}$";
    private static final String BCH_REGEX = "^(bitcoincash:)?[0-9a-zA-Z]{42}$";

    @Override
    public boolean isValid(String address) {
        return Pattern.compile(BCH_LEGACY_REGEX).matcher(address).matches() ||
                Pattern.compile(BCH_REGEX).matcher(address).matches();
    }
}
