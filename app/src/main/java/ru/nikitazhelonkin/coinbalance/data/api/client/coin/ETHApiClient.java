package ru.nikitazhelonkin.coinbalance.data.api.client.coin;


import java.util.List;

import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.api.response.EthplorerApiResponse;
import ru.nikitazhelonkin.coinbalance.data.api.service.coin.EthplorerApiService;
import ru.nikitazhelonkin.coinbalance.data.entity.Token;
import ru.nikitazhelonkin.coinbalance.data.entity.WalletBalance;
import ru.nikitazhelonkin.coinbalance.utils.ListUtils;

public class ETHApiClient implements ApiClient {

    private EthplorerApiService mApiService;

    public ETHApiClient(EthplorerApiService apiService) {
        mApiService = apiService;
    }

    @Override
    public Single<WalletBalance> getBalance(String address) {
        return mApiService.balance(address, "freekey")
                .map(ethResponse -> new WalletBalance(ethResponse.ETH.balance, mapTokenList(address, ethResponse.tokens)));
    }

    private List<Token> mapTokenList(String address, List<EthplorerApiResponse.Token> tokenList) {
        return ListUtils.map(tokenList, (i, t) ->
                new Token(0, address, t.tokenInfo.symbol, t.tokenInfo.name, formatBalance(t.balance, t.tokenInfo.decimals)));
    }

    private float formatBalance(String balance, int decimals) {
        return (float) (Double.parseDouble(balance) / Math.pow(10, decimals));
    }

}
