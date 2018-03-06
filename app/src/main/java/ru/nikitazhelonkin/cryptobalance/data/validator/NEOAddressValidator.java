package ru.nikitazhelonkin.cryptobalance.data.validator;


import java.util.regex.Pattern;

public class NEOAddressValidator implements AddressValidator {

    private static final String NEO_REGEX = "^A[0-9a-zA-Z]{33}$";

    @Override
    public boolean isValid(String address) {
        return Pattern.compile(NEO_REGEX).matcher(address).matches();
    }
}
