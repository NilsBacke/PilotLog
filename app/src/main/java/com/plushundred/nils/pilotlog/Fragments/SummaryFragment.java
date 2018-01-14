package com.plushundred.nils.pilotlog.Fragments;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.plushundred.nils.pilotlog.FlightLog;
import com.plushundred.nils.pilotlog.MainActivity;
import com.plushundred.nils.pilotlog.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SummaryFragment extends Fragment {

    public SummaryFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        container.removeAllViews();
        View view = inflater.inflate(R.layout.fragment_summary, container, false);
        setHasOptionsMenu(false);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.android_appbar_layout);
        appBarLayout.setExpanded(true);

        TextView totalHours = (TextView) view.findViewById(R.id.totalHoursTextView);
        TextView soloHours = (TextView) view.findViewById(R.id.soloHoursTextView);
        TextView ifrHours = (TextView) view.findViewById(R.id.totalSimInstrHours);
        TextView xcHours = (TextView) view.findViewById(R.id.totalCrossCountryHoursTextView);
        TextView numLandings = (TextView) view.findViewById(R.id.numofLandingsTextView);
        TextView favAircraft = (TextView) view.findViewById(R.id.favoriteAircrafTextView);
        TextView hoursInFavAircraft = (TextView) view.findViewById(R.id.hoursInFavAircraftTextView);
        TextView homeAirport = (TextView) view.findViewById(R.id.homeAirportTextView);

        ArrayList<FlightLog> list = ((MainActivity)getActivity()).getDatabaseList();

        Log.i("Summary", list.toString());

        double sumHours = 0;
        double solo = 0;
        double ifr = 0;
        double xc = 0;
        double favAircraftHours = 0;
        int landings = 0;

        String aircraft = mostCommonAircraft(list);

        for (FlightLog log : list) {
            sumHours += log.getTotalHours();
            solo += (log.getSoloDayHours() + log.getSoloNightHours());
            if (log.getSimulatedInstrument()) {
                ifr += log.getTotalHours();
            }
            if (log.getCrosscountry()) {
                xc += log.getTotalHours();
            }
            if (log.getAircraft().equals(aircraft)) {
                favAircraftHours += log.getTotalHours();
            }
            landings += (log.getDayLandings() + log.getNightLandings());
        }

        totalHours.setText(Double.toString(sumHours));
        soloHours.setText(Double.toString(solo));
        ifrHours.setText(Double.toString(ifr));
        xcHours.setText(Double.toString(xc));
        numLandings.setText(Integer.toString(landings));
        favAircraft.setText(aircraft);
        hoursInFavAircraft.setText(Double.toString(favAircraftHours));
        homeAirport.setText(mostCommonAirport(list));


        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    // half working?
    public String mostCommonAircraft(List<FlightLog> flightLogs) {

        ArrayList<String> list = new ArrayList<>();

        for (FlightLog log : flightLogs) {
            list.add(log.getAircraft());
        }

        Map<String, Integer> map = new HashMap<String, Integer>();

        for(int i = 0; i < list.size(); i++){
            if(map.get(list.get(i)) == null){
                map.put(list.get(i), 1);
            }else{
                map.put(list.get(i), map.get(list.get(i)) + 1);
            }
        }
        int largest = 0;
        String stringOfLargest = "";
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String key = entry.getKey();
            int value = entry.getValue();
            if( value > largest){
                largest = value;
                stringOfLargest = key;
            }
        }

        return stringOfLargest;

    }

    // half working?
    public String mostCommonAirport(List<FlightLog> flightLogs) {

        ArrayList<String> list = new ArrayList<>();

        for (FlightLog log : flightLogs) {
            list.add(log.getTo());
            list.add(log.getFrom());
        }

        Map<String, Integer> map = new HashMap<String, Integer>();

        for(int i = 0; i < list.size(); i++){
            if(map.get(list.get(i)) == null){
                map.put(list.get(i), 1);
            }else{
                map.put(list.get(i), map.get(list.get(i)) + 1);
            }
        }
        int largest = 0;
        String stringOfLargest = "";
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String key = entry.getKey();
            int value = entry.getValue();
            if( value > largest){
                largest = value;
                stringOfLargest = key;
            }
        }

        return stringOfLargest;

    }

}
