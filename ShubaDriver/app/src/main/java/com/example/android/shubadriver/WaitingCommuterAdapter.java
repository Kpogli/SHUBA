package com.example.android.shubadriver;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by Kennedy on 03/04/2017.
 */

public class WaitingCommuterAdapter extends RecyclerView.Adapter<WaitingCommuterAdapter.WaitingCommuterViewHolder> {

    private List<WaitingCommuter> list;
    private boolean mProcessWait = false;
    private boolean mProcessWaitCount = false;
    private boolean mProcessWaitCountToZero = false;



    public WaitingCommuterAdapter(List<WaitingCommuter> list) {
        this.list = list;
    }

    @Override
    public WaitingCommuterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WaitingCommuterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_waiting_commuter, parent, false));
    }

    @Override
    public void onBindViewHolder(WaitingCommuterViewHolder holder, int position) {
        final WaitingCommuter waitingCommuter = list.get(position);
        holder.textStopName.setText(waitingCommuter.stopName);
        holder.textWaiterCount.setText(String.valueOf(waitingCommuter.waiterCount));

        holder.clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mProcessWait = true;
                mProcessWaitCount = true;
                mProcessWaitCountToZero = true;
                final int zero = 0;


                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .child("waiting")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (mProcessWait) {
                                    if (dataSnapshot.child(waitingCommuter.stopName).hasChildren()) {
                                        FirebaseDatabase
                                                .getInstance()
                                                .getReference()
                                                .child("waiting")
                                                .child(waitingCommuter.stopName)
                                                .removeValue();
                                        mProcessWait = false;
                                    } else {
                                        mProcessWait = false;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .child("count")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (mProcessWaitCount) {
                                    if (dataSnapshot.child(waitingCommuter.stopName).hasChildren()) {
                                        FirebaseDatabase
                                                .getInstance()
                                                .getReference()
                                                .child("count")
                                                .child(waitingCommuter.stopName)
                                                .removeValue();
                                        mProcessWaitCount = false;
                                    } else {
                                        mProcessWaitCount = false;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                FirebaseDatabase
                        .getInstance()
                        .getReference("stops")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (mProcessWaitCountToZero) {
                                    if (dataSnapshot.child(waitingCommuter.stopName).hasChildren()) {
                                        FirebaseDatabase
                                                .getInstance()
                                                .getReference()
                                                .child("stops")
                                                .child(waitingCommuter.stopName)
                                                .child("waiterCount")
                                                .setValue(zero);
                                        mProcessWaitCountToZero = false;
                                    } else {
                                        mProcessWaitCountToZero = false;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class WaitingCommuterViewHolder extends RecyclerView.ViewHolder {

        TextView textStopName, textWaiterCount;
        Button clearButton;

        public WaitingCommuterViewHolder(View itemView) {
            super(itemView);

            textStopName = (TextView) itemView.findViewById(R.id.stopName);
            textWaiterCount = (TextView) itemView.findViewById(R.id.waiterCount);
            clearButton = (Button) itemView.findViewById(R.id.clearButton);

        }
    }
}
