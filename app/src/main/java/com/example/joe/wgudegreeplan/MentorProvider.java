package com.example.joe.wgudegreeplan;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MentorProvider extends ContentProvider {

    private static final String AUTHORITY = "com.example.joe.mentorprovider";
    private static final String BASE_PATH = "wgu";
    public static final Uri MENTOR_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

    // Constant to identify the requested operation
    private static final int MENTOR = 1;
    private static final int MENTOR_ID = 2;
    public static final String CONTENT_ITEM_TYPE = "MENTOR";

    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);
    static{
        uriMatcher.addURI(AUTHORITY, BASE_PATH, MENTOR);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", MENTOR_ID);
    }

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        DBHelper myHelper;

        myHelper = new DBHelper(getContext());
        database = myHelper.getWritableDatabase();

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (uriMatcher.match( uri ) == MENTOR_ID) {
            selection = DBHelper.MENTOR_ID + "=" + uri.getLastPathSegment();
        }

        return database.query(DBHelper.TABLE_MENTOR, DBHelper.MENTOR_ALL_COLUMNS,
                selection, null, null, null,
                DBHelper.MENTOR_ID + " DESC");
    }


    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Override
    public Uri insert( Uri uri, ContentValues values) {
        long id = database.insert(DBHelper.TABLE_MENTOR, null,
                values);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri,  String selection,  String[] selectionArgs) {
        return database.delete(DBHelper.TABLE_MENTOR, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values,  String selection,  String[] selectionArgs) {
        return database.update(DBHelper.TABLE_MENTOR, values, selection, selectionArgs);
    }
}
