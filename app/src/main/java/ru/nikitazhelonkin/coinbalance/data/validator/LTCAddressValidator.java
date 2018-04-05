package ru.nikitazhelonkin.coinbalance.data.validator;


import java.util.regex.Pattern;

public class LTCAddressValidator implements AddressValidator {

    private static final String LTC_REGEX = "^[LM3][a-km-zA-HJ-NP-Z1-9]{25,34}$";

    @Override
    public boolean isValid(String address) {
        return Pattern.compile(LTC_REGEX).matcher(address).matches();
    }
}
