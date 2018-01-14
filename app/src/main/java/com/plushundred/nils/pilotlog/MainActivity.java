package com.plushundred.nils.pilotlog;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.plushundred.nils.pilotlog.Fragments.FlightMapFragment;
import com.plushundred.nils.pilotlog.Fragments.LogbookFragment;
import com.plushundred.nils.pilotlog.Fragments.SettingsFragment;
import com.plushundred.nils.pilotlog.Fragments.SummaryFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<FlightLog> list;
    DatabaseHelper db;
    DrawerLayout drawer;
    ImageView customImage;
    Toolbar toolbar;
    ImageView headerImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.plushundred.nils.pilotlog.R.layout.activity_main);
        toolbar = (Toolbar) findViewById(com.plushundred.nils.pilotlog.R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(com.plushundred.nils.pilotlog.R.id.collapsingToolbarLayout);

        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(com.plushundred.nils.pilotlog.R.style.ExpandedAppBar);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(com.plushundred.nils.pilotlog.R.style.CollapsedAppBar);
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(com.plushundred.nils.pilotlog.R.style.ExpandedAppBarPlus1);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(com.plushundred.nils.pilotlog.R.style.CollapsedAppBarPlus1);

        drawer = (DrawerLayout) findViewById(com.plushundred.nils.pilotlog.R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, com.plushundred.nils.pilotlog.R.string.navigation_drawer_open, com.plushundred.nils.pilotlog.R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(com.plushundred.nils.pilotlog.R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();

        customImage = (ImageView) findViewById(com.plushundred.nils.pilotlog.R.id.customImage);
        headerImage = (ImageView) findViewById(R.id.headerImage);

        db = DatabaseHelper.getInstance(this);
        list = new ArrayList<>();

        if (!db.getAllItems().isEmpty()) {
            Log.i("list", db.getAllItems().toString());
            list.addAll(db.getAllItems());
        }

        Log.i("Replace", "replace");
        getFragmentManager().beginTransaction().replace(com.plushundred.nils.pilotlog.R.id.content_frame, new LogbookFragment()).commit();
        navigationView.getMenu().getItem(0).setChecked(true);

        getHeaderImage();

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Log.i("Logbook", "here");


        if (id == com.plushundred.nils.pilotlog.R.id.nav_home) {
            LogbookFragment fragment = new LogbookFragment();
            getFragmentManager().beginTransaction()
                    .replace(com.plushundred.nils.pilotlog.R.id.content_frame, fragment)
                    .commit();
            toolbar.setTitle("Pilot Log");
        } else if (id == com.plushundred.nils.pilotlog.R.id.nav_summary) {
            SummaryFragment fragment = new SummaryFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(com.plushundred.nils.pilotlog.R.id.content_frame, fragment)
                    .commit();
            toolbar.setTitle("Logbook Summary");
        } else if (id == com.plushundred.nils.pilotlog.R.id.nav_flightmap) {
            FlightMapFragment fragment = new FlightMapFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(com.plushundred.nils.pilotlog.R.id.content_frame, fragment)
                    .commit();
            toolbar.setTitle("Flight Map");
        } else if (id == com.plushundred.nils.pilotlog.R.id.nav_settings) {
            SettingsFragment fragment = new SettingsFragment();
            getFragmentManager().beginTransaction()
                    .replace(com.plushundred.nils.pilotlog.R.id.content_frame, fragment)
                    .commit();
            toolbar.setTitle("Settings");
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(com.plushundred.nils.pilotlog.R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void imageClick(View view) {
        Toast.makeText(this, "The header picture can be altered in the settings menu.", Toast.LENGTH_SHORT).show();
    }

    public void getHeaderImage() {
        SharedPreferences sharedpreferences = getSharedPreferences("first_preferencescreen", Context.MODE_PRIVATE);

        if (sharedpreferences.contains("selectImage")) {
            byte[] imageAsBytes = Base64.decode(sharedpreferences.getString("selectImage", "").getBytes(), 0);
            customImage.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

            // Added to pair header image with custom image
            headerImage.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        }
    }

    public ArrayList<FlightLog> getDatabaseList() {
        return db.getAllItems();
    }

}
