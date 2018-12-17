package com.henallux.ravelup.features.connection;

import android.content.Intent;
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
    private ConnectionDAO connectionDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        connectionDAO = new ConnectionDAO();

        //region logo handling
        ImageView logo= findViewById(R.id.logo);
        Glide.with(this).load(R.drawable.logo).into(logo);
        //endregion

//        //region errors handling
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
                LoginModel loginModel = new LoginModel();
                TextInputLayout login = findViewById(R.id.login_LoginActivity);
                TextInputLayout motDePasse = findViewById(R.id.motDePasse_LoginActivity);
                loginModel.setLogin(login.getEditText().getText().toString());
                loginModel.setMotDePasse(motDePasse.getEditText().getText().toString());
                TokenReceived tokenReceived = new TokenReceived();
                try {
                    tokenReceived = connectionDAO.checkLogin(loginModel);

                }
                catch(Exception e){
                        Toast.makeText(LoginActivity.this, "Mauvais Login et mot de passe", Toast.LENGTH_SHORT).show();
                }
                if (tokenReceived.getErrorException().equals("") && tokenReceived.getCode() == 200) {
                    startActivity(new Intent(LoginActivity.this, MainRedirectActivity.class));
                }

            }
        });
        //endregion
    }
}
