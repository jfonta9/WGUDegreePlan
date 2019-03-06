package com.example.joe.wgudegreeplan;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class AssessmentProvider extends ContentProvider {

    public static final String CONTENT_ITEM_TYPE = "ASSESSMENT";
    private static final String AUTHORITY = "com.example.joe.assessmentprovider";
    private static final String BASE_PATH = "wgu";
    public static final Uri ASSESSMENT_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

    // Constant to identify the requested operation
    private static final int ASSESSMENT = 1;
    private static final int ASSESSMENT_ID = 2;

    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);
    static{
        uriMatcher.addURI(AUTHORITY, BASE_PATH, ASSESSMENT);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", ASSESSMENT_ID);
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
        return database.query(DBHelper.TABLE_ASSESSMENT, DBHelper.ASSESSMENT_ALL_COLUMNS,
                selection, null, null, null,
                DBHelper.ASSESSMENT_ID + " DESC");
    }


    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = database.insert(DBHelper.TABLE_ASSESSMENT, null,
                values);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return database.delete(DBHelper.TABLE_ASSESSMENT, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return database.update(DBHelper.TABLE_ASSESSMENT, values, selection, selectionArgs);
    }
}
