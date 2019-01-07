package com.henallux.ravelup.features.ravel;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.gson.Gson;
import com.henallux.ravelup.R;
import com.henallux.ravelup.features.menus.MainRedirectActivity;
import com.henallux.ravelup.model.TokenReceivedModel;
/*import com.henallux.ravelup.features;
import com.henallux.smartcity.DAO.PlantJSONDAO;
import com.henallux.smartcity.R;*/

import java.io.IOException;


public class QrCodeActivity extends AppCompatActivity {


    private TextView txtView;

    private CameraSource cameraSource;
    private SurfaceView cameraPreview;
    private ConnectivityManager connectivityManager;
    final int RequestCameraPermissionID = 1001;

    //to avoid spamming scan of a qr code

    private boolean hasScanned = false;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_qr_code);

            connectivityManager = (ConnectivityManager) QrCodeActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

            TokenReceivedModel token = new TokenReceivedModel();
            token.setToken(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("Token","no Token"));

            cameraPreview =findViewById(R.id.scanner);
            txtView =findViewById(R.id.explanationQRDestination);

            BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
                    .setBarcodeFormats(Barcode.QR_CODE)
                    .build();
            cameraSource = new CameraSource
                    .Builder(this, barcodeDetector)
                    .setRequestedPreviewSize(300,350)
                    .build();

            //Add event
            cameraPreview.getHolder().addCallback(cameraCallback);
            barcodeDetector.setProcessor(scanDetector);
        }

        private SurfaceHolder.Callback cameraCallback = new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //Request permission
                    ActivityCompat.requestPermissions(QrCodeActivity.this,
                            new String[]{Manifest.permission.CAMERA}, RequestCameraPermissionID);
                    return;
                }
                try {
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
                hasScanned= false;
            }
        };

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            switch (requestCode) {
                case RequestCameraPermissionID: {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        try {
                            cameraSource.start(cameraPreview.getHolder());
                        } catch (IOException e) {

                        }
                    }
                }
                break;
            }
        }

        private Detector.Processor<Barcode> scanDetector = new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            //TO DO ON SCAN ITEM
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();


                    if(qrcodes.size() != 0 && !hasScanned){
                        hasScanned = true;
                        txtView.post(new Runnable(){
                            @Override
                            public void run(){
                                Gson gsonBuilder = new Gson();
                                String json = qrcodes.valueAt(0).displayValue;

                                PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                                        .edit()
                                        .putString("jsonTrajet",json)
                                        .apply();
                                //Create vibrate
                                Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                                vibrator.vibrate(250);
                                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                                if(isConnected) {
                                        Intent goToDescription = new Intent(QrCodeActivity.this, DescriptionActivity.class);
                                        startActivity(goToDescription);
                                }else{
                                        final Snackbar snackbar = Snackbar.make(findViewById(R.id.scanner),"La connexion internet s'est interrompue reessayez le scan", Snackbar.LENGTH_INDEFINITE);
                                        snackbar.setAction("OK", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                snackbar.dismiss();
                                            }
                                        });
                                        snackbar.show();
                                    hasScanned = false;
                                    }
                            }
                        });
//                        hasScanned = true;
                    }
                }
        };

    }


