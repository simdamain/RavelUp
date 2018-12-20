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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.henallux.ravelup.R;
import com.henallux.ravelup.dao.dataacess.MapDAO;
import com.henallux.ravelup.features.ravel.QrCodeActivity;
import com.henallux.ravelup.model.CategoryModel;
import com.henallux.ravelup.model.TokenReceived;

import java.util.ArrayList;

public class MapMenuActivity extends AppCompatActivity {
    private LoadCategories loadCategories;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CategorieAdapter adapter;
    private ArrayList<String> allCategories;
    private TokenReceived token;
    private String stringToken;
    private NetworkInfo activeNetwork;
    private boolean isConnected;
    private ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_map);
        //connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        token = new TokenReceived();
        mRecyclerView = findViewById(R.id.recyclerViewCat);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);


        token.setToken(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("token","no Token"));

        //Test internet
        /*activeNetwork = connectivityManager.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(isConnected) {*/
            loadCategories= new LoadCategories();
            loadCategories.execute(token);
        //}
        /*else{
            final Snackbar snackbar = Snackbar.make(findViewById(R.id.boutonToCarte),"La connection internet s'est interrompu", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }*/





    //region Slider
        // SeekBar
        SeekBar rayon = findViewById(R.id.seekBarRayon );
         final TextView rayonText = findViewById(R.id.rayonText);

        rayonText.setText("Rayon : " + rayon.getProgress()+"m");
        //
        rayon.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            int progress = 0;

            // When Progress value changed.
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;

                rayonText.setText("rayon : "+progressValue+"m");
            }
            // Notification that the user has finished a touch gesture
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                rayonText.setText("rayon : "+progress+"m");

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
        });
        //endregion
    // region Help button
        ImageView helpButton = findViewById(R.id.menu_carte_activity_help_button);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO changer le string
                final Snackbar snackbar = Snackbar.make(findViewById(R.id.menu_carte_activity_help_button), "j'ai pas d'idée", Snackbar.LENGTH_INDEFINITE);
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

    //button MapActivity
        Button map = findViewById(R.id.boutonToCarte);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMenu =new Intent(MapMenuActivity.this, MapActivity.class);
                startActivityForResult(goToMenu,1);
            }
        });
    }

    class LoadCategories extends AsyncTask<TokenReceived,Void,ArrayList<String>> {
        private MapDAO mapDAO= new MapDAO();

        @Override
        protected ArrayList<String> doInBackground(TokenReceived ...params) {
            ArrayList<CategoryModel> categories = new ArrayList<>();
            allCategories= new ArrayList<>();
            try {
                categories =mapDAO.getAllCategories(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (CategoryModel cat: categories){
                allCategories.add(cat.getLibelle());
            }
            return allCategories;
        }

        protected void onPostExecute(ArrayList<String> result){
            adapter = new CategorieAdapter(result);
            mRecyclerView.setAdapter(adapter);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
