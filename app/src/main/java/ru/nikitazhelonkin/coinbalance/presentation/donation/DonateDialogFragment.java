package ru.nikitazhelonkin.coinbalance.presentation.donation;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.nikitazhelonkin.coinbalance.App;
import ru.nikitazhelonkin.coinbalance.R;
import ru.nikitazhelonkin.coinbalance.di.DaggerPresenterComponent;
import ru.nikitazhelonkin.coinbalance.mvp.MvpDialogFragment;

public class DonateDialogFragment extends MvpDialogFragment<DonatePresenter, DonateView> implements DonateView {

    public static DonateDialogFragment create() {
        return new DonateDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_donation, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    protected DonatePresenter onCreatePresenter() {
        return DaggerPresenterComponent.builder()
                .appComponent(App.get(getContext()).getAppComponent())
                .build().donatePresenter();
    }

    @Override
    public void showMessage(int messageResId) {
        Toast.makeText(getContext(), messageResId, Toast.LENGTH_LONG).show();
    }


    @OnClick(R.id.donate_1)
    public void onDonate1Click(View v) {
        getPresenter().donate1Dollar();
    }


    @OnClick(R.id.donate_2)
    public void onDonate2Click(View v) {
        getPresenter().donate2Dollar();
    }


    @OnClick(R.id.donate_5)
    public void onDonate5Click(View v) {
        getPresenter().donate5Dollar();
    }


    @Override
    public void exit() {
        dismiss();
    }
}
