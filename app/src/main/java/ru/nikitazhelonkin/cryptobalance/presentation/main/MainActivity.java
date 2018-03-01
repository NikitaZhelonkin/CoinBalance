package ru.nikitazhelonkin.cryptobalance.presentation.main;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.nikitazhelonkin.cryptobalance.App;
import ru.nikitazhelonkin.cryptobalance.R;
import ru.nikitazhelonkin.cryptobalance.data.entity.WalletViewModel;
import ru.nikitazhelonkin.cryptobalance.di.DaggerPresenterComponent;
import ru.nikitazhelonkin.cryptobalance.mvp.MvpActivity;
import ru.nikitazhelonkin.cryptobalance.presentation.add.AddWalletActivity;

public class MainActivity extends MvpActivity<MainPresenter, MainView> implements MainView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_view)
    ProgressBar mProgressBar;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private MainAdapter mMainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mMainAdapter = new MainAdapter());
        mSwipeRefreshLayout.setOnRefreshListener(this);

        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.app_name);
    }

    @Override
    protected MainPresenter onCreatePresenter() {
        return DaggerPresenterComponent.builder()
                .appComponent(App.get(this).getAppComponent())
                .build().mainPresenter();
    }

    @OnClick(R.id.add_fab)
    public void onAddFabClick(View v) {
        getPresenter().onAddClick();
    }

    @Override
    public void setData(List<WalletViewModel> data) {
        mMainAdapter.setData(data);
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
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
    public void onRefresh() {
        getPresenter().onRefresh();
    }

    @Override
    public void navigateToAddWalletView() {
        startActivity(AddWalletActivity.createIntent(this));
    }
}

