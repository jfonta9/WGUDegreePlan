package com.example.joe.wgudegreeplan;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.LoaderManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CourseInsertActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList>{


    private EditText courseName ;
    private EditText courseStart ;
    private EditText courseEnd ;
    private EditText courseNotes;
    private Spinner statusSpinner;
    private String action;
    private int courseId;
    private int termId;
    private String name;
    private String start;
    private String end;
    private String status;
    private String notes;
    private String courseFilter;
    private Long statusId;
    private Uri courseUri;
    private boolean addCourse;
    private String termFilter;
    private Uri termUri;
    private Course course;
    private DatePickerDialog.OnDateSetListener startDateSetListener;
    private DatePickerDialog.OnDateSetListener endDateSetListener;
    private ImageButton editStartButton;
    private ImageButton editEndButton;
    private MentorAdapter mentorAdapter;
    private Date startDate;
    private Date endDate;
    private Date todayDate;


    private String mentorFilter;
    private ArrayList<Mentor> mentorList = new ArrayList<Mentor>();
    private ArrayList<Assessment> assessmentList = new ArrayList<Assessment>();
    private ArrayList<Note> noteList = new ArrayList<Note>();
    private AssessmentAdapter assessmentAdapter;
    private NoteAdapter noteAdapter;
    private String noteFilter;
    private String assessmentFilter;
    private int count = 0;
    private static final int EDIT_MENTOR_REQUEST_CODE = 1;
    private static final int MENTOR_REQUEST_CODE = 2;
    private static final int NOTE_REQUEST_CODE = 4;
    private static final int ASSESSMENT_REQUEST_CODE= 3;
    static ArrayList<PendingIntent> intentArrayList = new ArrayList<PendingIntent>(  );




    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_course_insert );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );

        courseName = findViewById( R.id.courseName );
        courseStart = findViewById( R.id.courseStart );
        courseEnd = findViewById( R.id.courseEnd );
        statusSpinner = findViewById( R.id.statusSpinner );
        editStartButton = findViewById( R.id.editStartDateButton );
        editEndButton = findViewById( R.id.editEndButton );

        Intent intent = getIntent();
        Uri termUri = intent.getParcelableExtra(TermProvider.CONTENT_ITEM_TYPE);
        termId = Integer.valueOf(termUri.getLastPathSegment());
        setTitle( "Add Course" );


        editStartButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        CourseInsertActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        startDateSetListener, year,month,day);
                dialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
                dialog.show();
            }
        } );

        startDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                courseStart.setText( month+"/"+dayOfMonth+"/"+year);
            }
        };


        editEndButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        CourseInsertActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        endDateSetListener, year,month,day);
                dialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
                dialog.show();
            }
        } );

        endDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                courseEnd.setText(month+"/"+dayOfMonth+"/"+year);

            }
        };
    }


    @Override
    public void onBackPressed() {
        courseFilter = DBHelper.COURSE_ID + " = " + courseId;
        getContentResolver().delete( CourseProvider.COURSE_CONTENT_URI, courseFilter,null );
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch(item.getItemId()) {
            case android.R.id.home:
                try {
                    finishInsert();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
        }
        return true;
    }


    private void finishInsert() throws ParseException {

        name = courseName.getText().toString().trim();
        start = courseStart.getText().toString().trim();
        end = courseEnd.getText().toString().trim();
        status = statusSpinner.getSelectedItem().toString().trim();

        if(validate() && checkLists()){
            getDates();
            if (endDate.before(startDate)){
                Toast.makeText( this, "Start date must be before end date!", Toast.LENGTH_SHORT ).show();
                setResult( RESULT_CANCELED );
            }
            else{
                updateCourse(name, start, end, status, termId);
                setResult(RESULT_OK);
                finish();
            }
        }
    }


    private void getDates() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat( "M/dd/yyyy" );
        startDate = dateFormat.parse( start );
        endDate = dateFormat.parse( end );
        Date date = new Date();
        String today = String.valueOf(date.getMonth()) + "/" + String.valueOf(date.getDay()) + "/"
                + String.valueOf(date.getYear());
        todayDate = dateFormat.parse(today);
    }


    private boolean validate(){
        if(name.equals("Name")){
            Toast.makeText( this, "Enter a valid name!", Toast.LENGTH_SHORT ).show();
            setResult( RESULT_CANCELED );
            return false;
        }
        if(name.length() == 0){
            Toast.makeText( this, "Enter a valid name!", Toast.LENGTH_SHORT ).show();
            setResult( RESULT_CANCELED );
            return false;
        }
        if(start.equals("Start")){
            Toast.makeText( this, "Select a valid start date!", Toast.LENGTH_SHORT ).show();
            setResult( RESULT_CANCELED );
            return false;
        }
        if(end.equals("End")){
            Toast.makeText( this, "Select a valid end date!", Toast.LENGTH_SHORT ).show();
            setResult( RESULT_CANCELED );
            return false;
        }
        else{
            return true;
        }
    }


    private void insertCourse(String name, String start, String end, String status, int termId) {
        ContentValues values = new ContentValues();
        values.put( DBHelper.COURSE_NAME, name );
        values.put( DBHelper.COURSE_START, start );
        values.put( DBHelper.COURSE_END, end );
        values.put( DBHelper.COURSE_STATUS, status );
        values.put(DBHelper.TERM_ID, termId);
        getContentResolver().insert( CourseProvider.COURSE_CONTENT_URI, values );
        setResult( RESULT_OK );
        Toast.makeText( this, "works", Toast.LENGTH_SHORT ).show();
    }


    private void updateCourse(String name, String start, String end, String status, int termId) {
        ContentValues values = new ContentValues();
        values.put( DBHelper.COURSE_NAME, name );
        values.put( DBHelper.COURSE_START, start );
        values.put( DBHelper.COURSE_END, end );
        values.put( DBHelper.COURSE_STATUS, status );
        values.put(DBHelper.TERM_ID, termId);
        courseFilter = DBHelper.COURSE_ID + "= " + courseId;
        getContentResolver().update( CourseProvider.COURSE_CONTENT_URI, values, courseFilter,
                null);
        setResult( RESULT_OK );
    }


    private void showMentorList(){
        ListView listview = (ListView) findViewById( R.id.mentorListView );
        listview.setAdapter( null );
        mentorAdapter = new MentorAdapter(CourseInsertActivity.this,
                R.layout.mentor_list_item,getMentors( courseId ));
        listview.setAdapter( mentorAdapter );
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long mentorId =mentorAdapter.getItemId( position );
                Intent intent = new Intent(CourseInsertActivity.this, MentorEditorActivity.class);
                Uri uri = Uri.parse(MentorProvider.MENTOR_CONTENT_URI + "/" + mentorId);
                intent.putExtra(MentorProvider.CONTENT_ITEM_TYPE, uri);
                startActivityForResult(intent, MENTOR_REQUEST_CODE);
            }
        });
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
        assessmentAdapter = new AssessmentAdapter(CourseInsertActivity.this,
                R.layout.assessment_list_item,getAssessments( courseId ));
        listview.setAdapter( assessmentAdapter );
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long assessmentId =assessmentAdapter.getItemId( position );
                Intent intent = new Intent(CourseInsertActivity.this, AssessmentEditorActivity.class);
                Uri uri = Uri.parse(AssessmentProvider.ASSESSMENT_CONTENT_URI + "/" + assessmentId);
                intent.putExtra(AssessmentProvider.CONTENT_ITEM_TYPE, uri);
                startActivityForResult(intent, ASSESSMENT_REQUEST_CODE);
            }
        });
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
        noteAdapter = new NoteAdapter(CourseInsertActivity.this,
                R.layout.note_list_item, getNotes( courseId ));
        listview.setAdapter( noteAdapter );
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long noteId =noteAdapter.getItemId( position );
                Intent intent = new Intent(CourseInsertActivity.this, NoteEditorActivity.class);
                Uri uri = Uri.parse(NoteProvider.NOTE_CONTENT_URI + "/" + noteId);
                intent.putExtra(NoteProvider.CONTENT_ITEM_TYPE, uri);
                startActivityForResult(intent, NOTE_REQUEST_CODE);
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

    public void newMentor(View view) {
        Intent intent = new Intent( this, MentorInsertActivity.class );
        count++;
        if (count == 1) {
            courseId = insertBlankCourse();
        }
        Uri uri = Uri.parse( CourseProvider.COURSE_CONTENT_URI + "/" + courseId );
        intent.putExtra( CourseProvider.CONTENT_ITEM_TYPE, uri );
        startActivityForResult( intent, MENTOR_REQUEST_CODE );
    }

    public void newNote(View view) {
        Intent intent  = new Intent(this, NoteInsertActivity.class );
        count++;
        if(count == 1){
            courseId = insertBlankCourse();
        }
        Uri uri = Uri.parse( CourseProvider.COURSE_CONTENT_URI+"/" + courseId );
        intent.putExtra( CourseProvider.CONTENT_ITEM_TYPE, uri );
        startActivityForResult( intent, NOTE_REQUEST_CODE );

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


    private int insertBlankCourse() {
        insertCourse( "None", "None", "None", "None", termId );
        Cursor cursor = getContentResolver().query( CourseProvider.COURSE_CONTENT_URI,
                DBHelper.COURSE_ALL_COLUMNS, courseFilter, null, DBHelper.COURSE_ID + " DESC" );
        cursor.moveToNext();
        int id = cursor.getInt( cursor.getColumnIndex( "_id" ) );
        return id;
    }


    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void restartLoader(){
        getLoaderManager().restartLoader( 0,null,this );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        restartLoader();
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

    public void newAssessment(View view) {
        Intent intent = new Intent( this, AssessmentInsertActivity.class );
        count++;
        if (count == 1) {
            courseId = insertBlankCourse();
        }
        if (assessmentList.size() >= 5) {
            Toast.makeText( this, "Max 5 assessments!", Toast.LENGTH_SHORT ).show();
        } else {
            Uri uri = Uri.parse( CourseProvider.COURSE_CONTENT_URI + "/" + courseId );
            intent.putExtra( CourseProvider.CONTENT_ITEM_TYPE, uri );
            startActivityForResult( intent, ASSESSMENT_REQUEST_CODE );
        }
    }

    @Override
    public Loader<ArrayList> onCreateLoader(int id, Bundle args) {
        return new Loader<ArrayList>(this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList> loader, ArrayList data) {
        mentorAdapter.notifyDataSetChanged();


    }

    @Override
    public void onLoaderReset(Loader<ArrayList> loader) {
        mentorAdapter.clear();
    }

}


