package com.example.android.shubadriver;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private DatabaseReference stopsRef = FirebaseDatabase.getInstance().getReference("stops");

    private ArrayList<LatLng> locationStops;
    private ArrayList<String> locationTitles;

    private LocationManager locationManager;

    private StopLocator stopLocator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            Intent backToLogin = new Intent(this, LoginActivity.class);
            startActivity(backToLogin);
        }

        final FirebaseUser user = firebaseAuth.getCurrentUser();

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
        mMap = googleMap;

        // Getting LocationManager object from System Service LOCATION_SERVICE
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        if (!isLocationEnabled()) {
            showAlert();
        } else {

        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, locationListener);



        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);


        trackStops(mMap);


        //Floating action bar stuff
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){

            ArrayList<LatLng> locStops = locationStops;
            ArrayList<String> locTitles = locationTitles;
            int i = 0;

            @Override
            public void onClick(View view) {
                if (i == locTitles.size()) {
                    i = 0;
                }

                mMap.addMarker(new MarkerOptions().position(locStops.get(i)).title(locTitles.get(i))).showInfoWindow();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locStops.get(i),18.0f));

                Toast.makeText(getApplicationContext(),"Stop is at "+ locTitles.get(i),Toast.LENGTH_SHORT).show();

                /*mMap.addMarker(new MarkerOptions().position(locStops.get(i)).title(locTitles.get(i))).showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(locStops.get(i)));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));*/

                i++;
            }
        });

    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            Toast.makeText(getApplicationContext(),"changed", Toast.LENGTH_SHORT).show();

            final FirebaseUser user = firebaseAuth.getCurrentUser();

            FirebaseDatabase.getInstance()
                    .getReference("buses")
                    .child(user.getUid())
                    .setValue(new BusLocator(user.getDisplayName(), user.getEmail(), location.getLatitude(), location.getLongitude()));


        }
        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }
        @Override
        public void onProviderEnabled(String s) {
        }
        @Override
        public void onProviderDisabled(String s) {
        }
    };


    private void trackStops(final GoogleMap mMap) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //final ArrayList<LatLng> locStops;
        locationStops = new ArrayList<>();

        //final ArrayList<String> locNames;
        locationTitles = new ArrayList<>();

        childEventListener = stopsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                stopLocator = dataSnapshot.getValue(StopLocator.class);
                String stopName = stopLocator.getStopName();
                Double latitude = stopLocator.getLatitude();
                Double longitude = stopLocator.getLongitude();
                LatLng stop = new LatLng(latitude,longitude);

                locationStops.add(stop);
                locationTitles.add(stopName);

                mMap.addMarker(new MarkerOptions().position(stop).title(stopName)).showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(stop));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(stop,18.0f));

                //Toast.makeText(getApplicationContext(), "Last stop is "+stopName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

}
