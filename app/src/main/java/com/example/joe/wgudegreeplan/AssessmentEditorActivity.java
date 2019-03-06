package com.example.joe.wgudegreeplan;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AssessmentEditorActivity extends AppCompatActivity {

    private static final int ASSESSMENT_REQUEST_CODE = 1;
    private int assessmentId;
    private EditText editName;
    private Spinner editType;
    private EditText editDueDate;
    private ImageButton editDueDateButton;
    private String assessmentFilter;
    private String oldName;
    private String oldType;
    private String oldDueDate;
    private String newName;
    private String newType;
    private String newDueDate;
    private Date dueDate;
    private DatePickerDialog.OnDateSetListener startDateSetListener;
    private int dueDateClick = 0;
    private Date todayDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_assessment_editor );

        editName = findViewById( R.id.assessmentName );
        editType = findViewById( R.id.typeSpinner );
        editDueDate = findViewById( R.id.assessmentDueDate );
        editDueDateButton = findViewById( R.id.editDueDateButton );

        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra( AssessmentProvider.CONTENT_ITEM_TYPE );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle( "Edit Assessment" );
        assessmentId = Integer.parseInt( uri.getLastPathSegment() );
        assessmentFilter = DBHelper.ASSESSMENT_ID + "=" + assessmentId;
        Cursor cursor = getContentResolver().query( uri , DBHelper.ASSESSMENT_ALL_COLUMNS, assessmentFilter,
                null,null);
        cursor.moveToFirst();
        oldName = cursor.getString( cursor.getColumnIndex( DBHelper.ASSESSMENT_TITLE ) );
        oldType = cursor.getString( cursor.getColumnIndex( DBHelper.ASSESSMENT_TYPE ) );
        oldDueDate = cursor.getString( cursor.getColumnIndex( DBHelper.ASSESSMENT_DUEDATE ) );
        editName.setText( oldName);
        setSpinner( oldType );
        editDueDate.setText( oldDueDate );

        editDueDateButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        AssessmentEditorActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        startDateSetListener, year,month,day);
                dialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
                dialog.show();
            }
        } );

        startDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                editDueDate.setText(month+"/"+dayOfMonth+"/"+year);
            }
        };

    }


    private void updateAlarm() throws ParseException{
        getDates();
        Intent intent = new Intent(AssessmentEditorActivity.this, AlarmReceiver.class);
        intent.putExtra( "startOrEnd", "start" );
        intent.putExtra( "name", editName.getText().toString() );
        AlarmManager alarmManager = (AlarmManager) getSystemService( Context.ALARM_SERVICE );
        Toast.makeText( this, "Alarm Updated!", Toast.LENGTH_SHORT ).show();
        PendingIntent updateStart = PendingIntent.getBroadcast( AssessmentEditorActivity.this,
                assessmentId,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set( AlarmManager.RTC_WAKEUP, dueDate.getTime(), updateStart );
    }

    private boolean alarmUp(){
        Intent intent = new Intent(AssessmentEditorActivity.this, AlarmReceiver.class);
        boolean alarmUp = (PendingIntent.getBroadcast(AssessmentEditorActivity.this, assessmentId, intent,
                PendingIntent.FLAG_NO_CREATE) != null);
        return alarmUp;
    }

    private void deleteAlarms(){
        Intent intent = new Intent(AssessmentEditorActivity.this, AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService( Context.ALARM_SERVICE );
        Toast.makeText( this, "Alarm Disabled", Toast.LENGTH_SHORT ).show();
        PendingIntent cancel = PendingIntent.getBroadcast( AssessmentEditorActivity.this,
                assessmentId,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        cancel.cancel();
        alarmManager.cancel( cancel );

    }

    private void setAlarm() throws ParseException {
        Intent intent = new Intent(AssessmentEditorActivity.this, AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService( Context.ALARM_SERVICE );
        if(alarmUp()){
            deleteAlarms();
        }
        else {
            getDates();


            intent.putExtra( "type", "assessment" );
            intent.putExtra( "name", editName.getText().toString() );
            PendingIntent startPi = PendingIntent.getBroadcast( AssessmentEditorActivity.this,
                    assessmentId, intent, 0 );
            alarmManager.set( AlarmManager.RTC_WAKEUP, dueDate.getTime(), startPi );

            Toast.makeText( this, "Alarm Enabled!", Toast.LENGTH_SHORT ).show();
        }
    }


    private void setSpinner(String assessmentType){
        for(int i = 0; i < editType.getAdapter().getCount(); i++){
            if(editType.getAdapter().getItem( i ).toString().contains( assessmentType )){
                editType.setSelection( i );
            }
        }
    }

    private void getDates() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat( "M/dd/yyyy" );
        dueDate = dateFormat.parse( editDueDate.getText().toString().trim() );
        Date date = new Date();
        String today = String.valueOf(date.getMonth()) + "/" + String.valueOf(date.getDay()) + "/"
                + String.valueOf(date.getYear());
        todayDate = dateFormat.parse(today);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch(item.getItemId()){
            case R.id.action_delete:
                deleteAssessment();
                break;
            case android.R.id.home:
                finishEdit();
                break;
            case R.id.action_alarm:
                try {
                    setAlarm();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
        }
        return true;
    }



    private void deleteAssessment() {
        assessmentFilter = DBHelper.ASSESSMENT_ID + " = " + assessmentId;
        getContentResolver().delete( AssessmentProvider.ASSESSMENT_CONTENT_URI, assessmentFilter,null );
        Toast.makeText( this, "Assessment Deleted", Toast.LENGTH_SHORT ).show();
        setResult( RESULT_OK );
        finish();
    }


    private void finishEdit(){
        newName = editName.getText().toString().trim();
        newType = editType.getSelectedItem().toString().trim();
        newDueDate = editDueDate.getText().toString().trim();
        if(validate()) {
            updateAssessment(newName,newType,newDueDate);
            setResult( RESULT_OK );
            if(alarmUp() && !oldDueDate.equals(newDueDate)){
                try {
                    updateAlarm();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            finish();
        }
    }


    private boolean validate(){
        if(newName.length() == 0){
            setResult(RESULT_CANCELED);
            Toast.makeText(this, "Enter valid name!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(newName.equals("Name")){
            setResult(RESULT_CANCELED);
            Toast.makeText(this, "Enter valid name!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(newDueDate.equals("Due Date")){
            setResult(RESULT_CANCELED);
            Toast.makeText(this, "Enter valid due date!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(newType.equals("Type")){
            setResult(RESULT_CANCELED);
            Toast.makeText(this, "Pick a valid type!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void updateAssessment(String name, String type, String dueDate) {
        ContentValues values = new ContentValues(  );
        values.put( DBHelper.ASSESSMENT_TITLE, name );
        values.put( DBHelper.ASSESSMENT_TYPE, type);
        values.put( DBHelper.ASSESSMENT_DUEDATE, dueDate );
        getContentResolver().update( AssessmentProvider.ASSESSMENT_CONTENT_URI, values,
                assessmentFilter, null);
        Toast.makeText( this, "Assessment Updated", Toast.LENGTH_SHORT ).show();
        setResult( RESULT_OK );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_assessment, menu );
        return true;
    }
}
