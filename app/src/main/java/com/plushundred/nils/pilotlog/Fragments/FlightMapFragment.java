package com.plushundred.nils.pilotlog.Fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plushundred.nils.pilotlog.AirportLatLng;
import com.plushundred.nils.pilotlog.FlightLog;
import com.plushundred.nils.pilotlog.MainActivity;
import com.plushundred.nils.pilotlog.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlightMapFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;
    LocationManager manager;

    public FlightMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        container.removeAllViews();
        mView = inflater.inflate(R.layout.fragment_flight_map, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(false);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.android_appbar_layout);
        appBarLayout.setExpanded(false);

        mMapView = (MapView) mView.findViewById(R.id.map);
        mMapView.setVisibility(View.VISIBLE);

        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getActivity());
        mGoogleMap = googleMap;

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String mapStyle = pref.getString("mapstyle", "");

        Log.i("String", mapStyle);

        switch (mapStyle) {
            case "1":
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case "2":
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case "3":
                googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case "4":
                googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            default:
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }

        ArrayList<FlightLog> list = ((MainActivity) getActivity()).getDatabaseList();


        String mostCommonAirport = mostCommonAirport(list);

        new RetrieveLatLngForAirport(mGoogleMap).execute(mostCommonAirport);


        for (FlightLog log : list) {
            new RetrieveLatLng(log, mGoogleMap).execute(log.getFrom(), log.getTo());
        }
    }

    private class RetrieveLatLng extends AsyncTask<String, Void, String[]> {

        private GoogleMap googleMap;
        private FlightLog log;

        public RetrieveLatLng(FlightLog log, GoogleMap googleMap) {
            this.log = log;
            this.googleMap = googleMap;
        }

        protected String[] doInBackground(String... locations) {
            try {
                AirportLatLng findLatLng = new AirportLatLng();
                String[] fromLatLng = findLatLng.getLatLongPositions(locations[0]);
                String[] toLatLng = findLatLng.getLatLongPositions(locations[1]);
                String[] bothLatLng = {fromLatLng[0], fromLatLng[1], toLatLng[0], toLatLng[1]};
                Log.i("Array", Arrays.toString(bothLatLng));
                return bothLatLng;
            } catch (Exception e) {
                e.printStackTrace();

                return null;
            }
        }

        protected void onPostExecute(String[] latLng) {
            Double fromLat = Double.parseDouble(latLng[0]);
            Double fromLng = Double.parseDouble(latLng[1]);

            Double toLat = Double.parseDouble(latLng[2]);
            Double toLng = Double.parseDouble(latLng[3]);

            googleMap.addPolyline(new PolylineOptions().add(new LatLng(fromLat, fromLng), new LatLng(toLat, toLng)).width(10).color(Color.RED));

            googleMap.addMarker(new MarkerOptions().position(new LatLng(fromLat, fromLng)).title(log.getFrom()));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(toLat, toLng)).title(log.getTo()));
        }
    }

    private class RetrieveLatLngForAirport extends AsyncTask<String, Void, String[]> {

        GoogleMap googleMap;

        public RetrieveLatLngForAirport(GoogleMap googleMap) {
            this.googleMap = googleMap;
        }

        protected String[] doInBackground(String... locations) {
            try {
                AirportLatLng findLatLng = new AirportLatLng();
                String[] latLng = findLatLng.getLatLongPositions(locations[0]);
                return latLng;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(String[] latLng) {
            Double lat = Double.parseDouble(latLng[0]);
            Double lng = Double.parseDouble(latLng[1]);

            CameraPosition camera = CameraPosition.builder().target(new LatLng(lat, lng)).zoom(6).bearing(0).tilt(0).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));

        }
    }

    private String mostCommonAirport(List<FlightLog> flightLogs) {

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
