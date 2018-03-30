package ru.nikitazhelonkin.coinbalance.presentation.main;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Currency;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.nikitazhelonkin.coinbalance.App;
import ru.nikitazhelonkin.coinbalance.R;
import ru.nikitazhelonkin.coinbalance.data.entity.Exchange;
import ru.nikitazhelonkin.coinbalance.data.entity.MainViewModel;
import ru.nikitazhelonkin.coinbalance.data.entity.Wallet;
import ru.nikitazhelonkin.coinbalance.di.DaggerPresenterComponent;
import ru.nikitazhelonkin.coinbalance.mvp.MvpActivity;
import ru.nikitazhelonkin.coinbalance.presentation.addexchange.AddExchangeActivity;
import ru.nikitazhelonkin.coinbalance.presentation.addwallet.AddWalletActivity;
import ru.nikitazhelonkin.coinbalance.presentation.settings.SettingsActivity;
import ru.nikitazhelonkin.coinbalance.ui.widget.AppBarBehavior;
import ru.nikitazhelonkin.coinbalance.ui.widget.FloatingActionMenu;
import ru.nikitazhelonkin.coinbalance.ui.widget.InputAlertDialogBuilder;
import ru.nikitazhelonkin.coinbalance.ui.widget.PieChartView;
import ru.nikitazhelonkin.coinbalance.ui.widget.TintDrawableTextView;
import ru.nikitazhelonkin.coinbalance.ui.widget.itemtouchhelper.ItemTouchHelperCallback;
import ru.nikitazhelonkin.coinbalance.utils.AndroidUtils;
import ru.nikitazhelonkin.coinbalance.utils.AppNumberFormatter;
import ru.nikitazhelonkin.coinbalance.utils.ChartColorPallet;
import ru.nikitazhelonkin.coinbalance.utils.ListUtils;

