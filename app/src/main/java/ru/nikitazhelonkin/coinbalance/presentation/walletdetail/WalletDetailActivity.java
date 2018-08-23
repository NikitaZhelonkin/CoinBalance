package ru.nikitazhelonkin.coinbalance.presentation.walletdetail;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.nikitazhelonkin.coinbalance.App;
import ru.nikitazhelonkin.coinbalance.R;
import ru.nikitazhelonkin.coinbalance.data.entity.Coin;
import ru.nikitazhelonkin.coinbalance.data.entity.Wallet;
import ru.nikitazhelonkin.coinbalance.data.entity.WalletDetailViewModel;
import ru.nikitazhelonkin.coinbalance.di.DaggerWalletDetailComponent;
import ru.nikitazhelonkin.coinbalance.di.WalletDetailModule;
import ru.nikitazhelonkin.coinbalance.mvp.MvpActivity;
import ru.nikitazhelonkin.coinbalance.ui.widget.AppToast;
import ru.nikitazhelonkin.coinbalance.ui.widget.InputAlertDialogBuilder;
import ru.nikitazhelonkin.coinbalance.utils.QRCodeUtils;

public class WalletDetailActivity extends MvpActivity<WalletDetailPresenter, WalletDetailView> implements
        WalletDetailView,
        WalletDetailAdapter.Callback {

    private static final String EXTRA_WALLET_ID = "extra_wallet_id";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar_subtitle)
    TextView mToolbarSubtitle;
    @BindView(R.id.icon)
    ImageView mIcon;
    @BindView(R.id.qr_code_view)
    ImageView mQRCodeView;
    @BindView(R.id.qr_error_view)
    View mQRErrorView;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private WalletDetailAdapter mWalletDetailAdapter;


    public static Intent createIntent(Context context, int walletId) {
        Intent i = new Intent(context, WalletDetailActivity.class);
        i.putExtra(EXTRA_WALLET_ID, walletId);
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_detail);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mWalletDetailAdapter = new WalletDetailAdapter();
        mWalletDetailAdapter.setCallback(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mWalletDetailAdapter);
        mRecyclerView.setHasFixedSize(true);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    @Override
    protected WalletDetailPresenter onCreatePresenter() {
        int walletId = getIntent().getIntExtra(EXTRA_WALLET_ID, -1);
        return DaggerWalletDetailComponent.builder()
                .appComponent(App.get(this).getAppComponent())
                .walletDetailModule(new WalletDetailModule(walletId))
                .build().presenter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wallet_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                getPresenter().onEditClick();
                return true;
            case R.id.action_delete:
                getPresenter().onDeleteClick();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onWalletAddressClick() {
        getPresenter().onWalletAddressClick();
    }

    @Override
    public void showEditNameView(Wallet wallet) {
        new InputAlertDialogBuilder(this)
                .input(null, wallet.getAlias(), (dialog, text) ->
                        getPresenter().editWalletName(wallet, text.toString()))
                .softInputVisible(true)
                .setPositiveButton(R.string.ok, null)
                .setNegativeButton(R.string.cancel, null)
                .setTitle(R.string.wallet_name)
                .create()
                .show();
    }

    @Override
    public void showDeleteView(Wallet wallet) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_delete_wallet_message)
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> getPresenter().deleteWallet(wallet))
                .setNegativeButton(R.string.cancel, null)
                .create()
                .show();
    }

    @Override
    public void showWallet(WalletDetailViewModel walletModel) {
        Wallet wallet = walletModel.getWallet();
        Coin coin = Coin.forTicker(wallet.getCoinTicker());
        if (coin == null) {
            return;
        }
        mToolbarTitle.setText(TextUtils.isEmpty(wallet.getAlias()) ?
                getString(R.string.my_wallet_format, coin.getTitle()) :
                wallet.getAlias());
        mToolbarSubtitle.setText(coin.getTitle());
        mIcon.setImageResource(coin.getIconResId());

        Bitmap bitmap = QRCodeUtils.generateQRCode(wallet.getAddress());
        if (bitmap != null) {
            mQRCodeView.setImageBitmap(bitmap);
        }
        mQRErrorView.setVisibility(bitmap == null ? View.VISIBLE : View.GONE);

        mWalletDetailAdapter.setData(walletModel);

    }

    @Override
    public void showMessage(int messageResId) {
        AppToast.make(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void exit() {
        finish();
    }
}
