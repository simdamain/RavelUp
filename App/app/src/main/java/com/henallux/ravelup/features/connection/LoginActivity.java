package com.henallux.ravelup.features.connection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.henallux.ravelup.R;
import com.henallux.ravelup.dao.dataacess.ConnectionDAO;
import com.henallux.ravelup.features.menus.MainRedirectActivity;
import com.henallux.ravelup.model.LoginModel;
import com.henallux.ravelup.model.TokenReceived;

public class LoginActivity extends AppCompatActivity {
    private TokenReceived mTokenReceived;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //region logo
        ImageView logo= findViewById(R.id.logo);
        Glide.with(this).load(R.drawable.logo).into(logo);
        //endregion

        //region connexion button
        Button connexion = findViewById(R.id.boutonConnexion);
        connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO verify with api
                //TokenReceived tokenReceived;


                LoginModel loginModel = new LoginModel();
                Boolean hasError = false;


                TextInputLayout login = findViewById(R.id.login_LoginActivity);
                String loginValue = login.getEditText().getText().toString();
                login.setError(null);
                if(!loginValue.equals("")){
                    loginModel.setUserName(loginValue);
                } else {
                    login.setError("Ce champs ne peut être vide");
                    hasError=true;
                }
                TextInputLayout password = findViewById(R.id.motDePasse_LoginActivity);
                String passwordValue = password.getEditText().getText().toString();
                password.setError(null);
                if(!passwordValue.equals("")){
                    loginModel.setPassword(passwordValue);
                } else {
                    password.setError("Ce champs ne peut être vide");
                    hasError=true;
                }


                if(!hasError) {
                    try {
                        new CheckLogin().execute(loginModel);

                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, "Identifiants incorrects", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    hasError = false;
                }


            }
        });
        //endregion
    }

    private class CheckLogin extends AsyncTask<LoginModel,Void,TokenReceived>{
        private ConnectionDAO connectionDAO= new ConnectionDAO();

        @Override
        protected TokenReceived doInBackground(LoginModel... params){

            TokenReceived tokenReceived = new TokenReceived();

            try{
                tokenReceived = connectionDAO.checkLogin(params[0]);
            }
            catch(Exception e){
                /*Toast*/
            }
            if( tokenReceived.getToken() != null)
                return tokenReceived;
            else{
                final Snackbar snackbar = Snackbar.make(findViewById(R.id.boutonConnexion),"Le login et/ou le mot de passe est incorrect", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
                return new TokenReceived();
            }

        }

        @Override
        protected void onPostExecute(TokenReceived tokenReceived) {
            mTokenReceived = tokenReceived;
            if (mTokenReceived.getErrorException().equals("") && mTokenReceived.getCode() == 200) {
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                        .edit()
                        .putString("token",mTokenReceived.getToken())
                        .apply();

                startActivity(new Intent(LoginActivity.this, MainRedirectActivity.class));
            }
        }
    }
}

