package com.henallux.ravelup.features.map;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.henallux.ravelup.R;

public class MapMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_map);

    // slider
        // SeekBar
        SeekBar rayon = findViewById(R.id.seekBarRayon );
         final TextView rayonText = findViewById(R.id.rayonText);

        rayonText.setText("Rayon : " + rayon.getProgress()+"m");
        //
        rayon.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            int progress = 0;

            // When Progress value changed.
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;

                rayonText.setText("rayon : "+progressValue+"m");
            }
            // Notification that the user has finished a touch gesture
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                rayonText.setText("rayon : "+progress+"m");

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
        });


        ImageView helpButton = findViewById(R.id.menu_carte_activity_help_button);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO changer le string
                final Snackbar snackbar = Snackbar.make(findViewById(R.id.menu_carte_activity_help_button), "j'ai pas d'idée", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }
        });




    //button MapActivity
        Button map = findViewById(R.id.boutonToCarte);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMenu =new Intent(MapMenuActivity.this, MapActivity.class);
                startActivityForResult(goToMenu,1);
            }
        });
    }
}
