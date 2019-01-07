package com.henallux.ravelup.features.map;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.henallux.ravelup.R;
import com.henallux.ravelup.dao.dataacess.MapDAO;
import com.henallux.ravelup.exeption.CategoryException;
import com.henallux.ravelup.exeption.TokenException;
import com.henallux.ravelup.features.connection.LoginActivity;
import com.henallux.ravelup.features.menus.MainRedirectActivity;
import com.henallux.ravelup.model.CategoryModel;
import com.henallux.ravelup.model.TokenReceivedModel;

import java.util.ArrayList;

public class MapMenuActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private CategoryAdapter adapter;

    private ArrayList<CategoryModel> allCategories;
    private ArrayList<Long> idCategories;

    private Gson gsonBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu_map);

        allCategories= new ArrayList<>();

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        TokenReceivedModel token = new TokenReceivedModel();
        mRecyclerView = findViewById(R.id.recyclerViewCat);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        token.setToken(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("token","no Token"));

        //region Test internet
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(isConnected) {
            LoadCategories loadCategories = new LoadCategories();
            loadCategories.execute(token);
        }
        else{
            final Snackbar snackbar = Snackbar.make(findViewById(R.id.buttonToMap_menuMap_activity),"La connexion internet s'est interrompue redirection vers le menu principale", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                        startActivity(new Intent(MapMenuActivity.this,MainRedirectActivity.class));
                    }
                });
            snackbar.show();

        }
        //endregionk

    //region Slider
        // SeekBar
        final SeekBar rayon = findViewById(R.id.seekBarRayon );
         final TextView rayonText = findViewById(R.id.radiusText);

        rayonText.setText("Rayon : " + rayon.getProgress()+" km");
        //
        rayon.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            int progress = 0;

            // When Progress value changed.
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;

                rayonText.setText("rayon : "+progressValue+"km");
            }
            // Notification that the user has finished a touch gesture
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                rayonText.setText("rayon : "+progress+"km");

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
        });
        //endregion

    // region Help button
        ImageView helpButton = findViewById(R.id.help_button_menuMap_activity);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Snackbar snackbar = Snackbar.make(findViewById(R.id.help_button_menuMap_activity), "choisissez les catégories que vous voulez voir afficher sur la carte", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }
        });

        ImageView helpButtonSeeker = findViewById(R.id.help_button_seeker_menuMap_activity);
        helpButtonSeeker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Snackbar snackbar = Snackbar.make(findViewById(R.id.help_button_seeker_menuMap_activity), "le rayon représente le zone autour de vous où les différents pin seront afficher", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }
        });
        //endregion

    //region button MapActivity
        Button map = findViewById(R.id.buttonToMap_menuMap_activity);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idCategories = adapter.getIdCategories();
                gsonBuilder= new Gson();
                String idToJson = gsonBuilder.toJson(idCategories);
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                        .edit()
                        .putString("Categories",idToJson)
                        .apply();
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                        .edit()
                        .putFloat("Rayon",(float)((rayon.getProgress())*0.001))  //le rayon est en km
                        .apply();

                Intent goToMenu =new Intent(MapMenuActivity.this, MapActivity.class);
                startActivity(goToMenu);
            }
        });
        //endregion
    }

    class LoadCategories extends AsyncTask<TokenReceivedModel,Void,ArrayList<CategoryModel>> {

        MapDAO mapDAO= new MapDAO();
        Boolean isTokenAlive= true;

        @Override
        protected ArrayList<CategoryModel> doInBackground(TokenReceivedModel...params) {

            idCategories = new ArrayList<>();

            try {
                allCategories =mapDAO.getAllCategories(params[0]);
            }catch (TokenException e) {
                final Snackbar snackbar = Snackbar.make(findViewById(R.id.buttonToMap_menuMap_activity), e.getMessage(), Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
                isTokenAlive= false;
            }catch (CategoryException e){
                final Snackbar snackbar = Snackbar.make(findViewById(R.id.buttonToMap_menuMap_activity),e.getMessage(), Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }

            return allCategories;
        }

        protected void onPostExecute(ArrayList<CategoryModel> result){
            if (isTokenAlive) {
                adapter = new CategoryAdapter(result);
                mRecyclerView.setAdapter(adapter);
            }
            else{
                startActivity(new Intent(MapMenuActivity.this,LoginActivity.class));
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
