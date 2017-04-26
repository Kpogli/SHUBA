package com.example.android.shuba1.com.example.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.android.shuba1.BusLocator;
import com.example.android.shuba1.R;
import com.example.android.shuba1.WaitingCommuter;
import com.example.android.shuba1.com.example.adapters.BusLocatorAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kennedy on 14/04/2017.
 */

public class Fragment1 extends Fragment{
    private TextView locationName;
    private TextView locationLatitude;
    private TextView locationLongitude;

    public static Double locationLatitudeActual;
    public static Double locationLongitudeActual;


    private RecyclerView recyclerView;
    private List<BusLocator> result;
    private BusLocatorAdapter adapter;

    public Fragment1() {
        // required empty public constructor
    }

    public static Fragment1 newInstance(String title, Double latitude, Double longitude) {
        Fragment1 fragment1 = new Fragment1();

        Bundle args = new Bundle();
        args.putString("title", title);


        //Bundle args1 = new Bundle();
        args.putDouble("latitude", latitude);


        //Bundle args2 = new Bundle();
        args.putDouble("longitude", longitude);


        fragment1.setArguments(args);
        //fragment1.setArguments(args1);
        //fragment1.setArguments(args2);

        return fragment1;
    }



    public String getTitle() {
        Bundle args = getArguments();
        return args.getString("title");
    }

    public Double getLatitude() {
        Bundle args = getArguments();
        return args.getDouble("latitude");
    }

    public Double getLongitude() {
        Bundle args = getArguments();
        return args.getDouble("longitude");
    }

    public void updateList() {
        FirebaseDatabase
                .getInstance()
                .getReference("buses")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        result.add(dataSnapshot.getValue(BusLocator.class));
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        BusLocator busLocator = dataSnapshot.getValue(BusLocator.class);
                        int index = getItemIndex(busLocator);
                        result.set(index, busLocator);
                        adapter.notifyItemChanged(index);
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        BusLocator busLocator = dataSnapshot.getValue(BusLocator.class);
                        int index = getItemIndex(busLocator);
                        result.remove(index);
                        adapter.notifyItemRemoved(index);
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private int getItemIndex(BusLocator busLocator) {
        int index = -1;

        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).driverEmail.equals(busLocator.driverEmail)) {
                index = i;
                break;
            }
        }
        return index;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment1_layout, container, false);

        //locationName = (TextView) v.findViewById(R.id.textView1);
        //locationLatitude = (TextView) v.findViewById(R.id.textView2);
        //locationLongitude = (TextView) v.findViewById(R.id.textView3);

        locationLatitudeActual = getLatitude();
        locationLongitudeActual = getLongitude();

        //locationName.setText(getTitle());
        //locationLatitude.setText(String.valueOf(getLatitude()));
        //locationLongitude.setText(String.valueOf(getLongitude()));

        result = new ArrayList<>();

        recyclerView = (RecyclerView) v.findViewById(R.id.bus_rv);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new BusLocatorAdapter(result);

        recyclerView.setAdapter(adapter);

        updateList();

        return v;
    }

    /*@Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locationName = (TextView) view.findViewById(R.id.textView1);
        locationLatitude = (TextView) view.findViewById(R.id.textView2);
        locationLongitude = (TextView) view.findViewById(R.id.textView3);

        locationName.setText(getTitle());
        locationLatitude.setText(String.valueOf(getLatitude()));
        locationLongitude.setText(String.valueOf(getLongitude()));

    }*/

}
