package com.example.joe.wgudegreeplan;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TermEditorActivity extends AppCompatActivity {

    private static final int EDIT_COURSE_REQUEST_CODE = 1;
    private String termId;
    private EditText editName;
    private EditText editStart;
    private EditText editEnd;
    private String termFilter;
    private String oldName;
    private String oldStart;
    private String oldEnd;
    private ImageButton editStartDateButton;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_term_editor );

        editName = findViewById( R.id.courseName );
        editStart = findViewById( R.id.courseStart );
        editEnd = findViewById( R.id.editEnd );
        editStartDateButton = findViewById( R.id.editStartDateButton );

        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra( TermProvider.CONTENT_ITEM_TYPE );

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle( "Edit Term" );

            termFilter = DBHelper.TERM_ID + "=" + uri.getLastPathSegment();
            Cursor cursor = getContentResolver().query( uri , DBHelper.TERM_ALL_COLUMNS, termFilter,
                    null,null);
            cursor.moveToFirst();
            termId = cursor.getString( cursor.getColumnIndex( "_id" ) );
            oldName = cursor.getString( cursor.getColumnIndex( DBHelper.TERM_NAME ) );
            oldStart = cursor.getString( cursor.getColumnIndex( DBHelper.TERM_START ) );
            oldEnd = cursor.getString( cursor.getColumnIndex( DBHelper.TERM_END ) );
            editName.setText( oldName);
            editStart.setText( oldStart );
            editEnd.setText( oldEnd );


        editStartDateButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        TermEditorActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener, year,month,day);
                dialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
                dialog.show();
            }
        } );

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                editStart.setText(month+"/"+dayOfMonth+"/"+year);
                Calendar c = Calendar.getInstance();
                c.set( year,month-1,dayOfMonth );
                c.add( Calendar.MONTH,6 );
                c.add( Calendar.DAY_OF_MONTH, -1 );
                SimpleDateFormat format = new SimpleDateFormat("M/dd/yyyy");
                String strDate = format.format(c.getTime());

                editEnd.setText(strDate);
            }
        };


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch(item.getItemId()){
            case android.R.id.home:
                finishEdit();
                return true;
        }
        return super.onOptionsItemSelected( item );
    }


    private void finishEdit(){
        String newName = editName.getText().toString().trim();
        String newStart = editStart.getText().toString().trim();
        String newEnd = editEnd.getText().toString().trim();
        if(newName.length() == 0){
            setResult(RESULT_CANCELED);
            Toast.makeText(this, "Enter valid Term name!", Toast.LENGTH_SHORT).show();
        }else{
            updateTerm(newName,newStart,newEnd);
            setResult( RESULT_OK );
            finish();
        }
    }


    private void updateTerm(String termName, String termStart, String termEnd) {
        ContentValues values = new ContentValues(  );
        values.put( DBHelper.TERM_NAME, termName );
        values.put( DBHelper.TERM_START, termStart);
        values.put( DBHelper.TERM_END, termEnd );
        getContentResolver().update( TermProvider.TERM_CONTENT_URI, values, termFilter,
                null);
        Toast.makeText( this, "Term Updated", Toast.LENGTH_SHORT ).show();
        setResult( RESULT_OK );
    }
}
