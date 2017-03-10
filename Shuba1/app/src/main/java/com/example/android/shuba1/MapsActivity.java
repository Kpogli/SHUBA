package com.example.android.shuba1;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {


    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    private TextView email;
    private TextView userName;
    private GoogleMap mMap;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private Toolbar toolbar;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private DatabaseReference busesRef = FirebaseDatabase.getInstance().getReference("buses");
    private DatabaseReference stopsRef = FirebaseDatabase.getInstance().getReference("stops");
    private static final int MY_LOCATION_REQUEST_CODE = 1;
    private Criteria criteria;
    private Location location;
    private LatLng latLng;
    private LatLng myPosition;

    private ArrayList<LatLng> locationStops;
    private ArrayList<String> locationTitles;

    //private String nameOfCurrentUser;

    private int stopCount = 0;

    private BusLocator busLocator;
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

        toolbar = (Toolbar) findViewById(R.id.app_bar_map);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.TRANSPARENT);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);


        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View header = navigationView.getHeaderView(0);
        userName = (TextView) header.findViewById(R.id.user_name);
        userName.setText(user.getDisplayName());
        email = (TextView) header.findViewById(R.id.email);
        email.setText(user.getEmail());
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.map:
                        drawerLayout.closeDrawers();
                        item.setChecked(false);
                        break;


                    case R.id.nearby_stops:
                        //Intent a = new Intent(MapsActivity.this, RegistrationActivity.class);
                        drawerLayout.closeDrawers();
                        //startActivity(a);
                        break;

                    case R.id.crowd:
                        Intent crowd = new Intent(MapsActivity.this, CrowdActivity.class);
                        startActivity(crowd);
                        drawerLayout.closeDrawers();
                        item.setChecked(false);
                        break;

                    case R.id.settings:
                        //Intent c = new Intent(MapsActivity.this, LoginActivity.class);
                        drawerLayout.closeDrawers();
                        //startActivity(c);
                        break;

                    case R.id.help_and_feedback:
                        //Intent d = new Intent(MapsActivity.this, LoginActivity.class);
                        drawerLayout.closeDrawers();
                        //startActivity(d);
                        break;

                    case R.id.sign_out_option:
                        drawerLayout.closeDrawers();
                        firebaseAuth.signOut();
                        finish();
                        Intent signOut = new Intent(MapsActivity.this, LoginActivity.class);
                        startActivity(signOut);
                        break;


                }

                return true;
            }
        });


    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
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

        // Show buses on map
        trackBuses(mMap);

        // Show stops on map
        trackStops(mMap);

        // Creating a criteria object to retrieve provider
        criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            // Getting latitude of the current location
            double latitude = location.getLatitude();

            // Getting longitude of the current location
            double longitude = location.getLongitude();

            // Creating a LatLng object for the current location
            latLng = new LatLng(latitude, longitude);

            myPosition = new LatLng(latitude, longitude);

            //mMap.addMarker(new MarkerOptions().position(myPosition).title("Start"));
        }


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);


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

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    private void trackBuses(final GoogleMap mMap) {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final Map<String, Marker> markers = new HashMap<>();

        childEventListener = busesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                busLocator = dataSnapshot.getValue(BusLocator.class);
                String driverName = busLocator.getDriverName();
                Double latitude = busLocator.getLatitude();
                Double longitude = busLocator.getLongitude();
                Long timestamp = busLocator.getTimestamp();
                LatLng bus = new LatLng(latitude,longitude);

                MarkerOptions busMarkerOptions = new MarkerOptions();
                busMarkerOptions.position(bus);
                busMarkerOptions.title(driverName);
                busMarkerOptions.snippet("Estimated time to next stop");

                busMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_shuttle_black_25px));

                Marker marker = mMap.addMarker(busMarkerOptions);
                markers.put(dataSnapshot.getKey(), marker);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {


                busLocator = dataSnapshot.getValue(BusLocator.class);
                String driverName = busLocator.getDriverName();
                Double latitude = busLocator.getLatitude();
                Double longitude = busLocator.getLongitude();
                Long timestamp = busLocator.getTimestamp();
                LatLng bus = new LatLng(latitude,longitude);

                MarkerOptions busMarkerOptions = new MarkerOptions();
                busMarkerOptions.position(bus);
                busMarkerOptions.title(driverName);
                busMarkerOptions.snippet("Estimated time to next stop");

                if (markers.containsKey(dataSnapshot.getKey())) {
                    markers.get(dataSnapshot.getKey()).remove();
                }

                busMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_shuttle_black_25px));

                Marker marker = mMap.addMarker(busMarkerOptions);
                markers.put(dataSnapshot.getKey(), marker);
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



    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
