package com.henallux.ravelup.features.map;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.henallux.ravelup.R;
import com.henallux.ravelup.dao.dataacess.MapDAO;
import com.henallux.ravelup.exeptions.PinException;
import com.henallux.ravelup.exeptions.TokenException;
import com.henallux.ravelup.features.connection.LoginActivity;
import com.henallux.ravelup.models.PinModel;
import com.henallux.ravelup.models.PointOfInterestModel;
import com.henallux.ravelup.models.TokenReceivedModel;
import java.util.ArrayList;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<PointOfInterestModel> allPins;
    private PinModel pin;
    private NetworkInfo activeNetwork;
    private boolean isConnected;
    private ConnectivityManager connectivityManager;
    private Boolean mLocationPermissionsGranted =false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 16f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        pin= new PinModel();
        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

            getLocationPermission();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        pin.setRayon(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getFloat("Rayon",0));
        pin.setLongitude(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getFloat("Longitude",0));
        pin.setLatitude(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getFloat("Latitude",0));

        Gson gsonBuilder = new Gson();
        String jsonToId = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("Categories","");
        ArrayList idCategories = gsonBuilder.fromJson(jsonToId, ArrayList.class);
        pin.setIdCategories(idCategories);

        if(mLocationPermissionsGranted){
            getDeviseLocation();
        }
        //region ajoutPin
        activeNetwork = connectivityManager.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(isConnected) {
            new LoadPins().execute(pin);
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

        //endregion

    }

    private void getLocationPermission() {
        String[] permissions= {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted= true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else{
            ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    private void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getDeviseLocation(){
        mFusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
            try{
            if(mLocationPermissionsGranted) {
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult();
                            LatLng location = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(location)
                                    .title("Votre position"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, DEFAULT_ZOOM));

                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                                    .edit()
                                    .putFloat("Longitude", (float) currentLocation.getLongitude())
                                    .apply();
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                                    .edit()
                                    .putFloat("Latitude", (float) currentLocation.getLatitude())
                                    .apply();
                        } else {
                            Toast.makeText(MapActivity.this, "Impossible d'obtenir votre localisation", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }catch(SecurityException e){
            final Snackbar snackbar = Snackbar.make(findViewById(R.id.map),e.getMessage(), Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }
    }


    class LoadPins extends AsyncTask<PinModel,Void,ArrayList<PointOfInterestModel>> {
        private MapDAO mapDAO= new MapDAO();
        Boolean isTokenAlive= true;
        TokenReceivedModel token= new TokenReceivedModel();
        @Override
        protected ArrayList<PointOfInterestModel> doInBackground(PinModel ...params) {
            allPins= new ArrayList<>();
            try {
                token.setToken(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("token","no Token"));

                allPins =mapDAO.getAllPins(token,params[0]);
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
            } catch (PinException e) {
                final Snackbar snackbar = Snackbar.make(findViewById(R.id.map),e.getMessage(), Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }
            return allPins;
        }

        protected void onPostExecute(ArrayList<PointOfInterestModel> result) {
            if(isTokenAlive) {
                for (PointOfInterestModel pin : result) {
                    switch ((int) pin.getCategorieId()) {
                        case 1:
                            mMap.addMarker(new MarkerOptions().position(new LatLng(pin.getLatitude(), pin.getLongitude()))
                                    .title(pin.getNom())
                                    .snippet(pin.getDescription()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                            break;
                        case 2:
                            mMap.addMarker(new MarkerOptions().position(new LatLng(pin.getLatitude(), pin.getLongitude()))
                                    .title(pin.getNom())
                                    .snippet(pin.getDescription()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                            break;
                        case 3:
                            mMap.addMarker(new MarkerOptions().position(new LatLng(pin.getLatitude(), pin.getLongitude()))
                                    .title(pin.getNom())
                                    .snippet(pin.getDescription()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                            break;
                        case 4:
                            mMap.addMarker(new MarkerOptions().position(new LatLng(pin.getLatitude(), pin.getLongitude()))
                                    .title(pin.getNom())
                                    .snippet(pin.getDescription()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                            break;
                        case 5:
                            mMap.addMarker(new MarkerOptions().position(new LatLng(pin.getLatitude(), pin.getLongitude()))
                                    .title(pin.getNom())
                                    .snippet(pin.getDescription()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
                            break;
                        default:
                            mMap.addMarker(new MarkerOptions().position(new LatLng(pin.getLatitude(), pin.getLongitude()))
                                    .title(pin.getNom())
                                    .snippet(pin.getDescription()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                            break;
                    }
                }
            }else{
                startActivity(new Intent(MapActivity.this,LoginActivity.class));
            }

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

}



