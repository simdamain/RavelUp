package com.henallux.ravelup.features.menus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.henallux.ravelup.R;
import com.henallux.ravelup.features.connection.LoginActivity;
import com.henallux.ravelup.features.connection.SignUpActivity;

public class RedirectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redirect);

        TextView connection = findViewById(R.id.connection);
        connection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RedirectActivity.this, LoginActivity.class));
            }
        });


        TextView signUp = findViewById(R.id.signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RedirectActivity.this, SignUpActivity.class));
            }
        });
    }

}
