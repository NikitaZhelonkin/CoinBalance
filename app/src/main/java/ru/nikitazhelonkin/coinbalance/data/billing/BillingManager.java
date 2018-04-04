package ru.nikitazhelonkin.coinbalance.data.billing;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.nikitazhelonkin.coinbalance.utils.L;

public class BillingManager implements PurchasesUpdatedListener {

    private Activity mActivity;

    private boolean mIsServiceConnected;

    private BillingClient mBillingClient;

    private final List<Purchase> mPurchases = new ArrayList<>();

    private Set<String> mTokensToBeConsumed;

    private final String mBase64PublicKey;

    private final BillingUpdatesListener mBillingUpdatesListener;

    public interface BillingUpdatesListener {
        void onBillingClientSetupFinished();

        void onConsumeFinished(String token,@BillingClient.BillingResponse int result);

        void onPurchasesUpdated(List<Purchase> purchases);
    }


    public BillingManager(Activity activity, String publicKey, @NonNull BillingUpdatesListener billingUpdatesListener) {
        mActivity = activity;
        mBase64PublicKey = publicKey;
        mBillingUpdatesListener = billingUpdatesListener;
        mBillingClient = BillingClient.newBuilder(mActivity).setListener(this).build();
    }

    public void setup() {
        startServiceConnection(() -> {
            mBillingUpdatesListener.onBillingClientSetupFinished();
            // IAB is fully set up. Now, let's get an inventory of stuff we own.
            queryPurchases();
        });
    }

    public void destroy() {
        L.e("Destroying the manager.");
        if (mBillingClient != null && mBillingClient.isReady()) {
            mBillingClient.endConnection();
            mBillingClient = null;
        }
    }

    public void initiatePurchaseFlow(final String skuId, final @BillingClient.SkuType String billingType) {
        initiatePurchaseFlow(skuId, null, billingType);
    }

    public void initiatePurchaseFlow(final String skuId, final ArrayList<String> oldSkus,
                                     final @BillingClient.SkuType String billingType) {
        executeServiceRequest(() -> {
            L.e("Launching in-app purchase flow. Replace old SKU? " + (oldSkus != null));
            BillingFlowParams purchaseParams = BillingFlowParams.newBuilder()
                    .setSku(skuId).setType(billingType).setOldSkus(oldSkus).build();
            mBillingClient.launchBillingFlow(mActivity, purchaseParams);
        });
    }

    public void consumeAsync(final String purchaseToken) {
        // If we've already scheduled to consume this token - no action is needed (this could happen
        // if you received the token when querying purchases inside onReceive() and later from
        // onActivityResult()
        if (mTokensToBeConsumed == null) {
            mTokensToBeConsumed = new HashSet<>();
        } else if (mTokensToBeConsumed.contains(purchaseToken)) {
            L.e("Token was already scheduled to be consumed - skipping...");
            return;
        }
        mTokensToBeConsumed.add(purchaseToken);

        // Generating Consume Response listener
        final ConsumeResponseListener onConsumeListener = new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(@BillingClient.BillingResponse int responseCode, String purchaseToken) {
                // If billing service was disconnected, we try to reconnect 1 time
                // (feel free to introduce your retry policy here).
                mBillingUpdatesListener.onConsumeFinished(purchaseToken, responseCode);
            }
        };

        executeServiceRequest(() -> {
            mBillingClient.consumeAsync(purchaseToken, onConsumeListener);
        });
    }

    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
        if (responseCode == BillingClient.BillingResponse.OK && purchases != null) {
            for (Purchase purchase : purchases) {
                handlePurchase(purchase);
            }
            mBillingUpdatesListener.onPurchasesUpdated(mPurchases);
        } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {
            L.e("onPurchasesUpdated() - user cancelled the purchase flow - skipping");
        } else {
            L.e("onPurchasesUpdated() got unknown resultCode: " + responseCode);
        }
    }

    private void handlePurchase(Purchase purchase) {
        if (!verifyValidSignature(purchase.getOriginalJson(), purchase.getSignature())) {
            L.e("Got a purchase: " + purchase + "; but signature is bad. Skipping...");
            return;
        }

        L.e("Got a verified purchase: " + purchase);

        mPurchases.add(purchase);
    }

    public boolean areSubscriptionsSupported() {
        int responseCode = mBillingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS);
        if (responseCode != BillingClient.BillingResponse.OK) {
            L.e("areSubscriptionsSupported() got an error response: " + responseCode);
        }
        return responseCode == BillingClient.BillingResponse.OK;
    }

    public void queryPurchases() {
        executeServiceRequest(() -> {
            L.e("Querying inventory.");
            Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(BillingClient.SkuType.INAPP);
            // If there are subscriptions supported, we add subscription rows as well
            if (areSubscriptionsSupported()) {
                Purchase.PurchasesResult subscriptionResult
                        = mBillingClient.queryPurchases(BillingClient.SkuType.SUBS);
                L.e("Querying subscriptions result code: "
                        + subscriptionResult.getResponseCode()
                        + " res: " + subscriptionResult.getPurchasesList().size());

                if (subscriptionResult.getResponseCode() == BillingClient.BillingResponse.OK) {
                    purchasesResult.getPurchasesList().addAll(
                            subscriptionResult.getPurchasesList());
                } else {
                    L.e("Got an error response trying to query subscription purchases");
                }
            } else if (purchasesResult.getResponseCode() == BillingClient.BillingResponse.OK) {
                L.e("Skipped subscription purchases query since they are not supported");
            } else {
                L.e("queryPurchases() got an error response code: "
                        + purchasesResult.getResponseCode());
            }
            onQueryPurchasesFinished(purchasesResult);
        });
    }

    private void onQueryPurchasesFinished(Purchase.PurchasesResult result) {
        // Have we been disposed of in the meantime? If so, or bad result code, then quit
        if (mBillingClient == null || result.getResponseCode() != BillingClient.BillingResponse.OK) {
            L.e("Billing client was null or result code (" + result.getResponseCode()
                    + ") was bad - quitting");
            return;
        }

        L.e("Query inventory was successful.");

        // Update the UI and purchases inventory with new list of purchases
        mPurchases.clear();
        onPurchasesUpdated(BillingClient.BillingResponse.OK, result.getPurchasesList());
    }

    private void executeServiceRequest(Runnable runnable) {
        if (mIsServiceConnected) {
            runnable.run();
        } else {
            // If billing service was disconnected, we try to reconnect 1 time.
            // (feel free to introduce your retry policy here).
            startServiceConnection(runnable);
        }
    }

    private void startServiceConnection(final Runnable executeOnSuccess) {
        L.e("Starting setup.");
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                L.e("Setup successful.");
                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                    mIsServiceConnected = true;
                    if (executeOnSuccess != null) {
                        executeOnSuccess.run();
                    }
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                mIsServiceConnected = false;
            }
        });
    }

    private boolean verifyValidSignature(String signedData, String signature) {
        try {
            return Security.verifyPurchase(mBase64PublicKey, signedData, signature);
        } catch (IOException e) {
            L.e("Got an exception trying to validate a purchase: " + e);
            return false;
        }
    }
}
