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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment1_layout, container, false);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locationName = (TextView) view.findViewById(R.id.textView1);
        locationLatitude = (TextView) view.findViewById(R.id.textView2);
        locationLongitude = (TextView) view.findViewById(R.id.textView3);

        locationName.setText(getTitle());
        locationLatitude.setText(String.valueOf(getLatitude()));
        locationLongitude.setText(String.valueOf(getLongitude()));


    }

}
