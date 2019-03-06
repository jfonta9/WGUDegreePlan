package com.example.joe.wgudegreeplan;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class AssessmentInsertActivity extends AppCompatActivity {


    //private String action;
    private EditText insertName;
    private EditText insertDueDate;
    private Spinner typeSpinner;
    private ImageButton dueDateButton;
    private DatePickerDialog.OnDateSetListener dueDateListener;


    private int courseId;
    private String assessmentName;
    private String assessmentDueDate;
    private String assessmentType;

    private Uri uri;
    private String courseFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_assessment_insert );

        insertName = findViewById( R.id.assessmentName );
        insertDueDate = findViewById( R.id.assessmentDueDate );
        typeSpinner = findViewById( R.id.typeSpinner );
        dueDateButton = findViewById( R.id.editDueDateButton );

        Intent intent = getIntent();
        uri = intent.getParcelableExtra( CourseProvider.CONTENT_ITEM_TYPE );
        courseId = Integer.parseInt( uri.getLastPathSegment() );
        setTitle( R.string.new_assessment );

        dueDateButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        AssessmentInsertActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dueDateListener, year,month,day);
                dialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
                dialog.show();
            }
        } );

        dueDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                insertDueDate.setText(month+"/"+dayOfMonth+"/"+year);
            }
        };
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch(item.getItemId()){
            case android.R.id.home:
                finishInsert();
                break;
        }
        return true;
    }



    private void finishInsert(){
        assessmentName = insertName.getText().toString().trim();
        assessmentType = typeSpinner.getSelectedItem().toString().trim();
        assessmentDueDate = insertDueDate.getText().toString().trim();

        if(validate()){
            insertAssessment(assessmentName, assessmentType, assessmentDueDate, courseId);
            finish();
        }
    }


    private boolean validate(){
        if(assessmentName.length() == 0){
            setResult(RESULT_CANCELED);
            Toast.makeText(this, "Enter valid name!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(assessmentName.equals("Name")){
            setResult(RESULT_CANCELED);
            Toast.makeText(this, "Enter valid name!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(assessmentDueDate.equals("Due Date")){
            setResult(RESULT_CANCELED);
            Toast.makeText(this, "Enter valid due date!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(assessmentType.equals("Type")){
            setResult(RESULT_CANCELED);
            Toast.makeText(this, "Pick a valid type!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void insertAssessment(String name, String type, String dueDate, int courseId){
        ContentValues values = new ContentValues();
        values.put( DBHelper.ASSESSMENT_TITLE, name );
        values.put( DBHelper.ASSESSMENT_TYPE, type );
        values.put( DBHelper.ASSESSMENT_DUEDATE, dueDate );
        values.put( DBHelper.COURSE_ID, courseId );
        getContentResolver().insert( AssessmentProvider.ASSESSMENT_CONTENT_URI, values );
        setResult( RESULT_OK );
    }

}
