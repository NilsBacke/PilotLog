package com.plushundred.nils.pilotlog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Nils on 8/6/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "logbook.db";
    public static final String TABLE_ITEMS = "table_logbook";
    public static final String COL_1 = "date";
    public static final String COL_2 = "aircraft";
    public static final String COL_3 = "ident";
    public static final String COL_4 = "dep";
    public static final String COL_5 = "arr";
    public static final String COL_6 = "dualDayHours";
    public static final String COL_7 = "dualNightHours";
    public static final String COL_8 = "soloDayHours";
    public static final String COL_9 = "soloNightHours";
    public static final String COL_10 = "crosscountry";
    public static final String COL_11 = "simulatedinstrument";
    public static final String COL_12 = "dayLandings";
    public static final String COL_13 = "nightLandings";
    public static final String COL_14 = "notes";

    private static DatabaseHelper sInstance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * A new DatabaseHelper object is created from a given context.
     * @param context The given context.
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    /**
     * This method is called when a DatabaseHelper object is constructed.
     * The table is generated from each of the initialized columns.
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_ITEMS + " (" + COL_1 + " TEXT," + COL_2 + " TEXT," + COL_3 +
                " TEXT," + COL_4 +  " TEXT," + COL_5 + " TEXT," + COL_6 + " TEXT," + COL_7 + " TEXT," + COL_8 + " TEXT," + COL_9 + " TEXT," +
                COL_10 + " TEXT," + COL_11 + " TEXT," + COL_12 + " TEXT," + COL_13 + " TEXT," +  COL_14 + " TEXT" + ");");
    }

    /**
     * This method is called when the database table is upgraded.
     * @param db The database.
     * @param oldVersion The old version number of the table.
     * @param newVersion The new version number of the table.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }

    /**
     * This method adds an item to the full item list data table.
     * Each piece of data of an item is put into a separate row.
     * @param item The new item.
     */
    public void addItem(FlightLog item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_1, item.getDate());
        values.put(COL_2, item.getAircraft());
        values.put(COL_3, item.getIdent());
        values.put(COL_4, item.getFrom());
        values.put(COL_5, item.getTo());
        values.put(COL_6, item.getDualDayHours());
        values.put(COL_7, item.getDualNightHours());
        values.put(COL_8, item.getSoloDayHours());
        values.put(COL_9, item.getSoloNightHours());
        if (item.getCrosscountry()) {
            values.put(COL_10, "true");
        } else {
            values.put(COL_10, "false");
        }

        if (item.getSimulatedInstrument()) {
            values.put(COL_11, "true");
        } else {
            values.put(COL_11, "false");
        }
        values.put(COL_12, item.getDayLandings());
        values.put(COL_13, item.getNightLandings());
        values.put(COL_14, item.getNotes());
        db.insert(TABLE_ITEMS, null, values);
        db.close(); // Closing database connection
    }

    /**
     * This method returns an ArrayList of all of the items stored in the full list table.
     * @return The ArrayList of items.
     */
    public ArrayList<FlightLog> getAllItems() {
        ArrayList<FlightLog> itemlist = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_ITEMS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FlightLog item = new FlightLog();
                item.setDate(cursor.getString(0));
                item.setAircraft(cursor.getString(1));
                item.setIdent(cursor.getString(2));
                item.setFrom(cursor.getString(3));
                item.setTo(cursor.getString(4));
                item.setDualDayHours(Double.parseDouble(cursor.getString(5)));
                item.setDualNightHours(Double.parseDouble(cursor.getString(6)));
                item.setSoloDayHours(Double.parseDouble(cursor.getString(7)));
                item.setSoloNightHours(Double.parseDouble(cursor.getString(8)));

                if (cursor.getString(9).equals("true")) {
                    item.setCrosscountry(true);
                } else {
                    item.setCrosscountry(false);
                }

                if (cursor.getString(10).equals("true")) {
                    item.setSimulatedInstrument(true);
                } else {
                    item.setSimulatedInstrument(false);
                }

                item.setDayLandings(Integer.parseInt(cursor.getString(11)));
                item.setNightLandings(Integer.parseInt(cursor.getString(12)));
                item.setNotes(cursor.getString(13));
                // Adding item to list
                itemlist.add(item);
            } while (cursor.moveToNext());
        }
        // return item list
        return itemlist;
    }

    /**
     * This method clears the full item list database.
     */
    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        String clearDBQuery = "DELETE FROM " + TABLE_ITEMS;
        db.execSQL(clearDBQuery);
    }

}