public class MainActivity extends MvpActivity<MainPresenter, MainView> implements
        MainView,
        SwipeRefreshLayout.OnRefreshListener,
        MainAdapter.Callback {

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
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
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.appbar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.total_balance)
    TextView mTotalBalance;
    @BindView(R.id.total_balance_chart)
    TextView mTotalBalanceChart;
    @BindView(R.id.profit_loss)
    TintDrawableTextView mProfitLoss;
    @BindView(R.id.profit_loss_chart)
    TintDrawableTextView mProfitLossChart;
    @BindView(R.id.profit_loss_layout)
    View mProfitLossLayout;
    @BindView(R.id.add_fam)
    FloatingActionMenu mFam;
    @BindView(R.id.action_mode)
    ImageButton mModeButton;
    @BindView(R.id.chart_layout)
    View mChartLayout;
    @BindView(R.id.chart_view)
    PieChartView mChartView;

    private MainAdapter mMainAdapter;

    private AssetsAdapter mAssetsAdapter;

    private AppBarBehavior mAppBarBehavior;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mAssetsAdapter = new AssetsAdapter();
        mMainAdapter = new MainAdapter();
        mMainAdapter.setCallback(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setHasFixedSize(true);

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(mRecyclerView);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);

        setSupportActionBar(mToolbar);
        mToolbarTitle.setText(R.string.app_name);

        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            float fraction = verticalOffset / (float) (mToolbar.getHeight() - mAppBarLayout.getHeight());
            mChartLayout.setAlpha(1 - fraction);
            mChartLayout.setScaleX(Math.max(0.3f, 1 - fraction));
            mChartLayout.setScaleY(Math.max(0.3f, 1 - fraction));
            mChartLayout.setPivotY(mChartLayout.getHeight());
            mTotalBalance.setAlpha(fraction);
            mProfitLossLayout.setAlpha(fraction);
        });
        mAppBarBehavior = (AppBarBehavior) ((CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams()).getBehavior();
    }

    @Override
    protected MainPresenter onCreatePresenter() {
        return DaggerPresenterComponent.builder()
                .appComponent(App.get(this).getAppComponent())
                .build().mainPresenter();
    }


    @Override
    public void onWalletMenuItemClick(Wallet wallet, int itemId) {
        getPresenter().onWalletMenuItemClick(wallet, itemId);
    }

    @Override
    public void onExchangeMenuItemClick(Exchange exchange, int itemId) {
        getPresenter().onExchangeMenuItemClick(exchange, itemId);
    }

    @Override
    public void onWalletItemClick(Wallet wallet) {
        getPresenter().onWalletItemClick(wallet);
    }

    @Override
    public void onExchangeItemClick(Exchange exchange) {
        getPresenter().onExchangeItemClick(exchange);
    }

    @Override
    public void onStartDragging() {
        //do nothing
    }

    @Override
    public void onStopDragging() {
        getPresenter().updateItemPositions();
    }

    @OnClick(R.id.action_mode)
    public void onModeClick(View v) {
        getPresenter().onModeClick();
    }

    @OnClick(R.id.action_settings)
    public void onSettingsClick(View v) {
        getPresenter().onSettingsClick();
    }

    @OnClick(R.id.add_wallet_fab)
    public void onAddWalletFabClick(View v) {
        mFam.collapse();
        getPresenter().onAddWalletClick();
    }

    @OnClick(R.id.add_exchange_fab)
    public void onAddExchangeFabClick(View v) {
        mFam.collapse();
        getPresenter().onAddExchangeClick();
    }

    @Override
    public void onRefresh() {
        getPresenter().onRefresh();
    }

    @Override
    public void setData(MainViewModel data) {
        mMainAdapter.setData(data);
        mAssetsAdapter.setData(data.getAssets());
        mChartView.setData(ListUtils.map(data.getAssets(), (i, asset) ->
                new PieChartView.PieEntry(asset.getCoin(), asset.getCurrencyBalance(), ChartColorPallet.colorForPosition(i))));
    }

    @Override
    public void setMode(int mode, boolean animate) {
        if (mode == MainPresenter.MODE_MAIN) {
            mAppBarBehavior.setDragEnabled(false);
            mModeButton.setImageResource(R.drawable.ic_pie_chart_24dp);
            mAppBarLayout.setExpanded(false, animate);
            mRecyclerView.setAdapter(mMainAdapter);
        } else {
            mAppBarBehavior.setDragEnabled(true);
            mModeButton.setImageResource(R.drawable.ic_view_list_24dp);
            mAppBarLayout.setExpanded(true, animate);
            mRecyclerView.setAdapter(mAssetsAdapter);
        }
        mRecyclerView.stopNestedScroll();
        mRecyclerView.stopScroll();
    }

    @Override
    public void setTotalBalance(String currencyStr, float totalBalance) {
        Currency currency = Currency.getInstance(currencyStr);
        String currencyBalanceStr = AppNumberFormatter.format(totalBalance);
        mTotalBalance.setText(String.format(Locale.US, "%s %s", currency.getSymbol(), currencyBalanceStr));
        mTotalBalanceChart.setText(String.format(Locale.US, "%s %s", currency.getSymbol(), currencyBalanceStr));
    }

    @Override
    public void setProfitLoss(float pl) {
        setProfitLoss(mProfitLoss, pl);
        setProfitLoss(mProfitLossChart, pl);
    }

    private void setProfitLoss(TintDrawableTextView textView, float pl){
        int trendColor = pl > 0 ? R.color.color_trend_up : pl < 0 ? R.color.color_trend_down : android.R.color.black;
        int trendIcon = pl > 0 ? R.drawable.ic_trending_up_24dp : R.drawable.ic_trending_down_24dp;
        textView.setText(String.format(Locale.US, "%.1f %%", Math.abs(pl)));
        textView.setCompoundDrawableTint(ContextCompat.getColor(this, trendColor));
        textView.setCompoundDrawablesWithIntrinsicBounds(trendIcon, 0, 0, 0);
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
    public void showDeleteView(Exchange exchange) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.dialog_delete_exchange_message)
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> getPresenter().deleteExchange(exchange))
                .setNegativeButton(R.string.cancel, null)
                .create()
                .show();
    }

    @Override
    public void showEditTitleView(Exchange exchange) {
        new InputAlertDialogBuilder(this)
                .input(null, exchange.getTitle(), (dialog, text) ->
                        getPresenter().editExchangeTitle(exchange, text.toString()))
                .softInputVisible(true)
                .setPositiveButton(R.string.ok, null)
                .setNegativeButton(R.string.cancel, null)
                .setTitle(R.string.exchange_title)
                .create()
                .show();
    }

    @Override
    public void navigateToAddWalletView() {
        startActivity(AddWalletActivity.createIntent(this));
    }

    @Override
    public void navigateToAddExchangeView() {
        startActivity(AddExchangeActivity.createIntent(this));
    }

    @Override
    public void navigateToSettingsView() {
        startActivity(SettingsActivity.createIntent(this));
    }

    @Override
    public void navigateToMarket() {
        AndroidUtils.openMarket(this);
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

    @Override
    public void showRateDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_rate_title)
                .setMessage(R.string.dialog_rate_message)
                .setPositiveButton(R.string.dialog_rate_ok, (dialog, which) -> {
                    getPresenter().onRateClick();
                })
                .setNegativeButton(R.string.dialog_rate_later, null)
                .create().show();
    }
}

