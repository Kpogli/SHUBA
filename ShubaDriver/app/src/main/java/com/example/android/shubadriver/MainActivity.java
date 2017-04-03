package com.example.android.shubadriver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView userName;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private Button buttonRefresh;
    private Button buttonRoute;
    private Button buttonMapView;

    private RecyclerView recyclerView;
    private List<WaitingCommuter> result;
    private WaitingCommuterAdapter adapter;

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

        result = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.waiting_commuters_rv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);

        createResult();

        adapter = new WaitingCommuterAdapter(result);

        recyclerView.setAdapter(adapter);

    }

    private void createResult(){

        for (int i = 0; i < 10; i++) {
            result.add(new WaitingCommuter("stopName", "waiterCount", ""));
        }

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
