package com.example.android.shubadriver;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Kennedy on 03/04/2017.
 */

public class WaitingCommuterAdapter extends RecyclerView.Adapter<WaitingCommuterAdapter.WaitingCommuterViewHolder> {

    private List<WaitingCommuter> list;

    public WaitingCommuterAdapter(List<WaitingCommuter> list) {
        this.list = list;
    }

    @Override
    public WaitingCommuterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WaitingCommuterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_waiting_commuter, parent, false));
    }

    @Override
    public void onBindViewHolder(WaitingCommuterViewHolder holder, int position) {
        WaitingCommuter waitingCommuter = list.get(position);
        holder.textStopName.setText(waitingCommuter.stopName);
        holder.textWaiterCount.setText(waitingCommuter.waiterCount);
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
