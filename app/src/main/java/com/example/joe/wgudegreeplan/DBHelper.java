package com.example.joe.wgudegreeplan;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    //information of database
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "wgu.db";

    // Term table variables
    public static final String TABLE_TERM = "Term";
    public static final String TERM_ID = "term_ID";
    public static final String TERM_NAME = "term_name";
    public static final String TERM_START = "term_start";
    public static final String TERM_END = "term_end";

    // Database creation sql statement
    private static final String TABLE_CREATE_TERM = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TERM + "( " + TERM_ID
            + " integer primary key autoincrement, " + TERM_NAME
            + " TEXT not null, " + TERM_START + " TEXT not null, "
            + TERM_END + " TEXT not null);";
    public static final String[] TERM_ALL_COLUMNS =
            {TERM_ID + " as _id", TERM_NAME,TERM_START,TERM_END};

    // Course table variables
    public static final String TABLE_COURSE = "Course";
    public static final String COURSE_ID = "course_ID";
    public static final String COURSE_NAME = "course_name";
    public static final String COURSE_STATUS = "course_status";
    public static final String COURSE_START = "course_start";
    public static final String COURSE_END = "course_end";
    // Database creation sql statement


    private static final String TABLE_CREATE_COURSE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_COURSE + "( " + COURSE_ID
            + " integer primary key autoincrement, " + COURSE_NAME
            + " TEXT not null, " + COURSE_STATUS + " TEXT not null, "
            + COURSE_START + " TEXT not null, " + COURSE_END + " TEXT not null, "  + TERM_ID +
            " integer not null, " + "foreign key (" +TERM_ID+") references `"+TABLE_TERM+"`);";
    public static final String[] COURSE_ALL_COLUMNS =
            {COURSE_ID + " as _id", COURSE_NAME,COURSE_STATUS,COURSE_START,COURSE_END,TERM_ID};


    // Assessment table variables
    public static final String TABLE_ASSESSMENT = "Assessment";
    public static final String ASSESSMENT_ID = "assessment_ID";
    public static final String ASSESSMENT_TYPE = "assessment_type";
    public static final String ASSESSMENT_TITLE = "assessment_title";
    public static final String ASSESSMENT_DUEDATE = "assessment_duedate";
    // Database creation sql statement
    private static final String TABLE_CREATE_ASSESSMENT = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ASSESSMENT + "( " + ASSESSMENT_ID
            + " integer primary key autoincrement, " + ASSESSMENT_TITLE
            + " TEXT not null, " + ASSESSMENT_TYPE + " TEXT not null, "
            + ASSESSMENT_DUEDATE + " TEXT not null," + COURSE_ID + " TEXT not null, " +
            "foreign key (" +COURSE_ID+") references `"+TABLE_COURSE+"` ON DELETE CASCADE);";
    public static final String[] ASSESSMENT_ALL_COLUMNS =
            {ASSESSMENT_ID, ASSESSMENT_TITLE,ASSESSMENT_TYPE,ASSESSMENT_DUEDATE, COURSE_ID};


    // Mentor table variables
    public static final String TABLE_MENTOR = "Mentor";
    public static final String MENTOR_ID = "mentor_ID";
    public static final String MENTOR_FNAME = "mentor_fname";
    public static final String MENTOR_LNAME = "mentor_lname";
    public static final String MENTOR_PHONE = "mentor_phone";
    public static final String MENTOR_EMAIL = "mentor_email";
    // Database creation sql statement
    private static final String TABLE_CREATE_MENTOR = "CREATE TABLE IF NOT EXISTS "
            + TABLE_MENTOR + "( " + MENTOR_ID
            + " integer primary key autoincrement, " + MENTOR_FNAME
            + " TEXT not null, " + MENTOR_LNAME + " TEXT not null, "
            + MENTOR_PHONE + " TEXT not null, " + MENTOR_EMAIL + " TEXT not null, " +
            COURSE_ID + " TEXT not null, foreign key (" +COURSE_ID+") " +
            "references `"+TABLE_COURSE+"` ON DELETE CASCADE);";
    public static final String[] MENTOR_ALL_COLUMNS =
            {MENTOR_ID, MENTOR_FNAME,MENTOR_LNAME,MENTOR_PHONE,MENTOR_EMAIL,COURSE_ID};




    // Notes table variables
    public static final String TABLE_NOTE = "Note";
    public static final String NOTE_ID = "note_ID";
    public static final String NOTE_CONTENT = "note_content";

    // Database creation sql statement
    private static final String TABLE_CREATE_NOTE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NOTE + "( " + NOTE_ID + " integer primary key autoincrement, " + NOTE_CONTENT
            + " TEXT not null, "+ COURSE_ID + " TEXT not null, foreign key (" +COURSE_ID+") " +
            "references `"+TABLE_COURSE+"` ON DELETE CASCADE);";
    public static final String[] NOTE_ALL_COLUMNS = {NOTE_ID, NOTE_CONTENT,COURSE_ID};




    public void createTables(){
        this.getWritableDatabase().execSQL( TABLE_CREATE_TERM );
        this.getWritableDatabase().execSQL( TABLE_CREATE_COURSE );
        this.getWritableDatabase().execSQL( TABLE_CREATE_ASSESSMENT );
        this.getWritableDatabase().execSQL( TABLE_CREATE_MENTOR );
        this.getWritableDatabase().execSQL( TABLE_CREATE_NOTE );
    }


    //initialize the database
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onConfigure(SQLiteDatabase db){
        db.setForeignKeyConstraintsEnabled( true );
    }

}

