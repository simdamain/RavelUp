package com.henallux.ravelup.features.connection;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.henallux.ravelup.R;
import com.henallux.ravelup.dao.dataacess.ConnectionDAO;
import com.henallux.ravelup.exeption.LoginExecption;
import com.henallux.ravelup.features.menus.MainRedirectActivity;
import com.henallux.ravelup.model.LoginModel;
import com.henallux.ravelup.model.TokenReceivedModel;

public class LoginActivity extends AppCompatActivity {

    private boolean isConnected;

    private String loginValue;
    private String passwordValue;
    private TextInputLayout password;
    private TextInputLayout login;
    private NetworkInfo activeNetwork;
    private ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        //region logo
        ImageView logo= findViewById(R.id.logo);
        Glide.with(this).load(R.drawable.logo).into(logo);
        //endregion

        //region connection button
        Button connexion = findViewById(R.id.connectionButton_login_activity);
        connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginModel loginModel = new LoginModel();
                Boolean hasError = false;

                login = findViewById(R.id.login_LoginActivity);
                loginValue = login.getEditText().getText().toString();
                login.setError(null);
                if(!loginValue.equals("")){
                    loginModel.setUserName(loginValue);
                } else {
                    login.setError("Ce champs ne peut être vide");
                    hasError=true;
                }

                password = findViewById(R.id.password_LoginActivity);
                passwordValue = password.getEditText().getText().toString();
                password.setError(null);
                if(!passwordValue.equals("")){
                    loginModel.setPassword(passwordValue);
                } else {
                    password.setError("Ce champs ne peut être vide");
                    hasError=true;
                }

                activeNetwork = connectivityManager.getActiveNetworkInfo();
                isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                if(!hasError){
                    if(isConnected)
                        new CheckLogin().execute(loginModel);
                    else {
                        final Snackbar snackbar = Snackbar.make(findViewById(R.id.connectionButton_login_activity),"La connexion internet s'est interrompue", Snackbar.LENGTH_INDEFINITE);
                        snackbar.setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) { snackbar.dismiss();
                            }
                        });
                        snackbar.show();
                    }
                }
            }
        });
        //endregion
    }


    private class CheckLogin extends AsyncTask<LoginModel,Void,TokenReceivedModel>{
        private ConnectionDAO connectionDAO= new ConnectionDAO();

        @Override
        protected TokenReceivedModel doInBackground(LoginModel... params){

            TokenReceivedModel tokenReceived = new TokenReceivedModel();

            try{
                tokenReceived = connectionDAO.checkLogin(params[0]);
            }
            catch (LoginExecption e){
                final Snackbar snackbar = Snackbar.make(findViewById(R.id.connectionButton_login_activity),e.getMessage(), Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }
            return tokenReceived;
        }

        @Override
        protected void onPostExecute(TokenReceivedModel tokenReceived) {
            if (tokenReceived.getErrorException().equals("") && tokenReceived.getCode() == 200) {
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                        .edit()
                        .putString("token", tokenReceived.getToken())
                        .apply();

                startActivity(new Intent(LoginActivity.this, MainRedirectActivity.class));
            }
        }
    }
}

