package ru.nikitazhelonkin.coinbalance.presentation.settings;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.nikitazhelonkin.coinbalance.App;
import ru.nikitazhelonkin.coinbalance.BuildConfig;
import ru.nikitazhelonkin.coinbalance.R;
import ru.nikitazhelonkin.coinbalance.data.AppSettings;
import ru.nikitazhelonkin.coinbalance.data.system.ClipboardManager;
import ru.nikitazhelonkin.coinbalance.di.DaggerPresenterComponent;
import ru.nikitazhelonkin.coinbalance.mvp.MvpActivity;
import ru.nikitazhelonkin.coinbalance.ui.adapter.RecyclerItemsAdapter;
import ru.nikitazhelonkin.coinbalance.utils.AndroidUtils;

public class SettingsActivity extends MvpActivity<SettingsPresenter, SettingsView> implements
        SettingsView,
        SettingsAdapter.OnItemClickListener,
        SettingsAdapter.Callback {

    private static final int RC_FILE = 1;
    private static final int RC_PERMISSIONS = 2;

    private static final String[] CURRENCIES = {"USD", "EUR", "GBP", "INR", "CNY", "JPY", "RUB"};

    public static Intent createIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private SettingsAdapter mSettingsAdapter;

    private Runnable mExportAction = this::onExportClick;
    private Runnable mImportAction = this::onImportClick;

    private Runnable mAfterPermissionAction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle(R.string.settings);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mSettingsAdapter = new SettingsAdapter());
        mSettingsAdapter.setOnItemClickListener(this);
        mSettingsAdapter.setCallback(this);
        updateItems();
    }

    @Override
    protected SettingsPresenter onCreatePresenter() {
        return DaggerPresenterComponent.builder()
                .appComponent(App.get(this).getAppComponent())
                .build().settingsPresenter();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_FILE && resultCode == RESULT_OK) {
            getPresenter().importConfig(data.getData());
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(mAfterPermissionAction!=null)
                    mAfterPermissionAction.run();
            }
        }
    }

    @Override
    public void onItemClick(RecyclerItemsAdapter.IListItem iListItem) {
        switch (iListItem.getId()) {
            case R.id.settings_currency:
                onChangeCurrencyClick();
                return;
            case R.id.settings_import:
                onImportClick();
                return;
            case R.id.settings_export:
                onExportClick();
                return;
            case R.id.settings_rate:
                onRateClick();
                return;
            case R.id.settings_share:
                onShareClick();
                return;
            case R.id.settings_report:
                onReportClick();
        }
    }

    private void onChangeCurrencyClick() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item);
        arrayAdapter.addAll(CURRENCIES);
        new AlertDialog.Builder(this)
                .setAdapter(arrayAdapter, (dialogInterface, i) -> {
                    String currency = arrayAdapter.getItem(i);
                    if (currency != null) {
                        AppSettings.get(SettingsActivity.this).setCurrency(currency);
                        updateItems();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create()
                .show();
    }

    private void onImportClick() {
        mAfterPermissionAction = mImportAction;
        if (!requestStoragePermissionWithCheck()) {
            return;
        }
        AndroidUtils.showFileChooser(this, getString(R.string.file_chooser_title), RC_FILE);
    }

    private void onExportClick() {
        mAfterPermissionAction = mExportAction;
        if (!requestStoragePermissionWithCheck()) {
            return;
        }
        getPresenter().exportConfig();
    }

    private void onRateClick() {
        AndroidUtils.openMarket(this);
    }

    private void onShareClick() {
        AndroidUtils.share(this, getString(R.string.share_text_format,
                getString(R.string.play_store_uri, getPackageName())));
    }

    private void onReportClick() {
        AndroidUtils.sendEmail(this,
                getString(R.string.report_email),
                getString(R.string.report_subject, BuildConfig.VERSION_NAME,
                        BuildConfig.VERSION_CODE));
    }

    @Override
    public void onAddressClick(String address) {
        ClipboardManager clipboardManager = new ClipboardManager(this);
        clipboardManager.copyToClipboard(address);
        Toast.makeText(this, R.string.address_copied, Toast.LENGTH_SHORT).show();
    }

    private void showTODO() {
        Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show();
    }

    private void updateItems() {
        mSettingsAdapter.invalidateItems(this, AppSettings.get(this).getCurrency());
    }

    @Override
    public void showMessage(String message) {
        showSnackBar(message, Toast.LENGTH_SHORT);
    }

    private void showSnackBar(String text, int duration) {
        Snackbar snackbar = Snackbar.make(mToolbar, text, duration);
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }

    private boolean requestStoragePermissionWithCheck() {
        if (!isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            if (shouldShowPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(this)
                        .setMessage(R.string.permission_storage_rationale)
                        .setPositiveButton(R.string.ok, (dialog, which) -> requestStoragePermission())
                        .setNegativeButton(R.string.cancel, null)
                        .create()
                        .show();
            } else {
                requestStoragePermission();
            }
            return false;
        } else {
            return true;
        }
    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, RC_PERMISSIONS);
    }

    private boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(this, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    private boolean shouldShowPermissionRationale(String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
    }
}
