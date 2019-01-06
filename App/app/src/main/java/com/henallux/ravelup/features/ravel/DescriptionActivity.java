package com.henallux.ravelup.features.ravel;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.henallux.ravelup.R;
import com.henallux.ravelup.dao.dataacess.RavelDAO;
import com.henallux.ravelup.exeptions.ImageException;
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
    private PointInteretTrajetModel actualPointInterest;
    private ArrayList<Long> idPoints;
    private ArrayList<TrajetModel> trajets;
    private TrajetModel trajet;
    private ArrayList<String> imagesUrl;
    private boolean isConnected;
    private NetworkInfo activeNetwork;
    private ConnectivityManager connectivityManager;
    private TextView titre;
    private TextView description;
    private RecyclerView mRecyclerView;
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
        description.setMovementMethod(new ScrollingMovementMethod());

        //region recupération du token
        token = new TokenReceivedModel();
        token.setToken(PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .getString("token","no Token"));
        //endregion

        //region recuperation des infos du QR code
        String json=PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .getString("jsonTrajet","no trajet");

        actualPointInterest = gsonBuilder.fromJson(json, new TypeToken<PointInteretTrajetModel>(){}.getType());
        ArrayList<Long> idTrajets = new ArrayList<>();

        for(JsonToTrajetModel trajet: actualPointInterest.getTrajets()){
            idTrajets.add(trajet.getIdTrajet());
        }

        //endregion

        //region gestion recyclerview
        mRecyclerView = findViewById(R.id.recyclerView_description_activity);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getBaseContext(), mRecyclerView ,new OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        trajet= trajets.get(position);
                        idPoints = actualPointInterest.getTrajets().get(position).getPoints();
                        //region
                        activeNetwork = connectivityManager.getActiveNetworkInfo();
                        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                        if(isConnected) {
                            new LoadPins().execute(idPoints);
                        }
                        else{
                            final Snackbar snackbar = Snackbar.make(findViewById(R.id.buttonToMap_menuMap_activity),"La connection internet s'est interrompu", Snackbar.LENGTH_INDEFINITE);
                            snackbar.setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    snackbar.dismiss();
                                }
                            });
                            snackbar.show();
                        }
                    }
                    @Override public void onLongItemClick(View view, int position) { }
                })
        );
        //endregion


        //region Test internet + obtention infos pour titre + description
        activeNetwork = connectivityManager.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(isConnected) {
            LoadPointInterest loadPointInterest = new LoadPointInterest();
            loadPointInterest.execute(actualPointInterest.getIdPointInteret());
            LoadImages loadImages = new LoadImages();
            loadImages.execute(actualPointInterest.getIdPointInteret());
            LoadTrajet loadTrajet = new LoadTrajet();
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
                titre.setText(point.getNom());
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

    class LoadImages extends AsyncTask<Long,Void,ArrayList<String>> {
        private RavelDAO ravelDAO= new RavelDAO();
        Boolean isTokenAlive= true;
        TokenReceivedModel token= new TokenReceivedModel();
        @Override
        protected ArrayList<String> doInBackground(Long ...params) {
            imagesUrl = new ArrayList<>();
            try {
                token.setToken(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("token","no Token"));
                imagesUrl =ravelDAO.getImages(token,params[0]);
            } catch (TokenException e) {
                final Snackbar snackbar = Snackbar.make(findViewById(R.id.map),e.getMessage(), Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
                isTokenAlive= false;
            } catch (ImageException e) {
                final Snackbar snackbar = Snackbar.make(findViewById(R.id.map),e.getMessage(), Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }
            return imagesUrl;
        }

        protected void onPostExecute(ArrayList<String> result) {
            if(isTokenAlive) {
                ViewPager viewPager =findViewById(R.id.viewPager);
                ImageAdapter mAdapter = new ImageAdapter(getApplicationContext(),result);
                viewPager.setAdapter(mAdapter);
            }else{
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
                TrajetAdapter adapter = new TrajetAdapter(result);
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

    class LoadPins extends AsyncTask<ArrayList<Long>,Void,ArrayList<PointOfInterestModel>> {
        private RavelDAO ravelDAO = new RavelDAO();
        Boolean isTokenAlive = true;
        TokenReceivedModel token = new TokenReceivedModel();

        @Override
        protected ArrayList<PointOfInterestModel> doInBackground(ArrayList<Long>... params) {
            ArrayList<PointOfInterestModel> pointOfInterest = new ArrayList<>();
            try {
                token.setToken(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("token", "no Token"));
                pointOfInterest = ravelDAO.getPointsInterests(token, params[0]);
            } catch (TokenException e) {
                final Snackbar snackbar = Snackbar.make(findViewById(R.id.viewPager), e.getMessage(), Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
                isTokenAlive = false;
            } catch (PinException e) {
                final Snackbar snackbar = Snackbar.make(findViewById(R.id.viewPager), e.getMessage(), Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }
            return pointOfInterest;
        }

        protected void onPostExecute(ArrayList<PointOfInterestModel> result) {
            if (isTokenAlive) {
                String url = "https://www.google.com/maps/dir/?api=1&" +
                        "origin="+point.getLatitude()+","+point.getLongitude()+
                        "&destination=" + result.get(result.size() - 1).getLatitude() + "," + result.get(result.size() - 1).getLongitude() + "&waypoints=";
                for (PointOfInterestModel pin : result) {
                    if (pin == result.get(result.size() - 2)) {
                        url += +pin.getLatitude() + "," + pin.getLongitude();
                    } else {
                        if (pin == result.get(result.size() - 1)) {

                        } else
                            url += +pin.getLatitude() + "," + pin.getLongitude() + "|";
                    }
                }
               switch(trajet.getTypeDeplacement()){
                   case 1:
                       url += "&travelmode=walking";
                        break;
                   case 2:
                       url += "&travelmode=bicycling";
                       break;
                   case 3:
                       url += "&travelmode=bicycling";
                       break;
               }
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse(url));
                    intent.setPackage("com.google.android.apps.maps");
                    startActivity(intent);
                }else {
                startActivity(new Intent(DescriptionActivity.this, LoginActivity.class));
            }
        }
    }


}
