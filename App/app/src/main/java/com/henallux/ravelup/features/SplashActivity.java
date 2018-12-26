package com.henallux.ravelup.features;

import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.henallux.ravelup.R;
import com.henallux.ravelup.dao.dataacess.ConnectionDAO;
import com.henallux.ravelup.features.map.CategorieAdapter;
import com.henallux.ravelup.features.menus.RedirectActivity;
import com.henallux.ravelup.model.CityModel;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        startActivity(new Intent(SplashActivity.this, RedirectActivity.class));
    }


}
