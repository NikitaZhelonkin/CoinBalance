package ru.nikitazhelonkin.coinbalance.data.repository;


import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import ru.nikitazhelonkin.coinbalance.data.db.AppDatabase;
import ru.nikitazhelonkin.coinbalance.data.db.TokenDao;
import ru.nikitazhelonkin.coinbalance.data.entity.Token;

public class TokenRepository {

    private TokenDao mDao;

    @Inject
    public TokenRepository(AppDatabase appDatabase) {
        mDao = appDatabase.tokenDao();
    }

    public Single<List<Token>> getTokens() {
        return mDao.getTokens();
    }

    public Completable insert(List<Token> tokenList) {
        return Completable.fromAction(() -> mDao.insert(tokenList));
    }

    public Completable delete(String walletAddress) {
        return Completable.fromAction(() -> mDao.delete(walletAddress));
    }
}
