package ru.nikitazhelonkin.coinbalance.presentation.main;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.nikitazhelonkin.coinbalance.R;
import ru.nikitazhelonkin.coinbalance.data.entity.Coin;
import ru.nikitazhelonkin.coinbalance.data.entity.Wallet;
import ru.nikitazhelonkin.coinbalance.data.system.ClipboardManager;

public class QRCodeBottomSheetFragment extends BottomSheetDialogFragment {

    private static final String EXTRA_WALLET = "extra_wallet";

    @BindView(R.id.wallet_name)
    TextView mWalletNameView;
    @BindView(R.id.qr_code_view)
    ImageView mQRCodeView;
    @BindView(R.id.wallet_address)
    TextView mWalletAddressView;
    @BindView(R.id.error_view)
    View mErrorView;

    private Wallet mWallet;

    public static QRCodeBottomSheetFragment create(Wallet wallet) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_WALLET, Parcels.wrap(wallet));
        QRCodeBottomSheetFragment fragment = new QRCodeBottomSheetFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_qr_code_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mWallet = Parcels.unwrap(getArguments().getParcelable(EXTRA_WALLET));

        Coin coin = Coin.forTicker(mWallet.getCoinTicker());

        mWalletNameView.setText(TextUtils.isEmpty(mWallet.getAlias()) ?
                getString(R.string.my_wallet_format, coin.getName()) :
                mWallet.getAlias());
        mWalletAddressView.setText(mWallet.getAddress());

        Bitmap bitmap = generateQRCode(mWallet.getAddress());
        if (bitmap != null) {
            mQRCodeView.setImageBitmap(bitmap);
        }
        mErrorView.setVisibility(bitmap == null ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.wallet_address)
    public void onWalletClick() {
        ClipboardManager clipboardManager = new ClipboardManager(getContext());
        clipboardManager.copyToClipboard(mWallet.getAddress());
        Toast.makeText(getContext(), R.string.address_copied, Toast.LENGTH_SHORT).show();
    }

    @Nullable
    private Bitmap generateQRCode(String text) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 640, 640);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            return barcodeEncoder.createBitmap(bitMatrix);
        } catch (WriterException e) {
            return null;
        }
    }
}
