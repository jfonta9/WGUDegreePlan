package com.example.joe.wgudegreeplan;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NoteInsertActivity extends AppCompatActivity {

    //private String action;
    private EditText noteContent;
    private String notes;
    private String courseFilter;
    private static final String TAG = "NoteInsertActivity";
    private int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_note_insert );
        noteContent = findViewById( R.id.noteContent );
        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra( CourseProvider.CONTENT_ITEM_TYPE );
        courseId = Integer.parseInt( uri.getLastPathSegment() );
        setTitle( "Add Note" );
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch(item.getItemId()){
            case android.R.id.home:
                finishInsert();
        }
        return true;
    }


    private void finishInsert(){
        notes = noteContent.getText().toString().trim();
        if(notes.length() == 0){
            setResult(RESULT_CANCELED);
        }else{
            insertNote(notes);
            Toast.makeText( this, notes, Toast.LENGTH_SHORT ).show();
            finish();
        }
    }


    private void insertNote(String notes) {
        ContentValues values = new ContentValues(  );
        values.put( DBHelper.NOTE_CONTENT, notes );
        values.put( DBHelper.COURSE_ID, courseId );
        getContentResolver().insert( NoteProvider.NOTE_CONTENT_URI, values);
        setResult( RESULT_OK );
    }

}

