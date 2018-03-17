package ru.nikitazhelonkin.coinbalance.data.api.response;


import java.util.List;

public class EthplorerApiResponse {

    public Balance ETH;

    public List<Token> tokens;

    @Override
    public String toString() {
        return "{" +
                "ETH=" + ETH +
                ", tokens=" + tokens +
                '}';
    }

    public static class Balance {
        public String balance;

        @Override
        public String toString() {
            return "{" +
                    "balance='" + balance + '\'' +
                    '}';
        }
    }

    public static class Token {
        public TokenInfo tokenInfo;
        public String balance;

        @Override
        public String toString() {
            return "{" +
                    "tokenInfo=" + tokenInfo +
                    ", balance='" + balance + '\'' +
                    '}';
        }
    }

    public static class TokenInfo {
        public String symbol;
        public String name;

        @Override
        public String toString() {
            return "{" +
                    "symbol='" + symbol + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }


}
