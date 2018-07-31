package ru.nikitazhelonkin.coinbalance.presentation.exchangedetail;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.nikitazhelonkin.coinbalance.App;
import ru.nikitazhelonkin.coinbalance.R;
import ru.nikitazhelonkin.coinbalance.data.entity.Exchange;
import ru.nikitazhelonkin.coinbalance.data.entity.ExchangeDetailViewModel;
import ru.nikitazhelonkin.coinbalance.di.DaggerExchangeDetailComponent;
import ru.nikitazhelonkin.coinbalance.di.ExchangeDetailModule;
import ru.nikitazhelonkin.coinbalance.mvp.MvpActivity;
import ru.nikitazhelonkin.coinbalance.ui.widget.InputAlertDialogBuilder;

public class ExchangeDetailActivity extends MvpActivity<ExchangeDetailPresenter, ExchangeDetailView> implements
        ExchangeDetailView {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar_subtitle)
    TextView mToolbarSubtitle;
    @BindView(R.id.icon)
    ImageView mIcon;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private ExchangeDetailAdapter mAdapter;

    private static final String EXTRA_EXCHANGE_ID = "extra_exchange_id";

    public static Intent createIntent(Context context, int exchangeId) {
        Intent i = new Intent(context, ExchangeDetailActivity.class);
        i.putExtra(EXTRA_EXCHANGE_ID, exchangeId);
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_detail);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mAdapter = new ExchangeDetailAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    @Override
    protected ExchangeDetailPresenter onCreatePresenter() {
        int exchangeId = getIntent().getIntExtra(EXTRA_EXCHANGE_ID, -1);
        return DaggerExchangeDetailComponent.builder()
                .appComponent(App.get(this).getAppComponent())
                .exchangeDetailModule(new ExchangeDetailModule(exchangeId))
                .build().presenter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exchange_detail, menu);
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

    @Override
    public void showExchange(ExchangeDetailViewModel model) {
        Exchange exchange = model.getExchange();
        mToolbarTitle.setText(TextUtils.isEmpty(exchange.getTitle()) ?
                exchange.getService().getTitle() :
                exchange.getTitle());
        mToolbarSubtitle.setText(exchange.getService().getTitle());
        mIcon.setImageResource(exchange.getService().getIconResId());
        mAdapter.setData(model);
    }

    @Override
    public void showMessage(int messageResId) {
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEditNameView(Exchange exchange) {
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
    public void showDeleteView(Exchange exchange) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.dialog_delete_exchange_message)
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> getPresenter().deleteExchange(exchange))
                .setNegativeButton(R.string.cancel, null)
                .create()
                .show();
    }

    @Override
    public void exit() {
        finish();
    }
}
