package ru.nikitazhelonkin.coinbalance.presentation.donation;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;

import java.util.List;

import javax.inject.Inject;

import ru.nikitazhelonkin.coinbalance.R;
import ru.nikitazhelonkin.coinbalance.data.billing.BillingConstants;
import ru.nikitazhelonkin.coinbalance.data.billing.BillingManager;
import ru.nikitazhelonkin.coinbalance.mvp.MvpBasePresenter;
import ru.nikitazhelonkin.coinbalance.utils.L;

public class DonatePresenter extends MvpBasePresenter<DonateView> implements
        BillingManager.BillingUpdatesListener {

    private BillingManager mBillingManager;

    @Inject
    public DonatePresenter(){

    }

    @Override
    public void onAttach(DonateView view, @Nullable Bundle savedInstanceState) {
        super.onAttach(view, savedInstanceState);
        mBillingManager = new BillingManager((Activity) view.getContext(), BillingConstants.BASE_64_ENCODED_PUBLIC_KEY, this);
        mBillingManager.setup();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mBillingManager != null) {
            mBillingManager.destroy();
        }
    }

    public void donate1Dollar() {
        if (mBillingManager == null) return;
        mBillingManager.initiatePurchaseFlow(BillingConstants.SKU_DONATE_1, BillingClient.SkuType.INAPP);
    }

    public void donate2Dollar() {
        if (mBillingManager == null) return;
        mBillingManager.initiatePurchaseFlow(BillingConstants.SKU_DONATE_2, BillingClient.SkuType.INAPP);
    }

    public void donate5Dollar() {
        if (mBillingManager == null) return;
        mBillingManager.initiatePurchaseFlow(BillingConstants.SKU_DONATE_5, BillingClient.SkuType.INAPP);
    }

    @Override
    public void onBillingClientSetupFinished() {
        //do nothing
    }

    @Override
    public void onConsumeFinished(String token, int result) {
        L.e("Consumption finished. Purchase token: " + token + ", result: " + result);

        if (result == BillingClient.BillingResponse.OK) {
            L.e("Consumption successful. Provisioning.");
            getView().showMessage(R.string.purchase_success);
            getView().exit();
        } else {
            getView().showMessage(R.string.purchase_consume_error);
        }
    }

    @Override
    public void onPurchasesUpdated(List<Purchase> purchases) {
        for (Purchase purchase : purchases) {
            switch (purchase.getSku()) {
                case BillingConstants.SKU_DONATE_1:
                case BillingConstants.SKU_DONATE_2:
                case BillingConstants.SKU_DONATE_5:
                    L.e("Item purchased. Consuming it.");
                    mBillingManager.consumeAsync(purchase.getPurchaseToken());
                    break;
            }
        }
    }
}
