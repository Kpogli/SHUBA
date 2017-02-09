package com.example.android.shubadriver;

import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private Criteria criteria;
    private Location location;
    private LatLng latLng;
    private LatLng myPosition;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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
        mMap = googleMap;

        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Getting LocationManager object from System Service LOCATION_SERVICE
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

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

            mMap.addMarker(new MarkerOptions().position(myPosition).title("Start"));
        }

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
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        // Add marker at Hall 7 and move the camera
        final LatLng hall7behind = new LatLng(6.679647, -1.574016);
        mMap.addMarker(new MarkerOptions().position(hall7behind).title("Stop at Hall 7"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(hall7behind));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hall7behind, 12.0f));

        // Add marker at Hall 7 front and move the camera
        final LatLng hall7front = new LatLng(6.679944, -1.573445);
        mMap.addMarker(new MarkerOptions().position(hall7front).title("Stop at Hall 7 (Front)"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(hall7front));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hall7front, 12.0f));

        // Add marker at Indece Supermarket and move the camera
        final LatLng indeceSupermarket = new LatLng(6.677614, -1.572615);
        mMap.addMarker(new MarkerOptions().position(indeceSupermarket).title("Stop at Indece Supermarket"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(indeceSupermarket));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(indeceSupermarket, 12.0f));

        // Add marker at Indece and move the camera
        final LatLng indece = new LatLng(6.677314, -1.572282);
        mMap.addMarker(new MarkerOptions().position(indece).title("Stop at Indece"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(indece));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(indece, 12.0f));

        // Add marker at Republic Hall and move the camera
        final LatLng repu = new LatLng(6.677554, -1.573758);
        mMap.addMarker(new MarkerOptions().position(repu).title("Stop at Republic Hall"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(repu));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(repu, 12.0f));

        // Add marker at Queens Hall and move the camera
        final LatLng queens = new LatLng(6.677069, -1.573906);
        mMap.addMarker(new MarkerOptions().position(queens).title("Stop at Queens Hall"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(queens));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(queens, 12.0f));

        // Add marker at Katanga/University Hall and move the camera
        final LatLng kat = new LatLng(6.672936, -1.573097);
        mMap.addMarker(new MarkerOptions().position(kat).title("Stop at Katanga/University Hall"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(kat));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(kat, 12.0f));

        // Add marker at Brunei and move the camera
        final LatLng brunei = new LatLng(6.671046, -1.573256);
        mMap.addMarker(new MarkerOptions().position(brunei).title("Stop at Brunei"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(brunei));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(brunei, 12.0f));

        // Add marker at IDL Shuttle Rank and move the camera
        final LatLng idlShuttleRank = new LatLng(6.675086, -1.567547);
        mMap.addMarker(new MarkerOptions().position(idlShuttleRank).title("Stop at IDL Shuttle Rank"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(idlShuttleRank));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(idlShuttleRank, 12.0f));

        // Add marker at College of Engineering labs and move the camera
        final LatLng engineeringLabs = new LatLng(6.672564, -1.567601);
        mMap.addMarker(new MarkerOptions().position(engineeringLabs).title("Stop at College of Engineering Labs"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(engineeringLabs));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(engineeringLabs, 12.0f));

        // Add marker at Business School and move the camera
        final LatLng businessSchool = new LatLng(6.669804, -1.567013);
        mMap.addMarker(new MarkerOptions().position(businessSchool).title("Stop at Business School"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(businessSchool));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(businessSchool, 12.0f));

        // Add a marker at Commercial Area and move the camera
        final LatLng commercialArea = new LatLng(6.682973, -1.575663);
        mMap.addMarker(new MarkerOptions().position(commercialArea).title("Stop at Commercial Area"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(commercialArea));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(commercialArea, 18.0f));


        // Add marker at Shuttle rank and move the camera
        final LatLng shuttleRank = new LatLng(6.682714, -1.576977);
        mMap.addMarker(new MarkerOptions().position(shuttleRank).title("Shuttle Rank"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(shuttleRank));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(shuttleRank, 18.0f));

    }
}
