package com.henallux.ravelup.features.ravel;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;


import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.henallux.ravelup.R;
import com.henallux.ravelup.dao.dataacess.RavelDAO;
import com.henallux.ravelup.model.PointOfInterestModel;
/*import com.henallux.ravelup.features;
import com.henallux.smartcity.DAO.PlantJSONDAO;
import com.henallux.smartcity.R;*/

import org.json.JSONException;

import java.io.IOException;


public class QrCodeActivity extends AppCompatActivity {
        private String token;
        private SharedPreferences preferences;
        private SharedPreferences.Editor editor;
        private ConnectivityManager connectivityManager;
        private NetworkInfo activeNetwork;
        private boolean isConnected;
        private SurfaceView cameraPreview;
        private TextView txtView;
        private BarcodeDetector barcodeDetector;
        private CameraSource cameraSource;
        //to avoid spamming scan of a qr code
        private boolean hasScanned = false;
        final int RequestCameraPermissionID = 1001;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_qr_code);

            connectivityManager = (ConnectivityManager) QrCodeActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

            preferences = PreferenceManager.getDefaultSharedPreferences(this);
            token = preferences.getString("token", "");
            editor=preferences.edit();

            cameraPreview =findViewById(R.id.scanner);
            txtView =findViewById(R.id.explanationQRDestination);

            barcodeDetector = new BarcodeDetector.Builder(this)
                    .setBarcodeFormats(Barcode.QR_CODE)
                    .build();
            cameraSource = new CameraSource
                    .Builder(this, barcodeDetector)
                    .setRequestedPreviewSize(640, 480)
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
                hasScanned=false;
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
                    txtView.post(new Runnable(){
                        @Override
                        public void run(){
                            //RavelDAO ravelDAO = new RavelJSONDAO();

                            //Create vibrate
                            Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(250);

                            try {
                                /*Intent goToDescription = new Intent(QrCodeActivity.this, DescriptionActivity.class);
                                PointOfInterestModel point = new PointOfInterestModel();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("poitnInteret", point);
                                goToDescription.putExtras(bundle);
                                startActivity(goToDescription);*/
                            }
                            catch (Exception e) {
                                final Snackbar snackbar = Snackbar.make(findViewById(R.id.boutonToCarte),"Il y a eu un problème lors de la lecture du qr code", Snackbar.LENGTH_INDEFINITE);
                                snackbar.setAction("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        snackbar.dismiss();
                                    }
                                });
                                snackbar.show();
                            }
                        }
                    });
                    hasScanned = true;
                }
            }
        };

    }


