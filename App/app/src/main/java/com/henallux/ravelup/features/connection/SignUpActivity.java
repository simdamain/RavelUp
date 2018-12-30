package com.henallux.ravelup.features.connection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.henallux.ravelup.R;
import com.henallux.ravelup.dao.dataacess.ConnectionDAO;
import com.henallux.ravelup.model.CityModel;
import com.henallux.ravelup.model.UserModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class SignUpActivity extends AppCompatActivity {
    private ArrayList<CityModel> allCities;
    private LoadCities loadCities;
    private NetworkInfo activeNetwork;
    private ConnectivityManager connectivityManager;
    private Spinner spinnerCities;
    private ArrayList<String> libelleCities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        Gson gsonBuilder = new GsonBuilder().serializeNulls().create();


        //region Test internet
        activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(isConnected) {
            loadCities = new LoadCities();
            loadCities.execute();
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

        //region spinner
        spinnerCities =findViewById(R.id.spinnerCity);
        spinnerCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
//                    Toast.makeText(SignUpActivity.this, item.toString(),
//                           Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });
        //endregion

        //region inscription
        Button inscription = findViewById(R.id.buttonSignUp);
        inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModel user= new UserModel();
                Boolean hasError = false;

                TextInputLayout login = findViewById(R.id.login_signUpActivity);
                String loginValue = login.getEditText().getText().toString();
                if(!loginValue.isEmpty()){
                    user.setUserName(loginValue);
                } else {
                    login.setError("Ce champs ne peut être vide");
                    hasError=true;
                }

                TextInputLayout password = findViewById(R.id.password_signUpActivity);
                String passwordValue = password.getEditText().getText().toString();
                if(!passwordValue.isEmpty()){
                    user.setPassword(passwordValue);
                } else {
                    password.setError("Ce champs ne peut être vide");
                    hasError=true;
                }

                TextInputLayout confirmedPassword = findViewById(R.id.confirmPassword_signUpActivity);
                String confirmedPasswordValue = confirmedPassword.getEditText().getText().toString();
                if(!confirmedPasswordValue.isEmpty()){
                    user.setPasswordConfirm(confirmedPasswordValue);
                } else {
                    confirmedPassword.setError("Ce champs ne peut être vide");
                    hasError=true;
                }

                TextInputLayout email = findViewById(R.id.email_signUpActivity);
                String emailValue = email.getEditText().getText().toString();
                if(!emailValue.isEmpty()){
                    user.setEmail(emailValue);
                } else {
                    email.setError("Ce champs ne peut être vide");
                    hasError=true;
                }

                TextInputLayout day = findViewById(R.id.day_birthDat_SignUpActivity);
                String dayValue = day.getEditText().getText().toString();
                if(dayValue.isEmpty()){
                    day.setError("Ce champs ne peut être vide");
                    hasError=true;
                }

                TextInputLayout month = findViewById(R.id.month_birthDat_SignUpActivity);
                String monthValue = month.getEditText().getText().toString();
                if(monthValue.isEmpty()){
                    month.setError("Ce champs ne peut être vide");
                    hasError=true;
                }

                TextInputLayout year = findViewById(R.id.year_birthDat_SignUpActivity);
                String yearValue = year.getEditText().getText().toString();
                if(yearValue.isEmpty()){
                    year.setError("Ce champs ne peut être vide");
                    hasError=true;
                }
                int yearnb =Integer.parseInt(yearValue)-1900;
                int monthnb =Integer.parseInt(yearValue)-1;
                Date date= new Date(yearnb,monthnb,Integer.parseInt(dayValue));
                user.setDateNaissance(date);

                String libelleVille = spinnerCities.getSelectedItem().toString();
                for (CityModel city : allCities){
                    if(libelleVille.contains(city.getLibelle())){
                        user.setIdVille(city.getId());
                    }
                }

                if(!hasError){
                    try {
                        new signUp().execute(user);

                    } catch (Exception e) {
                        Toast.makeText(SignUpActivity.this, "Les données entrées sont incorrect", Toast.LENGTH_SHORT).show();
                    }
                }else
                    hasError = false;

            }
        });
        //endregion
    }

    private class signUp extends  AsyncTask<UserModel,Void,Void>{

        private ConnectionDAO connectionDAO = new ConnectionDAO();

        @Override
        protected Void doInBackground(UserModel... params) {
            connectionDAO.signUp(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
           startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
        }
    }

    private class LoadCities extends AsyncTask<Void, Void, ArrayList<String>> {
        private ConnectionDAO connectionDAO = new ConnectionDAO();

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            libelleCities= new ArrayList<>();
            try {
                allCities = connectionDAO.getAllCities();
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (CityModel city : allCities){
                libelleCities.add(city.getLibelle()+" - "+city.getCodePostal());
            }
            return libelleCities;
        }

        protected void onPostExecute(ArrayList<String> result) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                    android.R.layout.simple_spinner_item, libelleCities);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCities.setAdapter(adapter);
        }
    }

}
