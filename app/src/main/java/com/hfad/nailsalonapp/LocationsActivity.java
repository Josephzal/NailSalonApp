package com.hfad.nailsalonapp;

import android.Manifest;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.lang.reflect.Executable;
import java.util.Arrays;
import java.util.List;

public class LocationsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";
    GoogleMap mMap;
    ListView stPlaces;
    private PlacesClient mPlacesClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private Location mLastKnownLocation;

    private final LatLng mDefaultLocation = new LatLng(28.4855, -81.4315);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    private static final int M_MAX_ENTRIES = 5;
    private String[] mLikelyPlaceNames;
    private String[] mLikelyPlaceAdresses;
    private String[] mLikelyPlaceAttributions;
    private LatLng[] mLikelyPlaceLatLngs;


//     Ensure proper layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        String apiKey = getString(R.string.google_map_key);
        Places.initialize(getApplicationContext(), "@string/APIKEY");
        mPlacesClient = Places.createClient(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

//     Ensure location exists
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_geolocate:
                pickCurrentPlace();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
// API Code
    private void getLocationPermission() {
        mLocationPermissionGranted = false;
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

// Generate map markers to be accessed    
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng orlando = new LatLng(28.4855, -81.4315);
        LatLng sanford = new LatLng(28.8029, -81.2695);
        LatLng altamonte = new LatLng(28.6611, -81.3656);
        mMap.addMarker(new MarkerOptions().position(orlando).title("Orlando Salon"));
        mMap.addMarker(new MarkerOptions().position(sanford).title("Sanford Salon"));
        mMap.addMarker(new MarkerOptions().position(altamonte).title("Altamonte Springs Salon"));
        float zoomLevel = 10.0f;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(altamonte, zoomLevel));

        mMap.getUiSettings().setZoomControlsEnabled(true);

        getLocationPermission();

    }
// API Code
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

//     API Code
    private void getCurrentPlaceLikelihoods() {
        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG);

        @SuppressWarnings("MissingPermission") final FindCurrentPlaceRequest request = FindCurrentPlaceRequest.builder(placeFields).build();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
         
            return;
        }
        Task<FindCurrentPlaceResponse> placeResponse = mPlacesClient.findCurrentPlace(request);
        placeResponse.addOnCompleteListener(this, new OnCompleteListener<FindCurrentPlaceResponse>() {
            @Override
            public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                if (task.isSuccessful()) {
                    FindCurrentPlaceResponse response = task.getResult();
                    int count;
                    if(response.getPlaceLikelihoods().size() < M_MAX_ENTRIES){
                        count = response.getPlaceLikelihoods().size();

                    }else{
                        count = M_MAX_ENTRIES;
                    }
                    int i = 0;
                    mLikelyPlaceNames = new String[count];
                    mLikelyPlaceAdresses = new String[count];
                    mLikelyPlaceAttributions = new String[count];
                    mLikelyPlaceLatLngs = new LatLng[count];

                    for(PlaceLikelihood placeLikelihood: response.getPlaceLikelihoods()){
                        Place currPlace = placeLikelihood.getPlace();
                        mLikelyPlaceNames[i] = currPlace.getName();
                        mLikelyPlaceAdresses[i] = currPlace.getAddress();
                        mLikelyPlaceAttributions[i] = (currPlace.getAttributions() == null) ? null : TextUtils.join(" ", currPlace.getAttributions());
                        mLikelyPlaceLatLngs[i] = currPlace.getLatLng();


                        String currLatLng = (mLikelyPlaceLatLngs[i] == null) ? " " : mLikelyPlaceLatLngs[i].toString();

                        Log.i(TAG, String.format("Place "+ currPlace.getName() + " has likelihood: " + placeLikelihood.getLikelihood() + " at " + currLatLng));

                        i++;

                        if(i > (count -1)){
                            break;
                        }
                    }

                }else{
                    Exception exception = task.getException();
                    if (exception instanceof ApiException){
                        ApiException apiException = (ApiException) exception;
                        Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                    }
                }
            }
        });
    }
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            Log.d(TAG, "Latitude: " + mLastKnownLocation.getLatitude());
                            Log.d(TAG, "Longitude: " + mLastKnownLocation.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                        }

                        getCurrentPlaceLikelihoods();
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    private void pickCurrentPlace() {
        if (mMap == null) {
            return;
        }

        if (mLocationPermissionGranted) {

            getDeviceLocation();
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.");

            // Add a default marker, because the user hasn't selected a place.
            mMap.addMarker(new MarkerOptions()
                    .title(getString(R.string.default_into_title))
                    .position(mDefaultLocation)
                    .snippet(getString(R.string.default_into_snippet)));

            // Prompt the user for permission.
            getLocationPermission();
        }
    }
}
