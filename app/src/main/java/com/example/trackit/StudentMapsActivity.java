package com.example.trackit;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.trackit.databinding.ActivityStudentMapsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityStudentMapsBinding binding;
    FusedLocationProviderClient fusedLocationProviderClient;
    DatabaseReference ref;
    Marker marker;
    ValueEventListener valueEventListener;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            getCurrentLocation();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStudentMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i("onMapReady","On Map ready called");
        mMap = googleMap;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Intent intent = getIntent();
        String busNumber = intent.getStringExtra("busNumber");
        // Add a marker in Sydney and move the camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            getCurrentLocation();
        }
        ref = FirebaseDatabase.getInstance().getReference().child("Active Buses").child(busNumber);
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Double driverLatitude = Double.valueOf(snapshot.child("Latitude").getValue(String.class));
                    Double driverLongitude = Double.valueOf(snapshot.child("Longitude").getValue(String.class));
                    LatLng driverLatLng = new LatLng(driverLatitude,driverLongitude);
                    if(marker != null)
                        marker.remove();
                    else
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(driverLatLng,12));
                    marker = mMap.addMarker(new MarkerOptions().position(driverLatLng).title("Driver's Location")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus)));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudentMapsActivity.this, "Your driver has stopped tracking their location", Toast.LENGTH_SHORT).show();
            }
        };
        if(ref != null)
        {
            ref.addValueEventListener(valueEventListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ref.removeEventListener(valueEventListener);
    }

    private void getCurrentLocation()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        LatLng curLatLng = new LatLng(location.getLatitude(),location.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(curLatLng).title("Your Location"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curLatLng,12));
                    }
                }
            });
        }

    }
}