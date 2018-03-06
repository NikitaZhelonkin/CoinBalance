package ru.nikitazhelonkin.coinbalance.data.validator;


import java.util.regex.Pattern;


public class XRPAddressValidator implements AddressValidator {

    private static final String XRP_REGEX = "^r[0-9a-zA-Z]{26,33}$";

    @Override
    public boolean isValid(String address) {
        return Pattern.compile(XRP_REGEX).matcher(address).matches();
    }
}