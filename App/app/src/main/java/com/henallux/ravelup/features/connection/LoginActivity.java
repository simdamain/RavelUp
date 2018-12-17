package com.henallux.ravelup.features.connection;

import android.content.Intent;
import android.os.AsyncTask;
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

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    //private ConnectionDAO connectionDAO;
    private TokenReceived tokenReceived;
    private CheckLogin checkLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkLogin = new CheckLogin();
        checkLogin.execute();
        //connectionDAO = new ConnectionDAO();

        //region logo
        ImageView logo= findViewById(R.id.logo);
        Glide.with(this).load(R.drawable.logo).into(logo);
        //endregion

//        //region errors
//        TextInputLayout login = findViewById(R.id.login);
//        login.setError("mauvais login");
//        TextInputLayout password = findViewById(R.id.motDePasse);
//        password.setError("mauvais mot de passe");
//        //endregion

        //region connexion button
        Button connexion = findViewById(R.id.boutonConnexion);
        connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO verify with api
                //TokenReceived tokenReceived;
                TextInputLayout login = findViewById(R.id.login_LoginActivity);
                TextInputLayout motDePasse = findViewById(R.id.motDePasse_LoginActivity);
                LoginModel loginModel = new LoginModel();

                loginModel.setLogin(login.getEditText().getText().toString());
                loginModel.setMotDePasse(motDePasse.getEditText().getText().toString());

                try {
                    //tokenReceived = connectionDAO.checkLogin(loginModel);
                    if (tokenReceived.getErrorException().equals("") && tokenReceived.getCode() == 200) {
                        startActivity(new Intent(LoginActivity.this, MainRedirectActivity.class));
                    }

                }
                catch(Exception e){
                        Toast.makeText(LoginActivity.this, "Mauvais Login et mot de passe", Toast.LENGTH_SHORT).show();
                }


            }
        });
        //endregion
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(checkLogin!=null)
            checkLogin.cancel(true);
    }
    class CheckLogin extends AsyncTask<Void,Void,TokenReceived>{
        private ConnectionDAO connectionDAO= new ConnectionDAO();

        @Override
        protected TokenReceived doInBackground(Void... Void){
            LoginModel loginModel = new LoginModel();
            TokenReceived tokenReceived = new TokenReceived();
            try{ tokenReceived= connectionDAO.checkLogin(loginModel);}
            catch(Exception e){/**Toast*/}
            return tokenReceived;
        }
    }
}

