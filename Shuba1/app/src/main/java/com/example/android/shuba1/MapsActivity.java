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
    private Location markerLocation;
    private LatLng latLng;
    private LatLng myPosition;

    public static ArrayList<LatLng> locationStops;
    public static ArrayList<Double> locationLatitudes;
    public static ArrayList<Double> locationLongitudes;
    public static ArrayList<String> locationTitles;

    //private String nameOfCurrentUser;

    private int stopCount = 0;
    private boolean mProcessWait = false;

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
                        Intent nearbyStops = new Intent(MapsActivity.this, NearbyStopsActivity.class);
                        startActivity(nearbyStops);
                        drawerLayout.closeDrawers();
                        item.setChecked(false);
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

        // Update waiter count
        updateWaiterCount();

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

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

                //This does not include the option that you should be near the stop
                LatLng markerPosition = marker.getPosition();
                markerLocation = new Location("marker");
                markerLocation.setLatitude(markerPosition.latitude);
                markerLocation.setLongitude(markerPosition.longitude);
                marker.remove();
                String markerLocationTitle = marker.getTitle();
                refreshMarkers();
                stayAlert(markerLocationTitle);

                //This includes the condition that you should be near the stop
                /*if (!isLocationEnabled()) {
                    showAlert();
                } else {
                    if (location == null) {
                        LatLng markerPosition = marker.getPosition();
                        markerLocation = new Location("marker");
                        markerLocation.setLatitude(markerPosition.latitude);
                        markerLocation.setLongitude(markerPosition.longitude);
                        marker.remove();
                        Toast.makeText(getApplicationContext(), "Establish Position 1st", Toast.LENGTH_SHORT).show();
                        //stayAlert();
                        refreshMarkers();
                    } else {
                        LatLng markerPosition = marker.getPosition();
                        markerLocation = new Location("marker");
                        markerLocation.setLatitude(markerPosition.latitude);
                        markerLocation.setLongitude(markerPosition.longitude);
                        marker.remove();
                        String markerLocationTitle = marker.getTitle();
                        float range = location.distanceTo(markerLocation);
                        //stayAlert();
                        refreshMarkers();
                        if (range > 100) {
                            Toast.makeText(getApplicationContext(), "You are "+ range +" metres out of range! Get closer.", Toast.LENGTH_SHORT).show();
                        } else {
                            stayAlert(markerLocationTitle);
                        }


                        //Toast.makeText(getApplicationContext(), "You are "+ range +" metres away! Get closer.", Toast.LENGTH_SHORT).show();

                    }
                }
*/
            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

            }
        });


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

                mMap.addMarker(new MarkerOptions().draggable(true).position(locStops.get(i)).title(locTitles.get(i))).showInfoWindow();
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

    private void updateWaiterCount() {
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("waiting")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()) {
                            Iterable<DataSnapshot> waitingChildren = dataSnapshot.getChildren();
                            for (DataSnapshot waitingChild : waitingChildren) {
                                FirebaseDatabase
                                        .getInstance()
                                        .getReference()
                                        .child("count")
                                        .child(waitingChild.getKey())
                                        .child("waiterCount")
                                        .setValue(waitingChild.getChildrenCount());
                            }
                        } else {

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void stayAlert(final String markerLocationTitle) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("You are at a Shuttle Stop")
                .setMessage("Will you wait for a Shuttle Bus here?")
                .setPositiveButton("Yes. Please hurry!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final FirebaseUser user = firebaseAuth.getCurrentUser();

                        mProcessWait = true;


                            FirebaseDatabase
                                    .getInstance()
                                    .getReference()
                                    .child("waiting")
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (mProcessWait) {
                                                if (dataSnapshot.child(markerLocationTitle).hasChild(user.getUid())) {
                                                    mProcessWait = false;
                                                } else {
                                                    FirebaseDatabase
                                                            .getInstance()
                                                            .getReference()
                                                            .child("waiting")
                                                            .child(markerLocationTitle)
                                                            .child(user.getUid())
                                                            .setValue("RandomValue");

                                                    mProcessWait = false;
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                        /*FirebaseDatabase.getInstance()
                                .getReference()
                                .child("waiting")
                                .child(markerLocationTitle)
                                .child(user.getUid())
                                .child("waiter")
                                .setValue(user.getEmail());
*/
                        Toast.makeText(getApplicationContext(), "A bus is on the way. Patience please.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        dialog.show();
    }

    private void refreshMarkers() {
        for (int x = 0; x < locationTitles.size(); x++) {
            mMap.addMarker(new MarkerOptions().draggable(true).position(locationStops.get(x)).title(locationTitles.get(x)));
        }
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
                float speed = busLocator.getSpeed();
                Long timestamp = busLocator.getTimestamp();
                LatLng bus = new LatLng(latitude,longitude);

                MarkerOptions busMarkerOptions = new MarkerOptions();
                busMarkerOptions.position(bus);
                busMarkerOptions.title(driverName);
                busMarkerOptions.snippet(speed+" KmpH");

                busMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_shuttle_black_24dp));

                Marker marker = mMap.addMarker(busMarkerOptions);
                markers.put(dataSnapshot.getKey(), marker);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {


                busLocator = dataSnapshot.getValue(BusLocator.class);
                String driverName = busLocator.getDriverName();
                Double latitude = busLocator.getLatitude();
                Double longitude = busLocator.getLongitude();
                float speed = busLocator.getSpeed();
                Long timestamp = busLocator.getTimestamp();
                LatLng bus = new LatLng(latitude,longitude);

                MarkerOptions busMarkerOptions = new MarkerOptions();
                busMarkerOptions.position(bus);
                busMarkerOptions.title(driverName);
                busMarkerOptions.snippet(speed+" KmpH");

                if (markers.containsKey(dataSnapshot.getKey())) {
                    markers.get(dataSnapshot.getKey()).remove();
                }

                busMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_shuttle_black_24dp));

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

        locationLatitudes = new ArrayList<>();
        locationLongitudes = new ArrayList<>();

        //final ArrayList<LatLng> locStops;
        locationStops = new ArrayList<>();

        //final ArrayList<String> locNames;
        locationTitles = new ArrayList<>();

        childEventListener = stopsRef.orderByChild("longitude").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                stopLocator = dataSnapshot.getValue(StopLocator.class);

                String stopName = stopLocator.getStopName();
                locationTitles.add(stopName);

                Double latitude = stopLocator.getLatitude();
                locationLatitudes.add(latitude);

                Double longitude = stopLocator.getLongitude();
                locationLongitudes.add(longitude);

                LatLng stop = new LatLng(latitude,longitude);
                locationStops.add(stop);

                mMap.addMarker(new MarkerOptions().position(stop).title(stopName).draggable(true)).showInfoWindow();
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
