package ru.nikitazhelonkin.coinbalance.presentation.settings;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.nikitazhelonkin.coinbalance.App;
import ru.nikitazhelonkin.coinbalance.BuildConfig;
import ru.nikitazhelonkin.coinbalance.Const;
import ru.nikitazhelonkin.coinbalance.R;
import ru.nikitazhelonkin.coinbalance.data.AppSettings;
import ru.nikitazhelonkin.coinbalance.data.prefs.Prefs;
import ru.nikitazhelonkin.coinbalance.di.DaggerPresenterComponent;
import ru.nikitazhelonkin.coinbalance.mvp.MvpFragment;
import ru.nikitazhelonkin.coinbalance.presentation.donation.DonateDialogFragment;
import ru.nikitazhelonkin.coinbalance.ui.adapter.RecyclerItemsAdapter;
import ru.nikitazhelonkin.coinbalance.ui.widget.AlertDialogBuilder;
import ru.nikitazhelonkin.coinbalance.ui.widget.AppToast;
import ru.nikitazhelonkin.coinbalance.utils.AndroidUtils;

public class SettingsFragment extends MvpFragment<SettingsPresenter, SettingsView> implements
        SettingsView,
        SettingsAdapter.OnItemClickListener {

    private static final int RC_FILE = 1;
    private static final int RC_PERMISSIONS = 2;

    private static final String[] CURRENCIES = {"USD", "EUR", "GBP", "INR", "CNY", "JPY", "RUB"};

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private SettingsAdapter mSettingsAdapter;

    private Runnable mExportAction = this::onExportClick;
    private Runnable mImportAction = this::onImportClick;

    private Runnable mAfterPermissionAction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getActivity().setTitle(R.string.settings);
        mToolbar.setNavigationOnClickListener(view1 -> getActivity().onBackPressed());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mSettingsAdapter = new SettingsAdapter());
        mSettingsAdapter.setOnItemClickListener(this);
        updateItems();
    }


    @Override
    protected SettingsPresenter onCreatePresenter() {
        return DaggerPresenterComponent.builder()
                .appComponent(App.get(getContext()).getAppComponent())
                .build().settingsPresenter();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_FILE && resultCode == Activity.RESULT_OK) {
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
                if (mAfterPermissionAction != null)
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
                return;
            case R.id.settings_donation:
                onDonationClick();
        }
    }

    private void onChangeCurrencyClick() {

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.list_item_select_dialog_singlechoice);
        arrayAdapter.addAll(CURRENCIES);

        String currentCurrency = AppSettings.get(getContext()).getCurrency();
        int currentPosition =  arrayAdapter.getPosition( currentCurrency);

        new AlertDialogBuilder(getContext())
                .setSingleChoiceItems(arrayAdapter, currentPosition, null)
                .setPositiveButton(R.string.dialog_ok, (dialog, which) -> {
                    int position = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                    String currency = arrayAdapter.getItem(position);
                    if (currency != null) {
                        AppSettings.get(getContext()).setCurrency(currency);
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
        Prefs.get(getContext()).putBoolean(Const.PREFS_APP_RATED, true);
        AndroidUtils.openMarket(getContext());
    }

    private void onShareClick() {
        AndroidUtils.share(getContext(), getString(R.string.share_text_format,
                getString(R.string.play_store_uri, getContext().getPackageName())));
    }

    private void onReportClick() {
        AndroidUtils.sendEmail(getContext(),
                getString(R.string.report_email),
                getString(R.string.report_subject, BuildConfig.VERSION_NAME,
                        BuildConfig.VERSION_CODE));
    }

    private void onDonationClick() {
        DonateDialogFragment.create().show(getFragmentManager(), "donate");
    }

    private void updateItems() {
        mSettingsAdapter.invalidateItems(getContext(), AppSettings.get(getContext()).getCurrency());
    }

    @Override
    public void showMessage(String message) {
        AppToast.make(getContext(), message, Toast.LENGTH_SHORT).show();
    }


    private boolean requestStoragePermissionWithCheck() {
        if (!isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                new AlertDialogBuilder(getContext())
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

    private void requestStoragePermission() {
        requestPermissions(new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, RC_PERMISSIONS);
    }

    private boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(getContext(), permission)
                == PackageManager.PERMISSION_GRANTED;
    }

}
