package com.example.joe.wgudegreeplan;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class NoteEditorActivity extends AppCompatActivity {


    private static final int NOTE_REQUEST_CODE = 1;
    private int noteId;
    private EditText editNote;
    private String noteFilter;
    private String oldNote;
    private String phoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_note_editor );

        editNote = findViewById( R.id.noteEdit );

        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra( NoteProvider.CONTENT_ITEM_TYPE );

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle( "Edit Note" );
        noteId = Integer.parseInt( uri.getLastPathSegment() );
        noteFilter = DBHelper.NOTE_ID+ "=" + noteId;
        Cursor cursor = getContentResolver().query( uri , DBHelper.NOTE_ALL_COLUMNS, noteFilter,
                null,null);
        cursor.moveToFirst();
        oldNote = cursor.getString( cursor.getColumnIndex( DBHelper.NOTE_CONTENT ) );
        editNote.setText(oldNote);
    }


    private void shareNotes(){
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);

            smsIntent.setData(Uri.parse("smsto:"));
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("sms_body"  , editNote.getText().toString().trim());

            try {
                startActivity(smsIntent);
                finish();
                Log.i("Finished sending SMS...", "");
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(NoteEditorActivity.this,
                        "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
            }
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch(item.getItemId()){
            case R.id.action_delete:
                deleteMentor();
                break;
            case android.R.id.home:
                finishEdit();
                break;
            case R.id.action_share:
                shareNotes();
                break;
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_note, menu );
        return true;
    }

    private void deleteMentor() {
        noteFilter = DBHelper.NOTE_ID + " = " + noteId;
        getContentResolver().delete( NoteProvider.NOTE_CONTENT_URI, noteFilter,null );
        Toast.makeText( this, "Note Deleted", Toast.LENGTH_SHORT ).show();
        setResult( RESULT_OK );
        finish();
    }

    private void finishEdit(){
        String newNote = editNote.getText().toString().trim();
        if(newNote.length() == 0) {
            setResult(RESULT_CANCELED);
        }else{
            updateNote(newNote);
            setResult( RESULT_OK );
            finish();
        }
    }


    private void updateNote(String note) {
        ContentValues values = new ContentValues(  );
        values.put( DBHelper.NOTE_CONTENT, note );
        getContentResolver().update( NoteProvider.NOTE_CONTENT_URI, values, noteFilter,
                null);
        Toast.makeText( this, "Note Updated", Toast.LENGTH_SHORT ).show();
        setResult( RESULT_OK );
    }


}


