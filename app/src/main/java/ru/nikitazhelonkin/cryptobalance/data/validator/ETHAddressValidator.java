package ru.nikitazhelonkin.cryptobalance.data.validator;


import java.util.regex.Pattern;

public class ETHAddressValidator implements AddressValidator {

    private static final String ETH_REGEX = "^0x[a-fA-F0-9]{40}$";

    @Override
    public boolean isValid(String address) {
        return Pattern.compile(ETH_REGEX).matcher(address).matches();
    }
}
