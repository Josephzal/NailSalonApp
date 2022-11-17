package com.hfad.nailsalonapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ConfirmationActivity extends AppCompatActivity {

    Button homeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        TextView nameConfirm = findViewById(R.id.nameConfirm);
        TextView phoneConfirm = findViewById(R.id.phoneConfirm);
        TextView locationConfirm = findViewById(R.id.locationConfirm);
        TextView timeConfirm = findViewById(R.id.timeConfirm);

        nameConfirm.setText(getIntent().getStringExtra("nameText"));
        phoneConfirm.setText(getIntent().getStringExtra("phoneText"));
        locationConfirm.setText(getIntent().getStringExtra("locationText"));
        timeConfirm.setText(getIntent().getStringExtra("timeText"));

        homeBtn = (Button) findViewById(R.id.homeBtn);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });

    }

    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}