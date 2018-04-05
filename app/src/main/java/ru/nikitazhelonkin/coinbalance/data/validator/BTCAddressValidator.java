package ru.nikitazhelonkin.coinbalance.data.validator;


import java.util.regex.Pattern;

public class BTCAddressValidator implements AddressValidator {

    private static final String BTC_REGEX = "^[13][a-km-zA-HJ-NP-Z0-9]{25,34}$";

    @Override
    public boolean isValid(String address) {
        return Pattern.compile(BTC_REGEX).matcher(address).matches();
    }
}
