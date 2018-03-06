package ru.nikitazhelonkin.coinbalance.presentation.main;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Currency;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.nikitazhelonkin.coinbalance.App;
import ru.nikitazhelonkin.coinbalance.R;
import ru.nikitazhelonkin.coinbalance.data.entity.MainViewModel;
import ru.nikitazhelonkin.coinbalance.data.entity.Wallet;
import ru.nikitazhelonkin.coinbalance.di.DaggerPresenterComponent;
import ru.nikitazhelonkin.coinbalance.mvp.MvpActivity;
import ru.nikitazhelonkin.coinbalance.presentation.add.AddWalletActivity;
import ru.nikitazhelonkin.coinbalance.presentation.settings.SettingsActivity;
import ru.nikitazhelonkin.coinbalance.ui.widget.InputAlertDialogBuilder;
import ru.nikitazhelonkin.coinbalance.ui.widget.itemtouchhelper.ItemTouchHelperCallback;
import ru.nikitazhelonkin.coinbalance.utils.AppNumberFormatter;

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
    @BindView(R.id.error_view)
    View mErrorView;
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

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(mMainAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setIcon(R.drawable.ic_app);
        }
        mToolbarTitle.setText(R.string.app_name);
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

    @Override
    public void onItemClick(Wallet wallet) {
        getPresenter().onItemClick(wallet);
    }

    @Override
    public void onStartDragging() {
        //do nothing
    }

    @Override
    public void onStopDragging() {
        getPresenter().updateItemPositions();
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
    }

    @Override
    public void setTotalBalance(String currencyStr, float totalBalance) {
        Currency currency = Currency.getInstance(currencyStr);
        String currencyBalanceStr = AppNumberFormatter.format(totalBalance);
        mTotalBalance.setText(String.format(Locale.US, "%s %s", currency.getSymbol(), currencyBalanceStr));
    }


    @Override
    public void showError(int errorResId) {
        showSnackBar(getString(errorResId), Snackbar.LENGTH_LONG);
    }

    @Override
    public void showMessage(int messageResId) {
        showSnackBar(getString(messageResId), Snackbar.LENGTH_SHORT);
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
        startActivity(SettingsActivity.createIntent(this));
    }

    private void showSnackBar(String text, int duration) {
        Snackbar snackbar = Snackbar.make(mToolbar, text, duration);
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }

    @Override
    public void setEmptyViewVisible(boolean visible) {
        mEmptyView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setErrorViewVisible(boolean visible) {
        mErrorView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

}

