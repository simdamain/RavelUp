package com.henallux.ravelup.features.connection;

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
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.henallux.ravelup.R;
import com.henallux.ravelup.dao.dataacess.ConnectionDAO;
import com.henallux.ravelup.exeption.CityException;
import com.henallux.ravelup.exeption.SignUpException;
import com.henallux.ravelup.model.CityModel;
import com.henallux.ravelup.model.UserModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUpActivity extends AppCompatActivity {

    private Spinner spinnerCities;

    private ArrayList<String> libelleCities;

    private ArrayList<CityModel> allCities;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Gson gsonBuilder = new GsonBuilder().serializeNulls().create();


        //region Test internet
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(isConnected) {
            LoadCities loadCities = new LoadCities();
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
        spinnerCities = findViewById(R.id.spinnerCity);
        spinnerCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    //TODO
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        //endregion

        //region help button
        ImageView helpButtonPassword = findViewById(R.id.help_button_password_signup_activity);
        helpButtonPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Snackbar snackbar = Snackbar.make(findViewById(R.id.help_button_password_signup_activity), "le mot de passe doit contenir au moins un chiffre, une maj et un caractère spc", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }
        });

        ImageView helpButtonBirthdate = findViewById(R.id.help_button_birthdate_signup_activity);
        helpButtonBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Snackbar snackbar = Snackbar.make(findViewById(R.id.help_button_birthdate_signup_activity), "l'application est conçue pour des personnes d'au moins 12ans", Snackbar.LENGTH_INDEFINITE);
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

        //region inscription
        Button inscription = findViewById(R.id.buttonSignUp);
        inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModel user= new UserModel();

                Boolean hasError = false;
                Boolean dateHasError= false;

                TextInputLayout login = findViewById(R.id.login_signUpActivity);
                String loginValue = login.getEditText().getText().toString();
                login.setError(null);
                if(!loginValue.isEmpty()){
                    user.setUserName(loginValue);
                } else {
                    login.setError("Ce champs ne peut être vide");
                    hasError=true;
                }

                TextInputLayout password = findViewById(R.id.password_signUpActivity);
                String passwordValue = password.getEditText().getText().toString();

                Pattern patternPassword = Pattern.compile(getString(R.string.regex));
                Matcher matcherPassword = patternPassword.matcher(passwordValue);

                password.setError(null);
                if(!passwordValue.isEmpty()){
                    if(matcherPassword.matches())
                    user.setPassword(passwordValue);
                    else{
                        password.setError("le mot de passe n'est pas conforme");
                        hasError=true;
                    }
                } else {
                    password.setError("Ce champs ne peut être vide");
                    hasError=true;
                }

                TextInputLayout confirmedPassword = findViewById(R.id.confirmPassword_signUpActivity);
                String confirmedPasswordValue = confirmedPassword.getEditText().getText().toString();
                confirmedPassword.setError(null);
                if(!confirmedPasswordValue.isEmpty() && passwordValue.equals(confirmedPasswordValue) ){
                    user.setPasswordConfirm(confirmedPasswordValue);
                } else {
                    confirmedPassword.setError("Ce champs ne peut être vide // les mots de passe ne correspondent pas");
                    hasError=true;
                }

                TextInputLayout email = findViewById(R.id.email_signUpActivity);
                String emailValue = email.getEditText().getText().toString();

                Pattern patternEmail = Pattern.compile(getString(R.string.email_regex));
                Matcher matcherEmail = patternEmail.matcher(emailValue);

                email.setError(null);
                if(!emailValue.isEmpty()){
                    if(matcherEmail.matches())
                    user.setEmail(emailValue);
                    else{
                        email.setError("l'email n'est pas conforme");
                        hasError=true;
                    }
                } else {
                    email.setError("Ce champs ne peut être vide");
                    hasError=true;
                }

                TextInputLayout day = findViewById(R.id.day_birthDat_SignUpActivity);
                String dayValue = day.getEditText().getText().toString();
                day.setError(null);
                if(dayValue.isEmpty()){
                    day.setError("Ce champs ne peut être vide");
                    hasError=true;
                    dateHasError=true;
                }

                TextInputLayout month = findViewById(R.id.month_birthDat_SignUpActivity);
                String monthValue = month.getEditText().getText().toString();
                month.setError(null);
                if(monthValue.isEmpty()){
                    month.setError("Ce champs ne peut être vide");
                    hasError=true;
                    dateHasError= true;
                }

                TextInputLayout year = findViewById(R.id.year_birthDat_SignUpActivity);
                String yearValue = year.getEditText().getText().toString();
                year.setError(null);
                if(yearValue.isEmpty()){
                    year.setError("Ce champs ne peut être vide");
                    hasError=true;
                    dateHasError=true;
                }
                if(!dateHasError) {
                    int yearnb = Integer.parseInt(yearValue) - 1900;
                    int monthnb = Integer.parseInt(monthValue) - 1;
                    Date date = new Date(yearnb, monthnb, Integer.parseInt(dayValue));
                    Date ageMin = new Date(106,0,1);

                    if (date.compareTo(ageMin)<0) {
                        user.setBirthDate(date);
                    } else {
                        day.setError("la date");
                        month.setError("est trop");
                        year.setError("récente");
                        hasError = true;
                    }
                }

                String libelleVille = spinnerCities.getSelectedItem().toString();
                for (CityModel city : allCities){
                    if(libelleVille.contains(city.getLibelle())){
                        user.setIdVille(city.getId());
                    }
                }
                if(!hasError){
                        new signUp().execute(user);
                }
            }
        });
        //endregion
    }

    private class signUp extends  AsyncTask<UserModel,Void,Void>{

        private boolean isSignup = true;
        private ConnectionDAO connectionDAO = new ConnectionDAO();


        @Override
        protected Void doInBackground(UserModel... params) {
            try {
                connectionDAO.signUp(params[0]);
            } catch (SignUpException e) {
                final Snackbar snackbar = Snackbar.make(findViewById(R.id.buttonSignUp),e.getMessage(), Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
                isSignup = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(isSignup)
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
            } catch (CityException e) {
                final Snackbar snackbar = Snackbar.make(findViewById(R.id.buttonSignUp),e.getMessage(), Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
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
