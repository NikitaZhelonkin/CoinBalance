package ru.nikitazhelonkin.coinbalance.presentation.walletdetail;


import ru.nikitazhelonkin.coinbalance.data.entity.Wallet;
import ru.nikitazhelonkin.coinbalance.data.entity.WalletDetailViewModel;
import ru.nikitazhelonkin.coinbalance.mvp.MvpView;

public interface WalletDetailView extends MvpView {

    void showWallet(WalletDetailViewModel wallet);

    void showMessage(int messageResId);

    void showEditNameView(Wallet wallet);

    void showDeleteView(Wallet wallet);

    void exit();
}
