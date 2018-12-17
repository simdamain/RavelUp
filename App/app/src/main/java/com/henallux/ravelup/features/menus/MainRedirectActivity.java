package com.henallux.ravelup.features.menus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.henallux.ravelup.R;
import com.henallux.ravelup.features.SplashActivity;
import com.henallux.ravelup.features.map.MapMenuActivity;
import com.henallux.ravelup.features.ravel.QrCodeActivity;

public class MainRedirectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_redirect);

        TextView ravel = findViewById(R.id.main_redirect_activity_ravel_text);
        TextView maps = findViewById(R.id.main_redirect_activity_places_text);

        ravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO change that shit
                startActivity(new Intent(MainRedirectActivity.this, QrCodeActivity.class));
            }
        });

        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainRedirectActivity.this, MapMenuActivity.class));
            }
        });
    }
}
