package com.example.joe.wgudegreeplan;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CourseEditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList>{

    private static final int EDIT_COURSE_REQUEST_CODE = 1;
    private int courseId;
    private EditText editName;
    private EditText editStart;
    private EditText editEnd;
    private EditText editNotes;
    private Spinner statusSpinner;

    private String oldName;
    private String oldStart;
    private String oldEnd;
    private String courseStatus;
    private String newName;
    private String newStart;
    private String newEnd;
    private String newStatus;
    private String courseFilter;
    private ImageButton editStartButton;
    private ImageButton editEndButton;
    private DatePickerDialog.OnDateSetListener startDateSetListener;
    private DatePickerDialog.OnDateSetListener endDateSetListener;
    private String mentorFilter;
    private ArrayList<Mentor> mentorList = new ArrayList<Mentor>();
    private ArrayList<Assessment> assessmentList = new ArrayList<Assessment>();
    private ArrayList<Note> noteList = new ArrayList<Note>();
    private AssessmentAdapter assessmentAdapter;
    private String assessmentFilter;
    private String noteFilter;
    private NoteAdapter noteAdapter;
    private ScrollView scrollView;
    private Date startDate;
    private Date endDate;
    private Date todayDate;
    private MenuItem alarm;
    private int courseIdOffset = courseId + 5000;
    private int startDateClick = 0;
    private int endDateClick = 0;


    private static final int EDIT_MENTOR_REQUEST_CODE = 1;
    private static final int MENTOR_REQUEST_CODE = 2;
    private static final int NOTE_REQUEST_CODE = 4;
    private static final int ASSESSMENT_REQUEST_CODE= 3;


    private MentorAdapter mentorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_course_editor );

        editName = findViewById( R.id.courseName );
        editStart = findViewById( R.id.courseStart );
        editEnd = findViewById( R.id.courseEnd );
        statusSpinner = findViewById( R.id.statusSpinner );
        editStartButton = findViewById( R.id.editStartDateButton );
        editEndButton = findViewById( R.id.editEndButton );
        scrollView = findViewById( R.id.scrollLayout );
        scrollView.fullScroll( View.FOCUS_DOWN );
        alarm = findViewById( R.id.action_alarm );


        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra( CourseProvider.CONTENT_ITEM_TYPE );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle( "Edit Course" );

        courseFilter = DBHelper.COURSE_ID + "=" + uri.getLastPathSegment();
        Cursor cursor = getContentResolver().query( uri , DBHelper.COURSE_ALL_COLUMNS, courseFilter,
                null,null);
        cursor.moveToFirst();
        courseId = cursor.getInt( cursor.getColumnIndex( "_id" ) );
        oldName = cursor.getString( cursor.getColumnIndex( DBHelper.COURSE_NAME ) );
        oldStart = cursor.getString( cursor.getColumnIndex( DBHelper.COURSE_START ) );
        oldEnd = cursor.getString( cursor.getColumnIndex( DBHelper.COURSE_END ) );
        courseStatus = cursor.getString( cursor.getColumnIndex( DBHelper.COURSE_STATUS ) );
        editName.setText( oldName);
        editStart.setText( oldStart );
        editEnd.setText( oldEnd );
        setSpinner( courseStatus );
        showAssessmentList();
        showMentorList();
        showNoteList();

        editStartButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        CourseEditorActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        startDateSetListener, year,month,day);
                dialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
                dialog.show();
                startDateClick++;
            }
        } );

        startDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                editStart.setText(month+"/"+dayOfMonth+"/"+year);

            }
        };


        editEndButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        CourseEditorActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        endDateSetListener, year,month,day);
                dialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
                dialog.show();
                endDateClick++;
            }
        } );

        endDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                editEnd.setText(month+"/"+dayOfMonth+"/"+year);

            }
        };
    }


    private void updateStartAlarm() throws ParseException {
        getDates();
        Intent intent = new Intent(CourseEditorActivity.this, AlarmReceiver.class);
        intent.putExtra( "type", "start" );
        intent.putExtra( "name", editName.getText().toString() );
        AlarmManager alarmManager = (AlarmManager) getSystemService( Context.ALARM_SERVICE );
        Toast.makeText( this, "Start Alarm Updated!", Toast.LENGTH_SHORT ).show();
        PendingIntent updateStart = PendingIntent.getBroadcast( CourseEditorActivity.this,
                courseId,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set( AlarmManager.RTC_WAKEUP, startDate.getTime(), updateStart );
    }



    private void updateEndAlarm() throws ParseException {
        getDates();
        Intent intent = new Intent(CourseEditorActivity.this, AlarmReceiver.class);
        intent.putExtra( "type", "end" );
        intent.putExtra( "name", editName.getText().toString() );
        AlarmManager alarmManager = (AlarmManager) getSystemService( Context.ALARM_SERVICE );
        Toast.makeText( this, "End Alarm Updated!", Toast.LENGTH_SHORT ).show();
        PendingIntent updateEnd = PendingIntent.getBroadcast( CourseEditorActivity.this,
                courseIdOffset,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set( AlarmManager.RTC_WAKEUP, endDate.getTime(), updateEnd );
    }

    private boolean alarmUp(){
        Intent intent = new Intent(CourseEditorActivity.this, AlarmReceiver.class);
        boolean alarmUp = (PendingIntent.getBroadcast(CourseEditorActivity.this, courseId, intent,
                PendingIntent.FLAG_NO_CREATE) != null);
        return alarmUp;
    }


    private void deleteAlarms(){
        Intent intent = new Intent(CourseEditorActivity.this, AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService( Context.ALARM_SERVICE );
        Toast.makeText( this, "Alarms Disabled", Toast.LENGTH_SHORT ).show();
        PendingIntent cancelStart = PendingIntent.getBroadcast( CourseEditorActivity.this,
                courseId,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        cancelStart.cancel();
        alarmManager.cancel( cancelStart );

        PendingIntent cancelEnd = PendingIntent.getBroadcast( CourseEditorActivity.this,
                courseIdOffset, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        cancelEnd.cancel();
        alarmManager.cancel( cancelEnd );
    }

    private boolean checkLists(){
        if(mentorList.size() == 0){
            Toast.makeText( this, "Must add mentor!", Toast.LENGTH_SHORT ).show();
            return false;
        }
        if(assessmentList.size() == 0){
            Toast.makeText( this, "Must add assessment!", Toast.LENGTH_SHORT ).show();
            return false;
        }
        if(noteList.size() == 0){
            Toast.makeText( this, "Must add note!", Toast.LENGTH_SHORT ).show();
            return false;
        }
        else{
            return true;
        }
    }



    private void finishEdit() throws ParseException {
        newName = editName.getText().toString().trim();
        newStart = editStart.getText().toString().trim();
        newEnd = editEnd.getText().toString().trim();
        newStatus = statusSpinner.getSelectedItem().toString();

        if(validate() && checkLists()){
            getDates();
            if (endDate.before(startDate)){
                Toast.makeText( this, "Start date must be before end date!", Toast.LENGTH_SHORT ).show();
                setResult( RESULT_CANCELED );
            }
            else{
                updateCourse(newName,newStart,newEnd,newStatus);
                if(alarmUp() && !oldStart.equals(newStart)){
                    try {
                        updateStartAlarm();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }if(alarmUp() && !oldEnd.equals(newEnd)){
                    try {
                        updateEndAlarm();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    private boolean validate(){
        if(newName.length() == 0){
            Toast.makeText( this, "Enter a valid name!", Toast.LENGTH_SHORT ).show();
            setResult( RESULT_CANCELED );
            return false;
        }
        else{
            return true;
        }
    }

    private void getDates() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat( "M/dd/yyyy" );
        startDate = dateFormat.parse( editStart.getText().toString().trim() );
        endDate = dateFormat.parse( editEnd.getText().toString().trim() );
        Date date = new Date();
        String today = String.valueOf(date.getMonth()) + "/" + String.valueOf(date.getDay()) + "/"
                + String.valueOf(date.getYear());
        todayDate = dateFormat.parse(today);
    }

    private void setAlarms(){
            Intent intent = new Intent(CourseEditorActivity.this, AlarmReceiver.class);
            AlarmManager alarmManager = (AlarmManager) getSystemService( Context.ALARM_SERVICE );
            if(alarmUp()){
                deleteAlarms();
            }
            else {
                try {
                    getDates();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                intent.putExtra( "type", "start" );
                intent.putExtra( "name", editName.getText().toString() );
                PendingIntent startPi = PendingIntent.getBroadcast( CourseEditorActivity.this,
                        courseId, intent, 0 );
                alarmManager.set( AlarmManager.RTC_WAKEUP, startDate.getTime(), startPi );

                intent.putExtra( "type", "end" );
                //endIntent.putExtra( "courseName",name );
                PendingIntent endPi = PendingIntent.getBroadcast( CourseEditorActivity.this,
                        courseIdOffset, intent, 0 );

                alarmManager.set( AlarmManager.RTC_WAKEUP,  endDate.getTime(), endPi );
                Toast.makeText( this, "Alarms enabled", Toast.LENGTH_SHORT ).show();
            }
    }




    private ArrayList<Mentor> getMentors(int courseId){
        mentorFilter = DBHelper.COURSE_ID + " = " + courseId;
        Cursor cursor = getContentResolver().query(MentorProvider.MENTOR_CONTENT_URI,
                DBHelper.MENTOR_ALL_COLUMNS, mentorFilter, null, null);
        while(cursor.moveToNext()){
            int mid = cursor.getInt( cursor.getColumnIndex( DBHelper.MENTOR_ID ) );
            String first = cursor.getString( cursor.getColumnIndex( DBHelper.MENTOR_FNAME ) );
            String last =cursor.getString( cursor.getColumnIndex( DBHelper.MENTOR_LNAME ) );
            String phone = cursor.getString( cursor.getColumnIndex( DBHelper.MENTOR_PHONE ) );
            String email =cursor.getString( cursor.getColumnIndex( DBHelper.MENTOR_EMAIL) );
            Mentor mentor = new Mentor(mid,first,last,phone,email);
            mentorList.add( mentor );
        }
        return mentorList;
    }


    private void showAssessmentList(){
        ListView listview = (ListView) findViewById( R.id.assessmentListView );
        listview.setAdapter( null );
        assessmentAdapter = new AssessmentAdapter(CourseEditorActivity.this,
                R.layout.assessment_list_item,getAssessments( courseId ));
        listview.setAdapter( assessmentAdapter );
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long assessmentId =assessmentAdapter.getItemId( position );
                Intent intent = new Intent(CourseEditorActivity.this, AssessmentEditorActivity.class);
                Uri uri = Uri.parse(AssessmentProvider.ASSESSMENT_CONTENT_URI + "/" + assessmentId);
                intent.putExtra(AssessmentProvider.CONTENT_ITEM_TYPE, uri);
                startActivityForResult(intent, ASSESSMENT_REQUEST_CODE);
            }
        });
    }

    public void newMentor(View view) {
        Intent intent = new Intent( this, MentorInsertActivity.class );
        Uri uri = Uri.parse( CourseProvider.COURSE_CONTENT_URI + "/" + courseId );
        intent.putExtra( CourseProvider.CONTENT_ITEM_TYPE, uri );
        startActivityForResult( intent, MENTOR_REQUEST_CODE );
    }



    public void newNote(View view) {
        Intent intent  = new Intent(this, NoteInsertActivity.class );
        Uri uri = Uri.parse( CourseProvider.COURSE_CONTENT_URI+"/" + courseId );
        intent.putExtra( CourseProvider.CONTENT_ITEM_TYPE, uri );
        startActivityForResult( intent, NOTE_REQUEST_CODE );

    }

    public void newAssessment(View view) {
        if (assessmentList.size() >= 5) {
            Toast.makeText( this, "Max 5 assessments!", Toast.LENGTH_SHORT ).show();
        } else {
            Intent intent = new Intent( this, AssessmentInsertActivity.class );
            Uri uri = Uri.parse( CourseProvider.COURSE_CONTENT_URI + "/" + courseId );
            intent.putExtra( CourseProvider.CONTENT_ITEM_TYPE, uri );
            startActivityForResult( intent, ASSESSMENT_REQUEST_CODE );
        }
    }


    private ArrayList<Assessment> getAssessments(int courseId){
        assessmentFilter = DBHelper.COURSE_ID + " = " + courseId;
        Cursor cursor = getContentResolver().query(AssessmentProvider.ASSESSMENT_CONTENT_URI,
                DBHelper.ASSESSMENT_ALL_COLUMNS, assessmentFilter, null, null);
        while(cursor.moveToNext()){
            int aid = cursor.getInt( cursor.getColumnIndex( DBHelper.ASSESSMENT_ID ) );
            String name = cursor.getString( cursor.getColumnIndex( DBHelper.ASSESSMENT_TITLE ) );
            String type =cursor.getString( cursor.getColumnIndex( DBHelper.ASSESSMENT_TYPE ) );
            String dueDate = cursor.getString( cursor.getColumnIndex( DBHelper.ASSESSMENT_DUEDATE ) );
            Assessment a = new Assessment(aid,name,type,dueDate);
            assessmentList.add( a );
        }

        return assessmentList;
    }


    private void showNoteList(){
        ListView listview = (ListView) findViewById( R.id.notesListView );
        listview.setAdapter( null );
        noteAdapter = new NoteAdapter(CourseEditorActivity.this,
                R.layout.note_list_item, getNotes( courseId ));
        listview.setAdapter( noteAdapter );
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long noteId =noteAdapter.getItemId( position );
                Intent intent = new Intent(CourseEditorActivity.this, NoteEditorActivity.class);
                Uri uri = Uri.parse(NoteProvider.NOTE_CONTENT_URI + "/" + noteId);
                intent.putExtra(NoteProvider.CONTENT_ITEM_TYPE, uri);
                startActivityForResult(intent, NOTE_REQUEST_CODE);
            }
        });
    }

    private void showMentorList(){
        ListView listview = (ListView) findViewById( R.id.mentorListView );
        listview.setAdapter( null );
        mentorAdapter = new MentorAdapter(CourseEditorActivity.this,
                R.layout.mentor_list_item,getMentors( courseId ));
        listview.setAdapter( mentorAdapter );
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long mentorId =mentorAdapter.getItemId( position );
                Intent intent = new Intent(CourseEditorActivity.this, MentorEditorActivity.class);
                Uri uri = Uri.parse(MentorProvider.MENTOR_CONTENT_URI + "/" + mentorId);
                intent.putExtra(MentorProvider.CONTENT_ITEM_TYPE, uri);
                startActivityForResult(intent, MENTOR_REQUEST_CODE);
            }
        });
    }

    private ArrayList<Note> getNotes(int courseId){
        noteFilter = DBHelper.COURSE_ID + " = " + courseId;
        Cursor cursor = getContentResolver().query(NoteProvider.NOTE_CONTENT_URI,
                DBHelper.NOTE_ALL_COLUMNS, noteFilter, null, null);
        while(cursor.moveToNext()){
            int nid = cursor.getInt( cursor.getColumnIndex( DBHelper.NOTE_ID ) );
            String noteContent = cursor.getString( cursor.getColumnIndex( DBHelper.NOTE_CONTENT ) );
            Note n = new Note(nid,noteContent);
            noteList.add( n );
        }
        return noteList;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.menu_course, menu );
        return true;
    }

    private void setSpinner(String courseStatus){
        for(int i = 0; i < statusSpinner.getAdapter().getCount(); i++){
            if(statusSpinner.getAdapter().getItem( i ).toString().contains( courseStatus )){
                statusSpinner.setSelection( i );
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch(item.getItemId()){
            case R.id.action_delete:
                deleteCourse();
                break;
            case android.R.id.home:
                try {
                    finishEdit();
                } catch (ParseException e) {
                }
                break;
            case R.id.action_alarm:
                setAlarms();
                break;
        }
        return true;
    }

    private void deleteCourse() {
        courseFilter = DBHelper.COURSE_ID + " = " + courseId;
        getContentResolver().delete( CourseProvider.COURSE_CONTENT_URI, courseFilter,null );
        deleteAlarms();
        Toast.makeText( this, "Course Deleted", Toast.LENGTH_SHORT ).show();
        setResult( RESULT_OK );
        finish();
    }

    private boolean checkDate(){
        if(editStartButton.equals( "Start" ) || editEndButton.equals( "End" )){
            Toast.makeText( this, "Enter valid start/end dates!", Toast.LENGTH_SHORT ).show();
            return false;
        }
        else{
            return true;
        }
    }

    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void restartLoader(){
        getLoaderManager().restartLoader( 0,null,this );
    }
    

    private void updateCourse(String name, String start, String end,String status) {
        ContentValues values = new ContentValues(  );
        values.put( DBHelper.COURSE_NAME, name );
        values.put( DBHelper.COURSE_START, start);
        values.put( DBHelper.COURSE_END, end );
        values.put( DBHelper.COURSE_STATUS, status);

        getContentResolver().update( CourseProvider.COURSE_CONTENT_URI, values, courseFilter,
                null);
        Toast.makeText( this, "Course Updated", Toast.LENGTH_SHORT ).show();
        setResult( RESULT_OK );
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //restartLoader();
        if(requestCode == EDIT_MENTOR_REQUEST_CODE || requestCode == MENTOR_REQUEST_CODE){
            mentorList.clear();
            showMentorList();
        } if(requestCode == ASSESSMENT_REQUEST_CODE){
            assessmentList.clear();
            showAssessmentList();
        } if(requestCode == NOTE_REQUEST_CODE){
            noteList.clear();
            showNoteList();
        }
    }


    @Override
    public Loader<ArrayList> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList> loader, ArrayList data) {
    }

    @Override
    public void onLoaderReset(Loader<ArrayList> loader) {
    }
}
