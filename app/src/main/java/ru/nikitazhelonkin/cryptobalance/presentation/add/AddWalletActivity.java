package ru.nikitazhelonkin.cryptobalance.presentation.add;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.nikitazhelonkin.cryptobalance.App;
import ru.nikitazhelonkin.cryptobalance.R;
import ru.nikitazhelonkin.cryptobalance.data.entity.Coin;
import ru.nikitazhelonkin.cryptobalance.data.entity.Wallet;
import ru.nikitazhelonkin.cryptobalance.di.DaggerPresenterComponent;
import ru.nikitazhelonkin.cryptobalance.mvp.MvpActivity;
import ru.nikitazhelonkin.cryptobalance.utils.AndroidUtils;

public class AddWalletActivity extends MvpActivity<AddWalletPresenter, AddWalletView> implements
        AddWalletView,
        AdapterView.OnItemSelectedListener,
        TextWatcher {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.coin_spinner)
    AppCompatSpinner mCoinSpinner;
    @BindView(R.id.wallet_address_input)
    EditText mWalletAddressInput;
    @BindView(R.id.address_clear_btn)
    View mAddressClearButton;
    @BindView(R.id.alias_input)
    EditText mAliasInput;
    @BindView(R.id.alis_clear_btn)
    View mAliasClearButton;
    @BindView(R.id.qr_code_btn)
    View mQRCodeButton;


    private CoinSpinnerAdapter mCoinSpinnerAdapter;


    public static Intent createIntent(Context context) {
        return new Intent(context, AddWalletActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wallet);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle(R.string.title_add_wallet);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());

        mCoinSpinnerAdapter = new CoinSpinnerAdapter();
        mCoinSpinner.setAdapter(mCoinSpinnerAdapter);
        mCoinSpinner.setOnItemSelectedListener(this);
        mWalletAddressInput.addTextChangedListener(this);
        mAliasInput.addTextChangedListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                mWalletAddressInput.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void setupCoins(List<Coin> coinList) {
        mCoinSpinnerAdapter.setCoinList(coinList);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        clearInputs();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //do nothing
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        invalidateInput();
    }

    @Override
    public void afterTextChanged(Editable editable) {
        //do nothing
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //do nothing
    }

    @OnClick(R.id.submit_btn)
    public void onSubmitClick(View v) {
        String coin = mCoinSpinnerAdapter.getItem(mCoinSpinner.getSelectedItemPosition()).getTicker();
        AndroidUtils.hideKeyboard(this, mWalletAddressInput.getWindowToken());
        Wallet wallet = new Wallet(coin,
                mWalletAddressInput.getText().toString(),
                mAliasInput.getText().toString());
        getPresenter().onSubmitClick(wallet);
    }

    @OnClick(R.id.qr_code_btn)
    public void onQRCodeClick(View v) {
        getPresenter().onQRCodeClick();
    }

    @OnClick(R.id.address_clear_btn)
    public void onAddressClearClick(View v) {
        mWalletAddressInput.setText(null);
    }

    @OnClick(R.id.alis_clear_btn)
    public void onAliasClearClick(View v) {
        mAliasInput.setText(null);
    }

    @Override
    protected AddWalletPresenter onCreatePresenter() {
        return DaggerPresenterComponent.builder()
                .appComponent(App.get(this).getAppComponent())
                .build().addWalletPresenter();
    }

    @Override
    public void showMessage(int messageResId) {
        showSnackBar(getString(messageResId));
    }

    @Override
    public void scanQRCode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setOrientationLocked(false);
        integrator.setPrompt(getString(R.string.scan_qr_code_prompt));
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    @Override
    public void exit() {
        finish();
    }

    private void clearInputs() {
        mWalletAddressInput.setText(null);
        mAliasInput.setText(null);
    }

    private void invalidateInput() {
        mQRCodeButton.setVisibility(mWalletAddressInput.getText().length() == 0 ? View.VISIBLE : View.GONE);
        mAddressClearButton.setVisibility(mWalletAddressInput.getText().length() == 0 ? View.GONE : View.VISIBLE);
        mAliasClearButton.setVisibility(mAliasInput.getText().length() == 0 ? View.GONE : View.VISIBLE);
    }

    private void showSnackBar(String text) {
        Snackbar snackbar = Snackbar.make(mToolbar, text, Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }
}

