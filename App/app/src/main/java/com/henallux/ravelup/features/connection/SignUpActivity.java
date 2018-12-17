package com.henallux.ravelup.features.connection;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.henallux.ravelup.R;
import com.henallux.ravelup.features.menus.MainRedirectActivity;
import com.henallux.ravelup.model.UserModel;

import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        //region DatePicker
        final TextView datePicker= findViewById(R.id.datePicker_signUpActivity);
        final DatePickerDialog.OnDateSetListener mDateSetListener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date= dayOfMonth+"/"+month+"/"+year;
                datePicker.setText(date);
            }
        };


        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal= Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        SignUpActivity.this,
                        android.R.style.Theme_Holo_Light_NoActionBar,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        //endregion

        //region errors handling
//        TextInputLayout login = findViewById(R.id.textInputLayout);
//        login.setError("mauvais login");
//        TextInputLayout motdepasse = findViewById(R.id.motDePasse);
//        motdepasse.setError("mauvais mot de passe");
//        TextInputLayout email = findViewById(R.id.email);
//        email.setError("mauvais email");
        //endregion

        //region inscription
        Button inscription = findViewById(R.id.boutonInscription_signUpActivity);
        inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //add in db
                UserModel user= new UserModel();
                TextInputLayout login = findViewById(R.id.login_signUpActivity);
                TextInputLayout motDePasse = findViewById(R.id.motDePasse_signUpActivity);
                TextInputLayout confirmeMotDePasse = findViewById(R.id.confirmeMotDePasse_signUpActivity);
                TextInputLayout email = findViewById(R.id.email_signUpActivity);
                //TextInputLayout dateNaissance = findViewById(R.id.datePicker_signUpActivity);

                user.setLogin(login.getEditText().getText().toString());
                user.setMotDePasse(motDePasse.getEditText().getText().toString());
                user.setConfirmeMotDePasse(confirmeMotDePasse.getEditText().getText().toString());
                user.seteMail(email.getEditText().getText().toString());


                //TODO changer les setters des classes userModel et loginModel
                startActivity(new Intent(SignUpActivity.this, MainRedirectActivity.class));
            }
        });
        //endregion
    }
}
