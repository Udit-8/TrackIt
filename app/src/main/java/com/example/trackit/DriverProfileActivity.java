package com.example.trackit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.atomic.AtomicBoolean;

public class DriverProfileActivity extends AppCompatActivity {
    TextInputLayout driverNameLayout, busNumberLayout, driverContactLayout;
    TextView headerDriverFullNameTextView;
    Driver loginDriver;
    MaterialCardView driverChangePasswordCardView;
    Button startTrackingButton;
    Toolbar driverToolbar;
    static boolean isTracking = false;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        loginDriver = (Driver) intent.getSerializableExtra("loginDriver");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        logout();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);
        onNewIntent(getIntent());
        driverToolbar = findViewById(R.id.driverToolbar);
        driverNameLayout = findViewById(R.id.driverFullNameLayout);
        busNumberLayout = findViewById(R.id.busNumberLayout);
        driverContactLayout = findViewById(R.id.driverContactNoLayout);
        headerDriverFullNameTextView = findViewById(R.id.headerDriverNameText);
        setDriverFields(loginDriver);
        driverChangePasswordCardView = findViewById(R.id.driverChangePasswordCard);
        startTrackingButton = findViewById(R.id.startTrackingButton);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }

        if(isTracking)
            startTrackingButton.setText("Stop Tracking");
        else
            startTrackingButton.setText("Start Tracking");
        startTrackingButton.setOnClickListener(v->{
            if(!isTracking)
            {
                Log.i("isTracking","is Tracking called");
                startTrackingButton.setText("Stop Tracking");
                isTracking = true;
                Intent newIntent = new Intent(DriverProfileActivity.this,ForegroundService.class);
                newIntent.putExtra("loginDriver",loginDriver);
                startService(newIntent);
            }
            else
            {
                startTrackingButton.setText("Start Tracking");
                isTracking = false;
                Intent serviceIntent = new Intent(this, ForegroundService.class);
                stopService(serviceIntent);

            }
        });
        driverToolbar.inflateMenu(R.menu.menu);
        driverToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.action_logout:
                        logout();
                }
                return false;
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
    protected void onStop() {
        super.onStop();
        if(!isTracking)
            logout();
    }

    private void logout() {
        isTracking = false;
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        stopService(serviceIntent);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("loginDrivers").child(loginDriver.getDriverBusNumber());
        ref.removeValue();
        Intent intent = new Intent(DriverProfileActivity.this,LoginActivity.class);
        startActivity(intent);
    }


    private void setDriverFields(Driver driver) {
        driverNameLayout.getEditText().setText(driver.getDriverName());
        busNumberLayout.getEditText().setText(driver.getDriverBusNumber());
        driverContactLayout.getEditText().setText(driver.getDriverMobileNumber());
        headerDriverFullNameTextView.setText(driver.getDriverName());
    }
}