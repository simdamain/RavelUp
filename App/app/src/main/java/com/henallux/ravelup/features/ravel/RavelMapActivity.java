package com.henallux.ravelup.features.ravel;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.henallux.ravelup.R;
import com.henallux.ravelup.dao.dataacess.RavelDAO;
import com.henallux.ravelup.exeptions.PinException;
import com.henallux.ravelup.exeptions.TokenException;
import com.henallux.ravelup.features.connection.LoginActivity;
import com.henallux.ravelup.models.PointOfInterestModel;
import com.henallux.ravelup.models.TokenReceivedModel;

import java.util.ArrayList;

public class RavelMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<PointOfInterestModel> pointOfInterest;
    private ArrayList<Long> idPoints;
    private LatLng myLocation;
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
        setContentView(R.layout.activity_ravel_map);
        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        activeNetwork = connectivityManager.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(isConnected) {
            getLocationPermission();
        }
        else{
            final Snackbar snackbar = Snackbar.make(findViewById(R.id.buttonToMap_menuMap_activity),"La connexion internet s'est interrompu", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }


//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.mapRavel);
//        mapFragment.getMapAsync(this);
    }

//    private String getUrl(LatLng start,LatLng end, String directionMode){
//        String strStart = "origin="+start.latitude+","+start.longitude;
//        String strEnd = "destination="+end.latitude+","+end.longitude;
//        String mode= "mode="+ directionMode;
//        String parameters = strStart+"&"+strEnd+"&"+mode;
//        String output= "json";
//        String url="https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters+"&key="+getString(R.string.google_maps_key);
//        return url;
//    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        pointOfInterest=new ArrayList<>();
        if(mLocationPermissionsGranted){
            getDeviseLocation();
        }
        String json =PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .getString("PointsForTrajet","no points");
        Gson gson = new Gson();
        idPoints = gson.fromJson(json, new TypeToken<ArrayList<Long>>(){}.getType());

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
        //endregion



    }

    private void getLocationPermission(){
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

    private void getDeviseLocation(){
        mFusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationPermissionsGranted){
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Location currentLocation = (Location) task.getResult();
                            myLocation = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(myLocation)
                                    .title("Votre position"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,DEFAULT_ZOOM));
                        }else{
                            Toast.makeText(RavelMapActivity.this,"Impossible d'obtenir votre localisation", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }catch(SecurityException e){
            Log.e("Tag","getDeviceLocation : SecurityException: "+e.getMessage());
        }
    }

    private void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapRavel);
        mapFragment.getMapAsync(this);
    }

    class LoadPins extends AsyncTask<ArrayList<Long>,Void,ArrayList<PointOfInterestModel>> {
        private RavelDAO ravelDAO= new RavelDAO();
        Boolean isTokenAlive;
        TokenReceivedModel token= new TokenReceivedModel();

        @Override
        protected ArrayList<PointOfInterestModel> doInBackground(ArrayList<Long> ...params) {
            pointOfInterest= new ArrayList<>();
            try {
                token.setToken(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("token","no Token"));
                pointOfInterest =ravelDAO.getPointsInterests(token,params[0]);
            } catch (TokenException e) {
                final Snackbar snackbar = Snackbar.make(findViewById(R.id.map),e.getMessage(), Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
                isTokenAlive = false;
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
            return pointOfInterest;
        }

        protected void onPostExecute(ArrayList<PointOfInterestModel> result) {
            if (isTokenAlive) {
                PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
                options.add(myLocation);
                LatLng lastPin = myLocation;
                String url = "http://maps.google.com/maps?f=d&hl=en&saddr=" + myLocation.latitude + "," + myLocation.longitude;
                for (PointOfInterestModel pin : result) {
//                mMap.addMarker(new MarkerOptions().position(new LatLng(pin.getLatitude(), pin.getLongitude()))
//                        .title(pin.getNom())
//                        .snippet(pin.getDescription()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//                options.add(new LatLng(pin.getLatitude(),pin.getLongitude()));
                    url += "&daddr=" + pin.getLatitude() + "," + pin.getLongitude();
                }
                //            Polyline polyline1= mMap.addPolyline(options);
                url += "&ie=UTF8&0&om=0&output=kml";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(url));
                startActivity(intent);
            }else{
                startActivity(new Intent(RavelMapActivity.this,LoginActivity.class));
            }
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
