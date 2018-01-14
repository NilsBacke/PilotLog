package com.plushundred.nils.pilotlog;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<FlightLog> mItems;
    private static ItemListener mListener;
    private static OnItemLongClickListener longListener;

    public RecyclerViewAdapter(List<FlightLog> items, ItemListener listener, OnItemLongClickListener longListener) {
        mItems = items;
        mListener = listener;
        this.longListener = longListener;
    }

    public void setListener(ItemListener listener) {
        mListener = listener;
    }

    public void setLongListener(OnItemLongClickListener listener) {
        longListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(com.plushundred.nils.pilotlog.R.layout.list_element, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public TextView date, aircraft, ident, route, totalHours, crosscountry, siminstr;
        public FlightLog item;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            date = (TextView) itemView.findViewById(com.plushundred.nils.pilotlog.R.id.dateTextView);
            aircraft = (TextView) itemView.findViewById(com.plushundred.nils.pilotlog.R.id.aircraftTextView);
            ident = (TextView) itemView.findViewById(com.plushundred.nils.pilotlog.R.id.identTextView);
            route = (TextView) itemView.findViewById(com.plushundred.nils.pilotlog.R.id.routeTextView);
            totalHours = (TextView) itemView.findViewById(com.plushundred.nils.pilotlog.R.id.totalHoursTextView);
            crosscountry = (TextView) itemView.findViewById(com.plushundred.nils.pilotlog.R.id.crosscountryTextView);
            siminstr = (TextView) itemView.findViewById(com.plushundred.nils.pilotlog.R.id.simulatedInstrumentTextView);
        }

        public void setData(FlightLog item) {
            this.item = item;
            date.setText(item.getDate());
            aircraft.setText(item.getAircraft());
            ident.setText(item.getIdent());
            route.setText(item.getFrom() + "  -->  " + item.getTo());
            totalHours.setText("Hours: " + item.getTotalHours().toString());
            if (item.getCrosscountry()) {
                crosscountry.setText("XC");
            } else {
                crosscountry.setText("");
            }
            if (item.getSimulatedInstrument()) {
                siminstr.setText("INSTR.");
            } else {
                siminstr.setText("");
            }
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(getPosition(), v);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            Log.i("Adapter", "onLongnull");
            if (longListener != null) {
                Log.i("Adapter", "onLong");
                longListener.onItemLongClicked(getPosition(), v);
            }
            return true;
        }
    }

    public interface ItemListener {
        void onItemClick(int position, View view);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClicked(int position, View view);
    }
}
