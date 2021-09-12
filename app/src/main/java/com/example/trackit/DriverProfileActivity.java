package com.example.trackit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class DriverProfileActivity extends AppCompatActivity {
    TextInputLayout driverNameLayout, busNumberLayout, driverContactLayout;
    TextView headerDriverFullNameTextView;
    LocationManager locationManager;
    LocationListener locationListener;
    FusedLocationProviderClient fusedLocationProviderClient;
    Driver loginDriver;
    Location curLocation;
    DatabaseReference ref;
    MaterialCardView driverChangePasswordCardView;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);
        Log.i("OnCreate","Activity Created");
        Intent intent = getIntent();
        loginDriver = (Driver) intent.getSerializableExtra("loginDriver");
        driverNameLayout = findViewById(R.id.driverFullNameLayout);
        busNumberLayout = findViewById(R.id.busNumberLayout);
        driverContactLayout = findViewById(R.id.driverContactNoLayout);
        headerDriverFullNameTextView = findViewById(R.id.headerDriverNameText);
        setDriverFields(loginDriver);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        driverChangePasswordCardView = findViewById(R.id.driverChangePasswordCard);
       fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
         ref = FirebaseDatabase.getInstance().getReference().child("Active Buses").child(loginDriver.getDriverBusNumber());


        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                curLocation = location;
                    setLocation(location);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
        };
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
        }

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {

                        setLocation(location);


                }
            }
        });
        driverChangePasswordCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(DriverProfileActivity.this,ChangePasswordActivity.class);
                newIntent.putExtra("isStudent",false);
                newIntent.putExtra("loginDriver",loginDriver);
                startActivity(newIntent);
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("OnStart","Activity Started");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("OnResume","Activity Resumed");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("OnPause","Activity Paused");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("OnStop","Activity Stopped");
        ref.removeValue();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i("OnDestroy","Activity Destroyed");
    }

    private void setLocation(Location location) {
        ref.child("Latitude").setValue(String.valueOf(location.getLatitude()));
        ref.child("Longitude").setValue(String.valueOf(location.getLongitude()));
    }

    private void setDriverFields(Driver driver) {
        driverNameLayout.getEditText().setText(driver.getDriverName());
        busNumberLayout.getEditText().setText(driver.getDriverBusNumber());
        driverContactLayout.getEditText().setText(driver.getDriverMobileNumber());
        headerDriverFullNameTextView.setText(driver.getDriverName());
    }
}