package com.example.joe.wgudegreeplan;

import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.content.CursorLoader;

import android.support.annotation.Nullable;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.widget.AdapterView.*;

public class TermActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
{

    private static final int EDIT_COURSE_REQUEST_CODE = 1;
    private String action;
    private String courseId;
    private long termId;
    private String termFilter;
    private String courseFilter;
    private String termName;
    private String termStart;
    private String termEnd;
    private CursorAdapter courseCursorAdapter;
    private Uri termUri;
    private TextView startEndDate;
    private ListView listView;
    private TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_term );

        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );

        //startEndDate = findViewById( R.id.tvStartEnd );
        Intent intent = getIntent();
        termUri = intent.getParcelableExtra(TermProvider.CONTENT_ITEM_TYPE);

        currentTerm();
        courseCursorAdapter = new CourseCursorAdapter( TermActivity.this,null,0 );

        listView = (ListView) findViewById( android.R.id.list );
        listView.setAdapter( courseCursorAdapter );
        listView.addHeaderView( dateHeader());
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TermActivity.this, CourseEditorActivity.class);
                Uri courseUri = Uri.parse( CourseProvider.COURSE_CONTENT_URI + "/" + id);
                intent.putExtra(CourseProvider.CONTENT_ITEM_TYPE, courseUri);
                startActivityForResult(intent, EDIT_COURSE_REQUEST_CODE);
            }
        });
        getLoaderManager().initLoader( 0,null,this );
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private TextView dateHeader(){
        tv = new TextView(TermActivity.this);
        tv.setText( termStart + " - " + termEnd );
        tv.setTextSize( 20 );
        tv.setTextAlignment( TEXT_ALIGNMENT_CENTER );
        tv.setPadding( 0,25,0,25 );
        return tv;
    }


    private void currentTerm(){
        termId = Long.valueOf(termUri.getLastPathSegment());
        termFilter = DBHelper.TERM_ID + "=" + termId;
        Cursor cursor = getContentResolver().query( termUri , DBHelper.TERM_ALL_COLUMNS, termFilter,
                null,null);
        cursor.moveToFirst();
        termName = cursor.getString(cursor.getColumnIndex( DBHelper.TERM_NAME ));
        termStart = cursor.getString( cursor.getColumnIndex( DBHelper.TERM_START ) );
        termEnd = cursor.getString( cursor.getColumnIndex( DBHelper.TERM_END));
        setTitle( termName );
        cursor.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch(item.getItemId()){

            case R.id.action_edit:
                editTerm();
                break;
            case R.id.action_delete:
                deleteTerm();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }




    @Override
    public void onBackPressed(){
        finish();
    }

    public void newCourse(View view) {
        Intent intent = new Intent(TermActivity.this, CourseInsertActivity.class);
        Uri uri = Uri.parse(TermProvider.CONTENT_ITEM_TYPE + "/" + termId);
        intent.putExtra(TermProvider.CONTENT_ITEM_TYPE, uri);
        startActivityForResult(intent, EDIT_COURSE_REQUEST_CODE);
    }


    private void editTerm() {
        Intent intent = new Intent(TermActivity.this, TermEditorActivity.class);
        Uri uri = Uri.parse( TermProvider.TERM_CONTENT_URI+"/" + termId );
        intent.putExtra( TermProvider.CONTENT_ITEM_TYPE, uri );
        startActivityForResult( intent, EDIT_COURSE_REQUEST_CODE );
    }

    private void deleteTerm() {
        termFilter = DBHelper.TERM_ID + " = " + termId;
        if(checkForCourses()){
            Toast.makeText( this, "Must delete Course(s) first!", Toast.LENGTH_LONG  ).show();
        }else{
            getContentResolver().delete( TermProvider.TERM_CONTENT_URI, termFilter,null );
            Toast.makeText( this, "Term Deleted", Toast.LENGTH_SHORT ).show();
            setResult( RESULT_OK );
            finish();
        }
    }
    
    private boolean checkForCourses(){
        Cursor cursor = getContentResolver().query( CourseProvider.COURSE_CONTENT_URI , 
                DBHelper.ASSESSMENT_ALL_COLUMNS, courseFilter,
                null,null);
        if(cursor.moveToFirst()){
            cursor.close();
            return true;
        }else{
            cursor.close();
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.menu_term, menu );
        return true;
    }


    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void restartLoader(){
        getLoaderManager().restartLoader( 0,null,this );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == EDIT_COURSE_REQUEST_CODE ){
            currentTerm();
            tv.setText( termStart + " - " + termEnd );
            restartLoader();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        courseFilter = DBHelper.TERM_ID + "=" + termId;
        return new CursorLoader( this, CourseProvider.COURSE_CONTENT_URI, null,
                courseFilter,null,null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        courseCursorAdapter.swapCursor( data );
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        courseCursorAdapter.swapCursor( null );
    }


}
