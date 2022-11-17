package com.hfad.nailsalonapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    Button aptBtn;
    Button locationsBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aptBtn = (Button) findViewById(R.id.aptBtn);
        locationsBtn = (Button) findViewById(R.id.locationsBtn);

        aptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAptActivity();
            }
        });

        locationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLocationsActivity();
            }
        });
    }

    public void openAptActivity(){
        Intent intent = new Intent(this, AppointmentActivity.class);
        startActivity(intent);
    }

    public void openLocationsActivity(){
        Intent intent = new Intent(this, LocationsActivity.class);
        startActivity(intent);
    }

}

