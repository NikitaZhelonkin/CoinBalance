package ru.nikitazhelonkin.coinbalance.presentation.main;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Currency;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.nikitazhelonkin.coinbalance.App;
import ru.nikitazhelonkin.coinbalance.BuildConfig;
import ru.nikitazhelonkin.coinbalance.R;
import ru.nikitazhelonkin.coinbalance.data.entity.Exchange;
import ru.nikitazhelonkin.coinbalance.data.entity.MainViewModel;
import ru.nikitazhelonkin.coinbalance.data.entity.Wallet;
import ru.nikitazhelonkin.coinbalance.di.DaggerPresenterComponent;
import ru.nikitazhelonkin.coinbalance.mvp.MvpActivity;
import ru.nikitazhelonkin.coinbalance.presentation.addexchange.AddExchangeActivity;
import ru.nikitazhelonkin.coinbalance.presentation.addwallet.AddWalletActivity;
import ru.nikitazhelonkin.coinbalance.presentation.exchangedetail.ExchangeDetailActivity;
import ru.nikitazhelonkin.coinbalance.presentation.settings.SettingsActivity;
import ru.nikitazhelonkin.coinbalance.presentation.walletdetail.WalletDetailActivity;
import ru.nikitazhelonkin.coinbalance.ui.widget.AlertDialogBuilder;
import ru.nikitazhelonkin.coinbalance.ui.widget.AppBarBehavior;
import ru.nikitazhelonkin.coinbalance.ui.widget.AppToast;
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
    @BindView(R.id.total_balance_chart)
    TextView mTotalBalanceChart;
    @BindView(R.id.profit_loss_chart)
    TintDrawableTextView mProfitLossChart;
    @BindView(R.id.chart_layout)
    View mChartLayout;
    @BindView(R.id.chart_view)
    PieChartView mChartView;
    @BindView(R.id.action_accounts)
    ImageButton mActionAccounts;
    @BindView(R.id.action_overview)
    ImageButton mActionOverview;

    private MainAdapter mMainAdapter;

    private AssetsAdapter mAssetsAdapter;

    private AppBarBehavior mAppBarBehavior;

    private int mMode;

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


        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            float fraction = verticalOffset / (float) (0 - mAppBarLayout.getHeight());
            mChartLayout.setAlpha(1 - fraction);
            mChartLayout.setScaleX(Math.max(0.3f, 1 - fraction));
            mChartLayout.setScaleY(Math.max(0.3f, 1 - fraction));
            mChartLayout.setPivotY(mChartLayout.getHeight());
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
    public void onErrorWalletClick(Wallet wallet) {
        getPresenter().onWalletErrorClick(wallet);
    }

    @Override
    public void onErrorExchangeClick(Exchange exchange) {
        getPresenter().onExchangeErrorClick(exchange);
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
        getPresenter().onStartDragging();
    }

    @Override
    public void onStopDragging() {
        getPresenter().onStopDragging();
    }

    @OnClick(R.id.action_overview)
    public void onOverviewClick(View v) {
        getPresenter().onOverviewClick();
    }

    @OnClick(R.id.action_accounts)
    public void onAccountsClick(View v) {
        getPresenter().onAccountsClick();
    }

    @OnClick(R.id.action_settings)
    public void onSettingsClick(View v) {
        getPresenter().onSettingsClick();
    }

    @OnClick(R.id.add_fab)
    public void onFabClick(View v) {
        getPresenter().onAddClick();
    }

    @Override
    public void onRefresh() {
        getPresenter().onRefresh();
    }

    @Override
    public void setData(MainViewModel data, boolean animate) {
        mMainAdapter.setData(data);
        mAssetsAdapter.setData(data.getAssets());
        mChartView.setData(ListUtils.map(data.getAssets(), (i, asset) ->
                new PieChartView.PieEntry(asset.getCoin(), asset.getCurrencyBalance(), ChartColorPallet.colorForPosition(i))));
        if (animate) {
            mRecyclerView.scheduleLayoutAnimation();
            setMode(mMode, true);
        }
    }

    @Override
    public void setMode(int mode, boolean animate) {
        mMode = mode;
        mActionAccounts.setSelected(false);
        mActionOverview.setSelected(false);
        if (mode == MainPresenter.MODE_PROFILE) {
            mActionAccounts.setSelected(true);
            mAppBarBehavior.setDragEnabled(false);
            mAppBarLayout.setExpanded(false, animate);
            mRecyclerView.setAdapter(mMainAdapter);
        } else {
            mActionOverview.setSelected(true);
            mAppBarBehavior.setDragEnabled(mAssetsAdapter.getItemCount() > 0);
            mAppBarLayout.setExpanded(mAssetsAdapter.getItemCount() > 0, animate);
            mRecyclerView.setAdapter(mAssetsAdapter);
        }
        mRecyclerView.stopNestedScroll();
        mRecyclerView.stopScroll();
    }

    @Override
    public void setTotalBalance(String currencyStr, float totalBalance) {
        Currency currency = Currency.getInstance(currencyStr);
        String currencyBalanceStr = AppNumberFormatter.format(totalBalance);
        mTotalBalanceChart.setText(String.format(Locale.US, "%s %s", currency.getSymbol(), currencyBalanceStr));
    }

    @Override
    public void setProfitLoss(float pl) {
        setProfitLoss(mProfitLossChart, pl);
    }

    private void setProfitLoss(TintDrawableTextView textView, float pl) {
        int trendColor = pl > 0 ? R.color.color_trend_up : pl < 0 ? R.color.color_trend_down : android.R.color.black;
        int trendIcon = pl > 0 ? R.drawable.ic_trending_up_24dp : R.drawable.ic_trending_down_24dp;
        textView.setText(String.format(Locale.US, "%.1f %%", Math.abs(pl)));
        textView.setCompoundDrawableTint(ContextCompat.getColor(this, trendColor));
        textView.setCompoundDrawablesWithIntrinsicBounds(trendIcon, 0, 0, 0);
    }

    @Override
    public void showError(int errorResId) {
        AppToast.make(this, errorResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showWalletError() {
        new AlertDialogBuilder(this)
                .setTitle(R.string.dialog_error)
                .setMessage(R.string.wallet_error_common_issues)
                .setPositiveButton(R.string.ok, null)
                .setNegativeButton(R.string.dialog_report_error, (dialogInterface, i) -> getPresenter().onReportClick())
                .create()
                .show();
    }

    @Override
    public void showExchangeError(String message) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_exchange_error, null);
        ((TextView) view.findViewById(R.id.error_message)).setText(TextUtils.isEmpty(message) ?
                getString(R.string.error_unknown) : message);
        new AlertDialogBuilder(this)
                .setTitle(R.string.dialog_error)
                .setView(view)
                .setPositiveButton(R.string.ok, null)
                .setNegativeButton(R.string.dialog_report_error, (dialogInterface, i) -> getPresenter().onReportClick())
                .create()
                .show();
    }

    @Override
    public void showMessage(int messageResId) {
        AppToast.make(this, messageResId, Toast.LENGTH_SHORT).show();
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
    public void navigateToWalletDetail(Wallet wallet) {
        startActivity(WalletDetailActivity.createIntent(this, wallet.getId()));
    }

    @Override
    public void navigateToExchangeDetail(Exchange exchange) {
        startActivity(ExchangeDetailActivity.createIntent(this, exchange.getId()));
    }

    @Override
    public void navigateToMarket() {
        AndroidUtils.openMarket(this);
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
        new AlertDialogBuilder(this)
                .setTitle(R.string.dialog_rate_title)
                .setMessage(R.string.dialog_rate_message)
                .setPositiveButton(R.string.dialog_rate_ok, (dialog, which) -> {
                    getPresenter().onRateClick();
                })
                .setNegativeButton(R.string.dialog_rate_later, null)
                .create().show();
    }

    @Override
    public void showAddBottomSheetDialog() {
        AddBottomSheetDialogFragment.create().show(getSupportFragmentManager(), "add_bottom_sheet");
    }

    @Override
    public void reportError() {
        AndroidUtils.sendEmail(this,
                getString(R.string.report_email),
                getString(R.string.report_subject, BuildConfig.VERSION_NAME,
                        BuildConfig.VERSION_CODE));
    }

}

