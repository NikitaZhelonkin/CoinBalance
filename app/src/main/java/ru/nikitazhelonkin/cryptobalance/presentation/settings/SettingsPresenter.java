package ru.nikitazhelonkin.cryptobalance.presentation.settings;


import android.net.Uri;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import ru.nikitazhelonkin.cryptobalance.R;
import ru.nikitazhelonkin.cryptobalance.data.system.ResourceManager;
import ru.nikitazhelonkin.cryptobalance.domain.SettingsInteractor;
import ru.nikitazhelonkin.cryptobalance.mvp.MvpBasePresenter;
import ru.nikitazhelonkin.cryptobalance.utils.L;
import ru.nikitazhelonkin.cryptobalance.utils.rx.scheduler.RxSchedulerProvider;

public class SettingsPresenter extends MvpBasePresenter<SettingsView> {

    private SettingsInteractor mSettingsInteractor;
    private RxSchedulerProvider mRxSchedulerProvider;
    private ResourceManager mResourceManager;

    @Inject
    public SettingsPresenter(SettingsInteractor settingsInteractor,
                             RxSchedulerProvider rxSchedulerProvider,
                             ResourceManager resourceManager) {
        mSettingsInteractor = settingsInteractor;
        mRxSchedulerProvider = rxSchedulerProvider;
        mResourceManager = resourceManager;
    }

    public void exportConfig() {
        Disposable disposable = mSettingsInteractor.exportConfig()
                .compose(mRxSchedulerProvider.ioToMainTransformer())
                .subscribe(path -> getView().showMessage(mResourceManager.getString(R.string.success_export, path)),
                        throwable -> {
                    getView().showMessage(mResourceManager.getString(R.string.error_export));
                    L.e(""+throwable);
                });
        disposeOnDetach(disposable);
    }

    public void importConfig(Uri uri) {
        Disposable disposable = mSettingsInteractor.importConfig(uri)
                .compose(mRxSchedulerProvider.ioToMainTransformer())
                .subscribe(() -> getView().showMessage(mResourceManager.getString(R.string.success_import)),
                        throwable -> getView().showMessage(mResourceManager.getString(R.string.error_import)));
        disposeOnDetach(disposable);
    }

}
