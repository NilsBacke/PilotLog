package com.plushundred.nils.pilotlog.Fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.plushundred.nils.pilotlog.DatabaseHelper;
import com.plushundred.nils.pilotlog.FlightLog;
import com.plushundred.nils.pilotlog.R;
import com.plushundred.nils.pilotlog.RecyclerViewAdapter;

import java.util.ArrayList;

public class LogbookFragment extends Fragment {

    ArrayList<FlightLog> list;
    RecyclerView recyclerView;
    DatabaseHelper db;
    RecyclerViewAdapter adapter;
    TextView noListText;
    ImageView customImage;

    public LogbookFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        container.removeAllViews();
        View view = inflater.inflate(R.layout.fragment_logbook, container, false);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);

        AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.android_appbar_layout);
        appBarLayout.setExpanded(true);

        Log.i("Logbook", "fragment");

        db = DatabaseHelper.getInstance(getActivity());
        list = new ArrayList<>();

        if (!db.getAllItems().isEmpty()) {
            list.addAll(db.getAllItems());
        }

        noListText = (TextView) view.findViewById(R.id.noLogsTextView);
        customImage = (ImageView) view.findViewById(R.id.customImage);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        adapter = new RecyclerViewAdapter(list, null, null);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), manager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noListText.setVisibility(View.INVISIBLE);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Log a Flight");

                LayoutInflater inflater = getActivity().getLayoutInflater();
                View mView = inflater.inflate(R.layout.create_log, null);
                builder.setView(mView);

                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Dialog d = (Dialog) dialog;
                        final EditText date = (EditText) d.findViewById(R.id.dateEditText);
                        final EditText aircraft = (EditText) d.findViewById(R.id.aircraftEditText);
                        final EditText ident = (EditText) d.findViewById(R.id.identEditText);
                        final EditText from = (EditText) d.findViewById(R.id.fromEditText);
                        final EditText to = (EditText) d.findViewById(R.id.toEditText);
                        final EditText dualDayHours = (EditText) d.findViewById(R.id.dualDayHoursEditText);
                        final EditText dualNightHours = (EditText) d.findViewById(R.id.dualNightHoursEditText);
                        final EditText soloDayHours = (EditText) d.findViewById(R.id.soloDayHoursEditText);
                        final EditText soloNightHours = (EditText) d.findViewById(R.id.soloNightHoursEditText);
                        final CheckBox crosscountry = (CheckBox) d.findViewById(R.id.crosscountryCheckBox);
                        final CheckBox simulatedInstrument = (CheckBox) d.findViewById(R.id.simulatedInstrumentCheckBox);
                        final EditText dayLandings = (EditText) d.findViewById(R.id.landingsDayEditText);
                        final EditText nightLandings = (EditText) d.findViewById(R.id.landingsNightEditText);
                        final EditText notes = (EditText) d.findViewById(R.id.notesEditText);

                        String datestr = date.getText().toString();
                        String aircraftstr = aircraft.getText().toString();
                        String identstr = ident.getText().toString();
                        String fromstr = from.getText().toString();
                        String tostr = to.getText().toString();

                        Double dualDay;
                        if (!dualDayHours.getText().toString().isEmpty()) {
                            dualDay = Double.parseDouble(dualDayHours.getText().toString());
                        } else {
                            dualDay = 0.;
                        }

                        Double dualNight;
                        if (!dualNightHours.getText().toString().isEmpty()) {
                            dualNight = Double.parseDouble(dualNightHours.getText().toString());
                        } else {
                            dualNight = 0.;
                        }

                        Double soloDay;
                        if (!soloDayHours.getText().toString().isEmpty()) {
                            soloDay = Double.parseDouble(soloDayHours.getText().toString());
                        } else {
                            soloDay = 0.;
                        }

                        Double soloNight;
                        if (!soloNightHours.getText().toString().isEmpty()) {
                            soloNight = Double.parseDouble(soloNightHours.getText().toString());
                        } else {
                            soloNight = 0.;
                        }

                        Boolean cc = crosscountry.isChecked();
                        Boolean siminstr = simulatedInstrument.isChecked();

                        Integer dayLand;
                        if (!dayLandings.getText().toString().isEmpty()) {
                            dayLand = Integer.parseInt(dayLandings.getText().toString());
                        } else {
                            dayLand = 0;
                        }

                        Integer nightLand;
                        if (!nightLandings.getText().toString().isEmpty()) {
                            nightLand = Integer.parseInt(nightLandings.getText().toString());
                        } else {
                            nightLand = 0;
                        }

                        String notesstr = notes.getText().toString();

                        FlightLog flight = new FlightLog(datestr, aircraftstr, identstr, fromstr, tostr, dualDay, dualNight,
                                soloDay, soloNight, cc, siminstr, dayLand, nightLand, notesstr);

                        list.add(flight);

                        db.addItem(flight);

                        updateList();

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                onResume();
            }
        });

        checkEmpty();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        int id = item.getItemId();

        switch (id) {
            case R.id.deleteAll:
                deleteAll();
                break;
            default:
                break;
        }

        return true;
    }

    public void updateList() {
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(list, null, null);
        recyclerView.setAdapter(adapter);
    }

    public void checkEmpty() {
        if (list.size() == 0) {
            noListText.setVisibility(View.VISIBLE);
            noListText.setText("Looks like you haven't added any flights yet! \n " +
                    "Tap on the action button below to make a flight log.");
        } else {
            noListText.setVisibility(View.INVISIBLE);
        }
    }


    public void deleteAll() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Clear logs?");
        builder.setMessage("Are you sure you want to delete all logged flights? All records will be lost.");

        builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                list.clear();
                db.clearDatabase();
                updateList();
                checkEmpty();
                Toast.makeText(getActivity(), "Logbook cleared.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
        onResume();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.setListener(new RecyclerViewAdapter.ItemListener() {
            @Override
            public void onItemClick(final int position, View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Log a Flight");

                LayoutInflater inflater = getActivity().getLayoutInflater();
                View mView = inflater.inflate(R.layout.create_log, null);
                builder.setView(mView);

                final EditText date = (EditText) mView.findViewById(R.id.dateEditText);
                final EditText aircraft = (EditText) mView.findViewById(R.id.aircraftEditText);
                final EditText ident = (EditText) mView.findViewById(R.id.identEditText);
                final EditText from = (EditText) mView.findViewById(R.id.fromEditText);
                final EditText to = (EditText) mView.findViewById(R.id.toEditText);
                final EditText dualDayHours = (EditText) mView.findViewById(R.id.dualDayHoursEditText);
                final EditText dualNightHours = (EditText) mView.findViewById(R.id.dualNightHoursEditText);
                final EditText soloDayHours = (EditText) mView.findViewById(R.id.soloDayHoursEditText);
                final EditText soloNightHours = (EditText) mView.findViewById(R.id.soloNightHoursEditText);
                final CheckBox crosscountry = (CheckBox) mView.findViewById(R.id.crosscountryCheckBox);
                final CheckBox simulatedInstrument = (CheckBox) mView.findViewById(R.id.simulatedInstrumentCheckBox);
                final EditText dayLandings = (EditText) mView.findViewById(R.id.landingsDayEditText);
                final EditText nightLandings = (EditText) mView.findViewById(R.id.landingsNightEditText);
                final EditText notes = (EditText) mView.findViewById(R.id.notesEditText);

                date.setText(list.get(position).getDate());
                aircraft.setText(list.get(position).getAircraft());
                ident.setText(list.get(position).getIdent());
                from.setText(list.get(position).getFrom());
                to.setText(list.get(position).getTo());
                dualDayHours.setText(list.get(position).getDualDayHours().toString());
                dualNightHours.setText(list.get(position).getDualNightHours().toString());
                soloDayHours.setText(list.get(position).getSoloDayHours().toString());
                soloNightHours.setText(list.get(position).getSoloNightHours().toString());
                crosscountry.setChecked(list.get(position).getCrosscountry());
                simulatedInstrument.setChecked(list.get(position).getSimulatedInstrument());
                dayLandings.setText(list.get(position).getDayLandings().toString());
                nightLandings.setText(list.get(position).getNightLandings().toString());
                notes.setText(list.get(position).getNotes());

                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String datestr = date.getText().toString();
                        String aircraftstr = aircraft.getText().toString();
                        String identstr = ident.getText().toString();
                        String fromstr = from.getText().toString();
                        String tostr = to.getText().toString();

                        Double dualDay;
                        if (!dualDayHours.getText().toString().isEmpty()) {
                            dualDay = Double.parseDouble(dualDayHours.getText().toString());
                        } else {
                            dualDay = 0.;
                        }

                        Double dualNight;
                        if (!dualNightHours.getText().toString().isEmpty()) {
                            dualNight = Double.parseDouble(dualNightHours.getText().toString());
                        } else {
                            dualNight = 0.;
                        }

                        Double soloDay;
                        if (!soloDayHours.getText().toString().isEmpty()) {
                            soloDay = Double.parseDouble(soloDayHours.getText().toString());
                        } else {
                            soloDay = 0.;
                        }

                        Double soloNight;
                        if (!soloNightHours.getText().toString().isEmpty()) {
                            soloNight = Double.parseDouble(soloNightHours.getText().toString());
                        } else {
                            soloNight = 0.;
                        }

                        Boolean cc = crosscountry.isChecked();
                        Boolean siminstr = simulatedInstrument.isChecked();

                        Integer dayLand;
                        if (!dayLandings.getText().toString().isEmpty()) {
                            dayLand = Integer.parseInt(dayLandings.getText().toString());
                        } else {
                            dayLand = 0;
                        }

                        Integer nightLand;
                        if (!nightLandings.getText().toString().isEmpty()) {
                            nightLand = Integer.parseInt(nightLandings.getText().toString());
                        } else {
                            nightLand = 0;
                        }

                        String notesstr = notes.getText().toString();

                        FlightLog flight = new FlightLog(datestr, aircraftstr, identstr, fromstr, tostr, dualDay, dualNight,
                                soloDay, soloNight, cc, siminstr, dayLand, nightLand, notesstr);

                        list.set(position, flight);

                        db.clearDatabase();

                        for (FlightLog log : list) {
                            db.addItem(log);
                        }

                        updateList();

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });

        adapter.setLongListener(new RecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(final int position, View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Remove Log");
                builder.setMessage("Are you sure you want to delete the flight log for " + list.get(position).getDate() +
                        "? All data for this log will be lost.");

                builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list.remove(position);

                        db.clearDatabase();

                        for (FlightLog log : list) {
                            db.addItem(log);
                        }

                        updateList();
                        Toast.makeText(getActivity(), "Flight log removed.", Toast.LENGTH_SHORT).show();
                        onResume();

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                return false;
            }
        });
    }

}
