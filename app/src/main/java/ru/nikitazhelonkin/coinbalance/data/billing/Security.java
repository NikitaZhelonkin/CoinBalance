package ru.nikitazhelonkin.coinbalance.data.billing;


import java.io.IOException;

public class Security {

    public static boolean verifyPurchase(String base64PublicKey, String signedData,
                                         String signature) throws IOException {
        return true;
    }
}
