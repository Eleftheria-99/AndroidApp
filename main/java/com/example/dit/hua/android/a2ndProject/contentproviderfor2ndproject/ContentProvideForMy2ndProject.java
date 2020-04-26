package com.example.dit.hua.android.a2ndProject.contentproviderfor2ndproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

public class ContentProvideForMy2ndProject extends ContentProvider {


    private UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);   //+initialization here
    private static String AUTHORITY = "com.example.dit.hua.android.a2ndProject.contentproviderfor2ndproject"; //declare package here

    @Override
    public boolean onCreate() {
        matcher.addURI(AUTHORITY, "contacts", 1);//insert
        matcher.addURI(AUTHORITY, "retrieve_1st_entry", 2);  //returns 1st entry from db
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        DBHelper2nProject helper = new DBHelper2nProject(getContext());
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = null;
        switch(matcher.match(uri)) {

            case 1:
                String[] projection1 = {   // Define a projection that specifies which columns from the database you will actually use after this query.
                        BaseColumns._ID,
                        DBHelper2nProject.UNIX_TIMESTAMP,
                        DBHelper2nProject.LATITUDE,
                        DBHelper2nProject.LONGITUDE
                };


                //select from tablename order by column asc limit 1
                String string_order_by = "order by" + BaseColumns._ID +"asc limit 1 ";
                String selection1 = BaseColumns._ID + " = ?";
                String id = "1";
                String[] selectionArgs1 = { id };
                cursor = db.query(
                        DBHelper2nProject.DB_NAME,                                                               // The table to query
                        projection1,                                                                     // The array of columns to return (pass null to get all)
                        selection1,                                                                      // The columns for the WHERE clause
                        selectionArgs1,
                        null,                                                                  // don't group the rows
                        null,                                                                   // don't filter by row groups
                       null          //string_order_by
                );
                break;
            default:
        }
        return cursor;        //null
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        DBHelper2nProject helper = new DBHelper2nProject(getContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        long number = 0;
        number = db.insert(DBHelper2nProject.DB_TABLE, null, values);
        return  Uri.parse(uri.toString()+"/"+number);

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
