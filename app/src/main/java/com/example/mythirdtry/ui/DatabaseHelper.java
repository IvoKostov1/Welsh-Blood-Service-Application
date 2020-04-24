package com.example.mythirdtry.ui;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Registration.db";
    public static final String TABLE_NAME = "Users_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "USERNAME";
    public static final String COL_3 = "PASSWORD";
    public static final String COL_4 = "EMAIL";

    public static final String UPDATED_USER_DETAILS = "Updated_user_details";
    public static final String UPDATED_COL_1 = "Updated_ID";
    public static final String UPDATED_COL_2 = "Name";
    public static final String UPDATED_COL_3 = "Surname";
    public static final String UPDATED_COL_4 = "Age";
    public static final String UPDATED_COL_5 = "Address";
    public static final String UPDATED_COL_6 = "Blood_type";
    public static final String UPDATED_COL_7 = "Mobile_number";
    public static final String UPDATED_COL_8 = "Username";

    public static final String BOOKING_DETAILS = "Booking_details";
    public static final String BOOKING_COL_1 = "Booking_ID";
    public static final String BOOKING_COL_2 = "City";
    public static final String BOOKING_COL_3 = "Clinic";
    public static final String BOOKING_COL_4 = "Doctor";
    public static final String BOOKING_COL_5 = "Date_and_time";
    public static final String BOOKING_COL_6 = "Booking_username";


    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, 2);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT," +
                "PASSWORD TEXT," + "EMAIL TEXT)");
        db.execSQL("create table " + UPDATED_USER_DETAILS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT," +
                "Surname TEXT," +  "Age INTEGER," + "Address TEXT," + "Blood_type TEXT," + "Mobile_number TEXT," + "Username TEXT)");
        db.execSQL("create table " + BOOKING_DETAILS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, City TEXT," + "Clinic TEXT," + "Doctor TEXT," + "Date_and_time TEXT," + "Booking_username TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + UPDATED_USER_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + BOOKING_DETAILS);
        onCreate(db);
    }

    public boolean insertBookingDetails(String city_name, String clinic_name, String doctor_name, String date_and_time, String username)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues bookingContentValues = new ContentValues();

        bookingContentValues.put(BOOKING_COL_2, city_name);
        bookingContentValues.put(BOOKING_COL_3, clinic_name);
        bookingContentValues.put(BOOKING_COL_4, doctor_name);
        bookingContentValues.put(BOOKING_COL_5, date_and_time);

        long result = db.update(BOOKING_DETAILS, bookingContentValues, "Booking_username = ?", new String[] {username});

        if(result == -1)
        {
            return false;
        }

        else
        {
            return true;
        }
    }

    //inserts data typed in by the user in Registration.class
    public boolean insertData(String username, String password, String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues(); //content values to store all the details typed
        ContentValues UpdatedColumnValues = new ContentValues(); //content values to store only the username as a foreign key
        ContentValues BookingContentValues = new ContentValues(); //content values to store only the username as a foreign key

        //put all the data in content values
        contentValues.put(COL_2, username);
        contentValues.put(COL_3, password);
        contentValues.put(COL_4, email);

        //put only the username in these content values
        UpdatedColumnValues.put(UPDATED_COL_8, username);
        BookingContentValues.put(BOOKING_COL_6, username);

        //long result is declared to check if it returns a -1(error)
        long result = db.insert(TABLE_NAME, null, contentValues);

        //the username is inserted as a foreign key in UPDATED_USER_DETAILS and BOOKING_DETAILS tables
        db.insert(UPDATED_USER_DETAILS, null, UpdatedColumnValues);
        db.insert(BOOKING_DETAILS, null, BookingContentValues);

        if(result == -1)
        {
            return false;
        }

        else
        {
            return true;
        }
    }

    public boolean updateData(String name, String surname, int age, String address,
                              String blood_type, String mobile_number, String username)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UPDATED_COL_2, name);
        contentValues.put(UPDATED_COL_3, surname);
        contentValues.put(UPDATED_COL_4, age);
        contentValues.put(UPDATED_COL_5, address);
        contentValues.put(UPDATED_COL_6, blood_type);
        contentValues.put(UPDATED_COL_7, mobile_number);

        long result = db.update(UPDATED_USER_DETAILS, contentValues,
                "Username = ?", new String[] {username});

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean checkUpdatedUser(String username)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + UPDATED_USER_DETAILS + " where Username = ?", new String[] { username });
        if(cursor.getCount()>0)
        {
            return false;
        }

        else
        {
            return true;
        }
    }

    public Boolean checkUsername(String username)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from Users_table where username = ?", new String[]{username});
        if(cursor.getCount()>0)
        {
            return false;
        }

        else
        {
            return true;
        }
    }

    public Boolean checkUsernameAndPassword(String username, String password)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Users_table where username = ? and password = ?",
                new String[]{username, password});
        if(cursor.getCount() > 0)
        {
            return true;
        }

        else
        {
            return false;
        }
    }


}
