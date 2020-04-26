package com.example.dit.hua.android.a2ndProject.contentproviderfor2ndproject;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class DBHelper2nProject extends SQLiteOpenHelper implements BaseColumns, Serializable {


    //FIELDS OF THE DB_TABLE
    //public static final String _ID = "id";  id id autoincrement
    public static String UNIX_TIMESTAMP = "_UNIX_TIMESTAMP";
    public static String LATITUDE = "_LATITUDE";
    public static String LONGITUDE  ="_LONGTITUDE";

    public static final String DB_NAME = "MY_2ND_DATABASE";
    public static final String DB_TABLE = "MY_2ND_DATABASE";
    public static final int DB_VERSION =1;

    public DBHelper2nProject(@Nullable Context context) {
        super(context,  DB_NAME, null, DB_VERSION);
    }
    //context	     Context: to use for locating paths to the the database This value may be null.
    //name	         String: of the database file, or null for an in-memory database This value may be null.
    //factory	     SQLiteDatabase.CursorFactory: to use for creating cursor objects, or null for the default This value may be null.
    //version	     int: number of the database (starting at 1); if the database is older, onUpgrade(SQLiteDatabase, int, int) will be used to upgrade the database; if the database is newer, onDowngrade(SQLiteDatabase, int, int) will be used to downgrade the database


    @Override
    public void onCreate(SQLiteDatabase db) {
        create_database_table(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        //delete_table(db);
    }

    public void delete_table(SQLiteDatabase db){ //delete table
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        onCreate(db);
    }

    private void create_database_table(SQLiteDatabase db) {        //create database table
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + DB_TABLE + " (" +
                    BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DBHelper2nProject.UNIX_TIMESTAMP + " TEXT, " +
                    DBHelper2nProject.LATITUDE + " TEXT, " +
                    DBHelper2nProject.LONGITUDE + " TEXT " +
                    ");"
            );
        } catch (Exception e) {
            Log.e(TAG, "DB HELPER CLASS : TABLE NOT CREATED  ", e);
            e.getStackTrace();
        }
    }
    public long createEntry(DataContract2ndProject contactsContract,  SQLiteDatabase ourDatabase){  //insert the data that the user gave into the database table
        ContentValues contentValues = new ContentValues();
        long number = 0;
        //ID is autoincrement
        try {
            contentValues.put(DBHelper2nProject.UNIX_TIMESTAMP, contactsContract.getUnix_timestamp());
            contentValues.put(DBHelper2nProject.LATITUDE, contactsContract.getLat());
            contentValues.put(DBHelper2nProject.LONGITUDE, contactsContract.getLon());
        }catch(Exception e ){
            Log.e(TAG, "DBHELPER:ERROR-> CREATE ENTRY  ", e);
        }
        //in every column i want to add a new row
        //i have created an object type of DataContract, which contains unix timestamp, latitude, longitude  , so I need getters to "take" fname, lname and age from that object and put them into database each in the column that belongs
        try{
            number =ourDatabase.insert(DB_TABLE, null , contentValues);
        }catch (Exception e){
            Log.e(TAG, "DBHELPER:ERROR -> INSERT IN CREATE ENTRY ", e);
        }finally {
            Log.i(TAG, "DBHELPER: INSERT IN CREATE ENTRY : DONE ");
        }

        try{
            ourDatabase.close();                                                                    //close connection with the database in order to prevent memory leaking
        }catch(Exception e){
            Log.e(TAG, "DB HELPER CLASS: database connection did not close", e);
            e.getStackTrace();
        }
        return number ;
    }



}
