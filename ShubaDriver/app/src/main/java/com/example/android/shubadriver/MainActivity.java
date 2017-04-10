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

import com.google.android.gms.vision.text.Text;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView userName;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private Button buttonRefresh;
    private Button buttonRoute;
    private Button buttonMapView;

    private RecyclerView recyclerView;
    private List<WaitingCommuter> result;
    private WaitingCommuterAdapter adapter;

    private TextView emptyText;


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

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("stops");

        userName = (TextView) findViewById(R.id.driver_name);
        userName.setText(user.getDisplayName());

        buttonRefresh = (Button) findViewById(R.id.refresh_button);
        buttonRoute = (Button) findViewById(R.id.route_button);
        buttonMapView = (Button) findViewById(R.id.map_button);

        emptyText = (TextView) findViewById(R.id.text_no_data);

        buttonRefresh.setOnClickListener(this);
        buttonRoute.setOnClickListener(this);
        buttonMapView.setOnClickListener(this);

        result = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.waiting_commuters_rv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new WaitingCommuterAdapter(result);

        recyclerView.setAdapter(adapter);

        updateList();
        updateWaiterCountFB();
        checkIfEmpty();

    }

    private void updateWaiterCountFB() {
        FirebaseDatabase
                .getInstance()
                .getReference("count")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        FirebaseDatabase
                                .getInstance()
                                .getReference("stops")
                                .child(dataSnapshot.getKey())
                                .child("waiterCount")
                                .setValue(dataSnapshot.child("waiterCount").getValue());
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        FirebaseDatabase
                                .getInstance()
                                .getReference("stops")
                                .child(dataSnapshot.getKey())
                                .child("waiterCount")
                                .setValue(dataSnapshot.child("waiterCount").getValue());
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

    private void updateList() {
        databaseReference.orderByChild("longitude").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                result.add(dataSnapshot.getValue(WaitingCommuter.class));
                adapter.notifyDataSetChanged();
                checkIfEmpty();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                WaitingCommuter waitingCommuter = dataSnapshot.getValue(WaitingCommuter.class);
                int index = getItemIndex(waitingCommuter);
                result.set(index, waitingCommuter);
                adapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                WaitingCommuter waitingCommuter = dataSnapshot.getValue(WaitingCommuter.class);
                int index = getItemIndex(waitingCommuter);
                result.remove(index);
                adapter.notifyItemRemoved(index);

                checkIfEmpty();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private int getItemIndex(WaitingCommuter waitingCommuter) {
        int index = -1;

        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).longitude.equals(waitingCommuter.longitude)) {
                index = i;
                break;
            }
        }
        return index;
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

    private void checkIfEmpty() {
        if (result.size() == 0) {
            recyclerView.setVisibility(View.INVISIBLE);
            emptyText.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
