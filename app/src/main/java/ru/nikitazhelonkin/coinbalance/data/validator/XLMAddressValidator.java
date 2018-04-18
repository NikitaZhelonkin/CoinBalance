package ru.nikitazhelonkin.coinbalance.data.validator;


import android.text.TextUtils;

public class XLMAddressValidator  implements AddressValidator{


    @Override
    public boolean isValid(String address) {
        //TODO validation
        return !TextUtils.isEmpty(address);
    }
}
