package ru.nikitazhelonkin.cryptobalance.presentation.main;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.nikitazhelonkin.cryptobalance.App;
import ru.nikitazhelonkin.cryptobalance.R;
import ru.nikitazhelonkin.cryptobalance.data.entity.MainViewModel;
import ru.nikitazhelonkin.cryptobalance.data.entity.Wallet;
import ru.nikitazhelonkin.cryptobalance.di.DaggerPresenterComponent;
import ru.nikitazhelonkin.cryptobalance.mvp.MvpActivity;
import ru.nikitazhelonkin.cryptobalance.presentation.add.AddWalletActivity;
import ru.nikitazhelonkin.cryptobalance.ui.widget.InputAlertDialogBuilder;

public class MainActivity extends MvpActivity<MainPresenter, MainView> implements
        MainView,
        SwipeRefreshLayout.OnRefreshListener,
        MainAdapter.Callback {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_view)
    View mEmptyView;
    @BindView(R.id.progress_view)
    ProgressBar mProgressBar;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.appbar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.total_balance)
    TextView mTotalBalance;

    private MainAdapter mMainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mMainAdapter = new MainAdapter());
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setHasFixedSize(true);
        mMainAdapter.setCallback(this);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setIcon(R.drawable.ic_wallet_24dp);
        }
        mToolbarTitle.setText(R.string.app_name);

        setTotalBalance("USD", 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            getPresenter().onSettingsClick();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected MainPresenter onCreatePresenter() {
        return DaggerPresenterComponent.builder()
                .appComponent(App.get(this).getAppComponent())
                .build().mainPresenter();
    }


    @Override
    public void onMenuItemClick(Wallet wallet, int itemId) {
        getPresenter().onMenuItemClick(wallet, itemId);
    }

    @OnClick(R.id.add_fab)
    public void onAddFabClick(View v) {
        getPresenter().onAddClick();
    }

    @Override
    public void onRefresh() {
        getPresenter().onRefresh();
    }

    @Override
    public void setData(MainViewModel data) {
        mMainAdapter.setData(data);
        setTotalBalance(data.getCurrency(), data.getTotalBalance());
    }

    @Override
    public void showError(int errorResId) {
        showSnackBar(getString(errorResId));
    }

    @Override
    public void showMessage(int messageResId) {
        showSnackBar(getString(messageResId));
    }

    @Override
    public void showLoader() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoader() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showQRCodeView(Wallet wallet) {
        QRCodeBottomSheetFragment.create(wallet).show(getSupportFragmentManager(), "qr_code");
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
                .setMessage(R.string.dialog_delete_wallet_message)
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> getPresenter().deleteWallet(wallet))
                .setNegativeButton(R.string.cancel, null)
                .create()
                .show();
    }


    @Override
    public void navigateToAddWalletView() {
        startActivity(AddWalletActivity.createIntent(this));
    }

    @Override
    public void navigateToSettingsView() {
        Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show();
    }

    private void setTotalBalance(String currencyStr, float totalBalance) {
        Currency currency = Currency.getInstance(currencyStr);
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
        format.setCurrency(currency);

        String currencyBalanceStr = format.format(totalBalance)
                .replace(currency.getSymbol(), "");

        mTotalBalance.setText(String.format(Locale.US, "%s %s", currencyBalanceStr, currency.getSymbol()));
    }

    private void showSnackBar(String text) {
        Snackbar snackbar = Snackbar.make(mToolbar, text, Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }

    @Override
    public void setEmptyViewVisible(boolean visible) {
        mEmptyView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}

