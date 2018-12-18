package com.henallux.ravelup.features.connection;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.henallux.ravelup.R;
import com.henallux.ravelup.dao.dataacess.ConnectionDAO;
import com.henallux.ravelup.features.menus.MainRedirectActivity;
import com.henallux.ravelup.model.TokenReceived;
import com.henallux.ravelup.model.UserModel;

import java.util.Calendar;
import java.util.Date;

public class SignUpActivity extends AppCompatActivity {

    TokenReceived mTokenReceived = new TokenReceived();

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

        //region inscription
        Button inscription = findViewById(R.id.boutonInscription_signUpActivity);
        inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //add in db
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

                /*TextInputLayout password = findViewById(R.id.motDePasse_signUpActivity);
                String passwordValue = password.getEditText().getText().toString();
                if(!passwordValue.isEmpty()){
                    user.setPassword(passwordValue);
                } else {
                    login.setError("Ce champs ne peut être vide");
                    hasError=true;
                }*/

               /* TextInputLayout confirmedPassword = findViewById(R.id.confirmeMotDePasse_signUpActivity);
                String confirmedPasswordValue = confirmedPassword.getEditText().getText().toString();
                if(!confirmedPasswordValue.isEmpty()){
                    user.setPasswordConfirm(confirmedPasswordValue);
                } else {
                    login.setError("Ce champs ne peut être vide");
                    hasError=true;
                }*/

                TextInputLayout email = findViewById(R.id.email_signUpActivity);
                String emailValue = email.getEditText().getText().toString();
                if(!emailValue.isEmpty()){
                    user.setEmail(emailValue);
                } else {
                    login.setError("Ce champs ne peut être vide");
                    hasError=true;
                }

                /*user.setPassword("Test789_si");
                user.setPasswordConfirm("Test789_si");
                user.setDateNaissance(new Date(1998,2,20));*/

                if(!hasError){
                    SecondSignUp signUp = new SecondSignUp();
                    signUp.execute(user);
                }
            }
        });
        //endregion
    }

    private class SignUp extends AsyncTask<UserModel,Void,TokenReceived> {
        private ConnectionDAO connectionDAO = new ConnectionDAO();

        @Override
        protected TokenReceived doInBackground(UserModel... params){

            TokenReceived tokenReceived = new TokenReceived();

            try{
                tokenReceived = connectionDAO.signUp(params[0]);
            }
            catch(Exception e){
                /*Toast*/
            }

            return tokenReceived;
        }

        @Override
        protected void onPostExecute(TokenReceived tokenReceived) {
            mTokenReceived = tokenReceived;
            if (mTokenReceived.getErrorException().equals("") && mTokenReceived.getCode() == 200) {
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                        .edit()
                        .putString("token",mTokenReceived.getToken())
                        .apply();

                startActivity(new Intent(SignUpActivity.this, MainRedirectActivity.class));
            }
        }
    }

    private class SecondSignUp extends  AsyncTask<UserModel,Void,Void>{

        private ConnectionDAO connectionDAO = new ConnectionDAO();

        @Override
        protected Void doInBackground(UserModel... params) {
            connectionDAO.secondSignUp(params[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
           startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
        }
    }
}
