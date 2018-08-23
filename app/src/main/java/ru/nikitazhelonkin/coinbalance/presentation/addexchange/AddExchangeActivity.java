package ru.nikitazhelonkin.coinbalance.presentation.addexchange;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.nikitazhelonkin.coinbalance.App;
import ru.nikitazhelonkin.coinbalance.R;
import ru.nikitazhelonkin.coinbalance.data.entity.Exchange;
import ru.nikitazhelonkin.coinbalance.data.entity.ExchangeService;
import ru.nikitazhelonkin.coinbalance.di.DaggerPresenterComponent;
import ru.nikitazhelonkin.coinbalance.mvp.MvpActivity;
import ru.nikitazhelonkin.coinbalance.ui.widget.AppToast;
import ru.nikitazhelonkin.coinbalance.utils.AndroidUtils;

public class AddExchangeActivity extends MvpActivity<AddExchangePresenter, AddExchangeView> implements
        AddExchangeView,
        AdapterView.OnItemSelectedListener,
        TextWatcher {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.exchange_service_spinner)
    AppCompatSpinner mServiceSpinner;
    @BindView(R.id.api_key_input)
    EditText mApiKeyInput;
    @BindView(R.id.api_key_clear_btn)
    View mApiKeyClearButton;
    @BindView(R.id.api_secret_input)
    EditText mApiSecretInput;
    @BindView(R.id.api_secret_clear_btn)
    View mApiSecretClearButton;
    @BindView(R.id.title_input)
    EditText mTitleInput;
    @BindView(R.id.title_clear_btn)
    View mTitleClearButton;
    @BindView(R.id.submit_btn)
    View mSubmitButton;

    private ServiceSpinnerAdapter mServiceSpinnerAdapter;

    public static Intent createIntent(Context context) {
        return new Intent(context, AddExchangeActivity.class);
    }

    @Override
    protected AddExchangePresenter onCreatePresenter() {
        return DaggerPresenterComponent.builder()
                .appComponent(App.get(this).getAppComponent())
                .build().addExchangePresenter();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exchange);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle(R.string.title_add_exchange);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());

        mServiceSpinnerAdapter = new ServiceSpinnerAdapter();
        mServiceSpinner.setAdapter(mServiceSpinnerAdapter);
        mServiceSpinner.setOnItemSelectedListener(this);
        mApiKeyInput.addTextChangedListener(this);
        mApiSecretInput.addTextChangedListener(this);
        mTitleInput.addTextChangedListener(this);
    }


    @Override
    public void setupServices(List<ExchangeService> serviceList) {
        mServiceSpinnerAdapter.setServiceList(serviceList);
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
        ExchangeService service = mServiceSpinnerAdapter.getItem(mServiceSpinner.getSelectedItemPosition());
        AndroidUtils.hideKeyboard(this, mApiSecretInput.getWindowToken());

        Exchange exchange = new Exchange(service,
                mApiKeyInput.getText().toString(),
                mApiSecretInput.getText().toString(),
                mTitleInput.getText().toString());
        getPresenter().onSubmitClick(exchange);
    }

    @OnClick(R.id.api_key_clear_btn)
    public void onAddressClearClick(View v) {
        mApiKeyInput.setText(null);
    }

    @OnClick(R.id.api_secret_clear_btn)
    public void onAliasClearClick(View v) {
        mApiSecretInput.setText(null);
    }

    @OnClick(R.id.title_clear_btn)
    public void onTitleClearClick(View v) {
        mTitleInput.setText(null);
    }

    @Override
    public void showMessage(int messageResId) {
        AppToast.make(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAgreement(Exchange exchange) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.api_keys_agreement_title)
                .setMessage(R.string.api_keys_agreement_message)
                .setPositiveButton(R.string.agree, (dialogInterface, i) -> getPresenter().onAgreeClick(exchange))
                .setNegativeButton(R.string.disagree, null)
                .create()
                .show();
    }

    @Override
    public void exit() {
        finish();
    }

    @Override
    public void setSubmitEnabled(boolean enabled) {
        mSubmitButton.setEnabled(enabled);
    }

    private void clearInputs() {
        mApiKeyInput.setText(null);
        mApiSecretInput.setText(null);
        mTitleInput.setText(null);
    }

    private void invalidateInput() {
        mApiKeyClearButton.setVisibility(mApiKeyInput.getText().length() == 0 ? View.GONE : View.VISIBLE);
        mApiSecretClearButton.setVisibility(mApiSecretInput.getText().length() == 0 ? View.GONE : View.VISIBLE);
        mTitleClearButton.setVisibility(mTitleInput.getText().length() == 0 ? View.GONE : View.VISIBLE);
    }

}
