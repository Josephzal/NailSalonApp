package com.hfad.nailsalonapp;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.content.ContentValues;

class nailSalonDatabaseHelper extends SQLiteOpenHelper{

    private static final String DB_NAME = "nailsalon"; // the name of our database
    private static final int DB_VERSION = 2; // the version of the database

    nailSalonDatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        updateMyDatabase(db, 0, DB_VERSION);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        insertAppointment(db, "John Smith", "1234567890", "Orlando", "11:00am");
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private static void insertAppointment(SQLiteDatabase db, String name,
                                          String phone, String location, String time) {
        ContentValues appValues = new ContentValues();
        appValues.put("NAME", name);
        appValues.put("PHONE", phone);
        appValues.put("LOCATION", location);
        appValues.put("TIME", time);
        db.insert("DRINK", null, appValues);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE APPOINTMENTS (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "NAME TEXT," +
                    "PHONE TEXT," +
                    "LOCATION TEXT," +
                    "TIME TEXT);");
            insertAppointment(db, "John Smith", "1234567890", "Orlando", "11:00am");
        }
        if (oldVersion < 2) {
            //Code to Edit appointments in database
            ContentValues appValues = new ContentValues();
            appValues.put("NAME", AppointmentActivity.nameString);
            appValues.put("PHONE", AppointmentActivity.phoneString);
            appValues.put("LOCATION", AppointmentActivity.locationString);
            appValues.put("TIME", AppointmentActivity.timeString);

            db.update("APPOINTMENTS", appValues, "NAME = ? OR PHONE = ? OR LOCATION = ? OR TIME = ?",
                    new String[] {"John Smith", "1234567890", "Orlando", "11:00am"});
        }
    }

}
