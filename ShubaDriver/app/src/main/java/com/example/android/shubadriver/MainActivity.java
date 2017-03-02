package com.example.android.shubadriver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView userName;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private Button buttonRefresh;
    private Button buttonRoute;
    private Button buttonMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            Intent backToLogin = new Intent(this, LoginActivity.class);
            startActivity(backToLogin);
        }

        final FirebaseUser user = firebaseAuth.getCurrentUser();

        userName = (TextView) findViewById(R.id.driver_name);
        userName.setText(user.getDisplayName());

        buttonRefresh = (Button) findViewById(R.id.refresh_button);
        buttonRoute = (Button) findViewById(R.id.route_button);
        buttonMapView = (Button) findViewById(R.id.map_button);

        buttonRefresh.setOnClickListener(this);
        buttonRoute.setOnClickListener(this);
        buttonMapView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == buttonRefresh) {
            firebaseAuth.signOut();
            finish();
            Intent out = new Intent(this, LoginActivity.class);
            startActivity(out);
        }

        if (view == buttonRoute) {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
        }

        if (view == buttonMapView) {
            Intent mapView = new Intent(this, MapsActivity.class);
            startActivity(mapView);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
