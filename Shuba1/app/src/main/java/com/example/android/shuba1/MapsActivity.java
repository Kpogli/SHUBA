package com.example.android.shuba1;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

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

        // Add a marker at Commercial Area and move the camera
        LatLng commercialArea = new LatLng(6.683119, -1.574864);
        mMap.addMarker(new MarkerOptions().position(commercialArea).title("Stop at Commercial Area"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(commercialArea));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(commercialArea, 12.0f));

        // Add marker at Shuttle rank and move the camera
        LatLng shuttleRank = new LatLng(6.682863, -1.577085);
        mMap.addMarker(new MarkerOptions().position(shuttleRank).title("Shuttle Rank"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(shuttleRank));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(shuttleRank, 12.0f));

        // Add marker at Hall 7 and move the camera
        LatLng hall7behind = new LatLng(6.679700, -1.574115);
        mMap.addMarker(new MarkerOptions().position(hall7behind).title("Stop at Hall 7"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(hall7behind));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hall7behind, 12.0f));

        // Add marker at Hall 7 front and move the camera
        LatLng hall7front = new LatLng(6.679881, -1.573343);
        mMap.addMarker(new MarkerOptions().position(hall7front).title("Stop at Hall 7 (Front)"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(hall7front));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hall7front, 12.0f));

        // Add marker at Indece Supermarket and move the camera
        LatLng indeceSupermarket = new LatLng(6.677682, -1.572628);
        mMap.addMarker(new MarkerOptions().position(indeceSupermarket).title("Stop at Indece Supermarket"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(indeceSupermarket));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(indeceSupermarket, 12.0f));

        // Add marker at Indece and move the camera
        LatLng indece = new LatLng(6.677043, -1.572327);
        mMap.addMarker(new MarkerOptions().position(indece).title("Stop at Indece"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(indece));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(indece, 12.0f));

        // Add marker at Republic Hall and move the camera
        LatLng repu = new LatLng(6.677629, -1.573808);
        mMap.addMarker(new MarkerOptions().position(repu).title("Stop at Republic Hall"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(repu));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(repu, 12.0f));

        // Add marker at Queens Hall and move the camera
        LatLng queens = new LatLng(6.677267, -1.573883);
        mMap.addMarker(new MarkerOptions().position(queens).title("Stop at Queens Hall"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(queens));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(queens, 12.0f));

        // Add marker at Katanga/University Hall and move the camera
        LatLng kat = new LatLng(6.672905, -1.573109);
        mMap.addMarker(new MarkerOptions().position(kat).title("Stop at Katanga/University Hall"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(kat));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(kat, 12.0f));

        // Add marker at Brunei and move the camera
        LatLng brunei = new LatLng(6.670991, -1.573241);
        mMap.addMarker(new MarkerOptions().position(brunei).title("Stop at Brunei"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(brunei));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(brunei, 12.0f));

        // Add marker at IDL Shuttle Rank and move the camera
        LatLng idlShuttleRank = new LatLng(6.675111, -1.567562);
        mMap.addMarker(new MarkerOptions().position(idlShuttleRank).title("Stop at IDL Shuttle Rank"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(idlShuttleRank));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(idlShuttleRank, 12.0f));

        // Add marker at College of Engineering labs and move the camera
        LatLng engineeringLabs = new LatLng(6.672538, -1.567575);
        mMap.addMarker(new MarkerOptions().position(engineeringLabs).title("Stop at College of Engineering Labs"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(engineeringLabs));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(engineeringLabs, 12.0f));

        // Add marker at Business School and move the camera
        LatLng businessSchool = new LatLng(6.669804, -1.567042);
        mMap.addMarker(new MarkerOptions().position(businessSchool).title("Stop at Business School"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(businessSchool));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(businessSchool, 12.0f));

    }
}
