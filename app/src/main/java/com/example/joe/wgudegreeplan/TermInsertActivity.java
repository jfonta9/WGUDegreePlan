package com.example.joe.wgudegreeplan;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TermInsertActivity  extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    //private String action;
    private EditText insertName;
    private EditText insertStart;
    private EditText insertEnd;
    private String termFilter;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private static final String TAG = "TermInsertActivity";
    private ImageButton editStartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_term_insert );
        insertName = findViewById( R.id.insertName );
        insertStart = findViewById( R.id.insertStart );
        insertEnd = findViewById( R.id.insertEnd );
        editStartButton = findViewById( R.id.editStartDateButton );
        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra( TermProvider.CONTENT_ITEM_TYPE );
        setTitle( "Add Term" );

        editStartButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        TermInsertActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener, year,month,day);
                dialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
                dialog.show();
            }
        } );

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                insertStart.setText(month+"/"+dayOfMonth+"/"+year);
                Calendar c = Calendar.getInstance();
                c.set( year,month-1,dayOfMonth );
                c.add( Calendar.MONTH,6 );
                c.add( Calendar.DAY_OF_MONTH, -1 );
                SimpleDateFormat format = new SimpleDateFormat("M/dd/yyyy");
                String strDate = format.format(c.getTime());

                insertEnd.setText(strDate);
            }
        };
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
        String name = insertName.getText().toString().trim();
        String start = insertStart.getText().toString().trim();
        String end = insertEnd.getText().toString().trim();
        if(name.equals("Name")){
            setResult(RESULT_CANCELED);
            Toast.makeText(this, "Enter valid Term name!", Toast.LENGTH_SHORT).show();
        }
        else if(name.length() == 0){
            setResult(RESULT_CANCELED);
            Toast.makeText(this, "Enter valid Term name!", Toast.LENGTH_SHORT).show();
        }
        else if(start.equals("Start")){
            setResult((RESULT_CANCELED));
            Toast.makeText(this, "Enter Term start date!", Toast.LENGTH_SHORT).show();
        }
        else{
            insertTerm(name,start,end);
            finish();
        }
    }


    private void insertTerm(String termName, String termStart, String termEnd) {
        ContentValues values = new ContentValues(  );
        values.put( DBHelper.TERM_NAME, termName );
        values.put( DBHelper.TERM_START, termStart);
        values.put( DBHelper.TERM_END, termEnd );
        getContentResolver().insert( TermProvider.TERM_CONTENT_URI, values);
        setResult( RESULT_OK );
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }
}

