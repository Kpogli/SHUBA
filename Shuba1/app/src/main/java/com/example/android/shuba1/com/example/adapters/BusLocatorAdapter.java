package com.example.android.shuba1.com.example.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.shuba1.BusLocator;
import com.example.android.shuba1.R;

import java.util.List;

/**
 * Created by Kennedy on 18/04/2017.
 */

public class BusLocatorAdapter extends RecyclerView.Adapter<BusLocatorAdapter.BusLocatorViewHolder> {

    private List<BusLocator> list;

    public BusLocatorAdapter(List<BusLocator> list) { this.list = list; }

    @Override
    public BusLocatorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BusLocatorViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bus, parent, false));
    }

    @Override
    public void onBindViewHolder(BusLocatorViewHolder holder, int position) {
        final BusLocator busLocator = list.get(position);
        holder.textDriverName.setText(busLocator.driverName);
        holder.textDistance.setText("Distance");
        holder.textETA.setText(busLocator.speed+" KmpH");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class BusLocatorViewHolder extends RecyclerView.ViewHolder {
        TextView textDriverName, textDistance, textETA;

        public BusLocatorViewHolder(View itemView) {
            super(itemView);

            textDriverName = (TextView) itemView.findViewById(R.id.driverName);
            textDistance = (TextView) itemView.findViewById(R.id.distanceAway);
            textETA = (TextView) itemView.findViewById(R.id.eta);

        }
    }

}
