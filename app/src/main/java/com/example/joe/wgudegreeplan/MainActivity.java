package com.example.joe.wgudegreeplan;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.LoaderManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

import static com.example.joe.wgudegreeplan.CourseInsertActivity.intentArrayList;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
{
    public static final String CHANNEL_ID = "1";
    private CursorAdapter cursorAdapter;
    private static final int TERM_REQUEST_CODE = 1;
    private Context context;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        DBHelper helper = new DBHelper( this );
        helper.createTables();
        //createNotificationChannel();
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        this.context = this;
        cursorAdapter = new TermCursorAdapter( this,null,0 );


        final ListView list = findViewById( R.id.termList);
        list.setAdapter( cursorAdapter );

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, TermActivity.class);
                Uri uri = Uri.parse(TermProvider.TERM_CONTENT_URI + "/" + id);
                intent.putExtra(TermProvider.CONTENT_ITEM_TYPE, uri);
                startActivityForResult(intent, TERM_REQUEST_CODE);
            }
        });
        getLoaderManager().initLoader( 0,null,this );


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.menu_main, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void newTerm(View view) {
        Intent intent  = new Intent(this, TermInsertActivity.class );
        startActivityForResult( intent,  TERM_REQUEST_CODE);
    }

    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void restartLoader(){
        getLoaderManager().restartLoader( 0,null,this );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == TERM_REQUEST_CODE ){
            restartLoader();
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader( this, TermProvider.TERM_CONTENT_URI, null,
                null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor( data );
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor( null );
    }

}
