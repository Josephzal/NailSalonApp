

package com.hfad.nailsalonapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;


public class AppointmentActivity extends AppCompatActivity {

    Button confirmBtn;
    Button homeBtn;
    public static String nameString;
    public static String phoneString;
    public static String locationString;
    public static String timeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        Spinner spinnerLocations = (Spinner) findViewById(R.id.locations);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterLocations = ArrayAdapter.createFromResource(this,
                R.array.locations_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterLocations.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerLocations.setAdapter(adapterLocations);

        Spinner spinnerTimes = (Spinner) findViewById(R.id.times);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterTimes = ArrayAdapter.createFromResource(this,
                R.array.times_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterTimes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerTimes.setAdapter(adapterTimes);

        confirmBtn = (Button) findViewById(R.id.confirmBtn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openConfirmationActivity();
            }
        });

        homeBtn = (Button) findViewById(R.id.homeBtnApt);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });
    }

    public void openConfirmationActivity(){
        Intent intent = new Intent(this, ConfirmationActivity.class);
        EditText nameEdit =  (EditText) findViewById(R.id.name);
        nameString = (String) nameEdit.getText().toString();
        intent.putExtra("nameText", nameString);

        EditText phoneEdit =  (EditText) findViewById(R.id.phone);
        phoneString = (String) phoneEdit.getText().toString();
        intent.putExtra("phoneText", phoneString);

        Spinner locationSpinner = (Spinner) findViewById(R.id.locations);
        locationString = locationSpinner.getSelectedItem().toString();
        intent.putExtra("locationText", locationString);

        Spinner timeSpinner = (Spinner) findViewById(R.id.times);
        timeString = timeSpinner.getSelectedItem().toString();
        intent.putExtra("timeText", timeString);

        startActivity(intent);

    }

    public String getNameString() {
        return nameString;
    }

    public String getPhoneString() {
        return phoneString;
    }

    public String getLocationString() {
        return locationString;
    }

    public String getTimeString() {
        return timeString;
    }


    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}





