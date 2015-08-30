package it.jaschke.alexandria.cameraPreview;

/*
 * Barebones implementation of displaying camera preview.
 *
 * Created by lisah0 on 2012-02-24
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import it.jaschke.alexandria.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * A basic Camera preview class
 */
public class CameraPreviewActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    public static final String TAG = CameraPreviewActivity.class.getSimpleName();
    public static final String RAW_RESULT_KEY = "rawResult";

    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        // DEBUG:
//        Intent data = new Intent();
//        data.putExtra(RAW_RESULT_KEY, "9781888799972");
//        setResult(RESULT_OK, data);
//        finish();
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        if (rawResult.getBarcodeFormat() != null) {
//            if (rawResult.getBarcodeFormat() == BarcodeFormat.ISBN13) {
            if (rawResult.getBarcodeFormat() == BarcodeFormat.EAN_13) {
                Intent data = new Intent();
                data.putExtra(RAW_RESULT_KEY, rawResult.getText());
                setResult(RESULT_OK, data);
                finish();
            } else {
                Toast.makeText(this, R.string.error_upc, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, R.string.error_scan, Toast.LENGTH_LONG).show();
        }
    }

}
