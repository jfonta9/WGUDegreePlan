package com.example.joe.wgudegreeplan;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class NoteProvider extends ContentProvider {


    private static final String AUTHORITY = "com.example.joe.noteprovider";
    private static final String BASE_PATH = "wgu";
    public static final Uri NOTE_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

    // Constant to identify the requested operation
    private static final int NOTE = 1;
    private static final int NOTE_ID = 2;
    public static final String CONTENT_ITEM_TYPE = "NOTE";

    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);
    static{
        uriMatcher.addURI(AUTHORITY, BASE_PATH, NOTE);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", NOTE_ID);
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

        if (uriMatcher.match( uri ) == NOTE_ID) {
            selection = DBHelper.NOTE_ID + "=" + uri.getLastPathSegment();
        }

        return database.query(DBHelper.TABLE_NOTE, DBHelper.NOTE_ALL_COLUMNS,
                selection, null, null, null,
                DBHelper.NOTE_ID + " DESC");
    }


    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = database.insert(DBHelper.TABLE_NOTE, null,
                values);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return database.delete(DBHelper.TABLE_NOTE, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return database.update(DBHelper.TABLE_NOTE, values, selection, selectionArgs);
    }
}
