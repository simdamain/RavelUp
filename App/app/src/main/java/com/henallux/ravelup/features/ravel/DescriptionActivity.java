package com.henallux.ravelup.features.ravel;

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
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.henallux.ravelup.R;
import com.henallux.ravelup.dao.dataacess.RavelDAO;
import com.henallux.ravelup.exeptions.PinException;
import com.henallux.ravelup.exeptions.TokenException;
import com.henallux.ravelup.exeptions.TrajetException;
import com.henallux.ravelup.features.connection.LoginActivity;
import com.henallux.ravelup.models.JsonToTrajetModel;
import com.henallux.ravelup.models.PointInteretTrajetModel;
import com.henallux.ravelup.models.PointOfInterestModel;
import com.henallux.ravelup.models.TokenReceivedModel;
import com.henallux.ravelup.models.TrajetModel;

import java.util.ArrayList;

public class DescriptionActivity extends AppCompatActivity {
    private TokenReceivedModel token;
    private PointOfInterestModel point;
    private ArrayList<Long> pointsTrajet;
    private ArrayList<TrajetModel> trajets;
    private PointInteretTrajetModel actualPointInterest;
    private ArrayList<Long> idTrajets;
    private LoadPointInterest loadPointInterest;
    private LoadTrajet loadTrajet;
    private NetworkInfo activeNetwork;
    private ConnectivityManager connectivityManager;
    private TextView titre;
    private final String titreString = "Description de : ";
    private TextView description;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TrajetAdapter adapter;
    private Gson gsonBuilder = new GsonBuilder()
            .serializeNulls()
            .create();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        titre=findViewById(R.id.title_description_activity);
        description =findViewById(R.id.description_area_description_activity);

        //region recupération du token
        token = new TokenReceivedModel();
        token.setToken(PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .getString("token","no Token"));
        //endregion

        //region recuperation des infos du QR code
        String json=PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .getString("jsonTrajet","no trajet");

        Gson gson = new Gson();
        actualPointInterest = gson.fromJson(json, new TypeToken<PointInteretTrajetModel>(){}.getType());
        idTrajets= new ArrayList<>();

        for(JsonToTrajetModel trajet: actualPointInterest.getTrajets()){
            idTrajets.add(trajet.getIdTrajet());
        }
        //endregion

        //region gestion recyclerview
        mRecyclerView = findViewById(R.id.recyclerView_description_activity);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getBaseContext(), mRecyclerView ,new OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                                .edit()
                                .putString("PointsForTrajet",gsonBuilder.toJson(actualPointInterest.getTrajets().get(position).getPoints()))
                                .apply();
                        Intent goToTrajet =new Intent(DescriptionActivity.this, RavelMapActivity.class);
                        startActivity(goToTrajet);
                    }
                    @Override public void onLongItemClick(View view, int position) { }
                })
        );
        //endregion

        //region Test internet + obtention infos pour titre + description
        activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(isConnected) {
            loadPointInterest = new LoadPointInterest();
            loadPointInterest.execute(actualPointInterest.getIdPointInteret());
            loadTrajet = new LoadTrajet();
            loadTrajet.execute(idTrajets);
        } else{
            final Snackbar snackbar = Snackbar.make(findViewById(R.id.buttonSignUp),"La connection internet s'est interrompu", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }
        //endregion
    }

    class LoadPointInterest extends AsyncTask<Long,Void,PointOfInterestModel> {
        private RavelDAO ravelDAO= new RavelDAO();
        Boolean isTokenAlive= true;

        @Override
        protected PointOfInterestModel doInBackground(Long ...params) {
            point= new PointOfInterestModel();
            try {
                point =ravelDAO.getPointInterest(token,params[0]);
            } catch (TokenException e) {
                final Snackbar snackbar = Snackbar.make(findViewById(R.id.recyclerView_description_activity), e.getMessage(), Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
                isTokenAlive= false;
            } catch (PinException e) {
                final Snackbar snackbar = Snackbar.make(findViewById(R.id.recyclerView_description_activity), e.getMessage(), Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }
            return point;
        }

        protected void onPostExecute(PointOfInterestModel result) {
            if(isTokenAlive) {
                titre.setText(titreString + point.getNom());
                description.setText(point.getDescription());
            }else {
                startActivity(new Intent(DescriptionActivity.this,LoginActivity.class));
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    class LoadTrajet extends AsyncTask<ArrayList<Long>,Void,ArrayList<TrajetModel>> {
        private RavelDAO ravelDAO= new RavelDAO();
        Boolean isTokenAlive = true;
        @Override
        protected ArrayList<TrajetModel> doInBackground(ArrayList<Long> ...params) {
            trajets= new ArrayList<>();
            try {
                trajets =ravelDAO.getTrajets(token,params[0]);
            }catch (TokenException e) {
                final Snackbar snackbar = Snackbar.make(findViewById(R.id.recyclerView_description_activity), e.getMessage(), Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
                isTokenAlive = false;
            }catch (TrajetException e){
                final Snackbar snackbar = Snackbar.make(findViewById(R.id.recyclerView_description_activity), e.getMessage(), Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }
                return trajets;

        }
        protected void onPostExecute(ArrayList<TrajetModel> result) {
            if(isTokenAlive) {
                adapter = new TrajetAdapter(result);
                mRecyclerView.setAdapter(adapter);
            }else{
                startActivity(new Intent(DescriptionActivity.this,LoginActivity.class));
            }
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

}
