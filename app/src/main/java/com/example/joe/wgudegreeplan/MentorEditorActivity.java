package com.example.joe.wgudegreeplan;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class MentorEditorActivity extends AppCompatActivity {

    private static final int MENTOR_REQUEST_CODE = 1;
    private int mentorId;
    private EditText editFname;
    private EditText editLname;
    private EditText editPhone;
    private EditText editEmail;
    private String mentorFilter;
    private String oldFname;
    private String oldLname;
    private String oldPhone;
    private String oldEmail;
    private String newFname;
    private String newLname;
    private String newPhone;
    private String newEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_mentor_editor );

        editFname = findViewById( R.id.editFname );
        editLname = findViewById( R.id.editLname );
        editPhone = findViewById( R.id.editPhone );
        editEmail = findViewById( R.id.editEmail );

        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra( MentorProvider.CONTENT_ITEM_TYPE );

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle( "Edit Mentor" );
        mentorId = Integer.parseInt( uri.getLastPathSegment() );
        mentorFilter = DBHelper.MENTOR_ID + "=" + mentorId;
        Cursor cursor = getContentResolver().query( uri , DBHelper.MENTOR_ALL_COLUMNS, mentorFilter,
                null,null);
        cursor.moveToFirst();
        oldFname = cursor.getString( cursor.getColumnIndex( DBHelper.MENTOR_FNAME ) );
        oldLname = cursor.getString( cursor.getColumnIndex( DBHelper.MENTOR_LNAME ) );
        oldPhone = cursor.getString( cursor.getColumnIndex( DBHelper.MENTOR_PHONE ) );
        oldEmail = cursor.getString( cursor.getColumnIndex( DBHelper.MENTOR_EMAIL ) );
        editFname.setText( oldFname);
        editLname.setText( oldLname );
        editPhone.setText( oldPhone );
        editEmail.setText( oldEmail );
    }

    @Override
    public void onBackPressed(){
        finishEdit();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch(item.getItemId()){
            case R.id.action_delete:
                deleteMentor();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_mentor, menu );
        return true;
    }

    private void deleteMentor() {
        mentorFilter = DBHelper.MENTOR_ID + " = " + mentorId;
        getContentResolver().delete( MentorProvider.MENTOR_CONTENT_URI, mentorFilter,null );
        Toast.makeText( this, "Mentor Deleted", Toast.LENGTH_SHORT ).show();
        setResult( RESULT_OK );
        finish();
    }

    private void finishEdit() {
        newFname = editFname.getText().toString().trim();
        newLname = editLname.getText().toString().trim();
        newPhone = editPhone.getText().toString().trim();
        newEmail = editEmail.getText().toString().trim();

        if (validate()) {
            updateMentor(newFname, newLname, newPhone, newEmail);
            setResult(RESULT_OK);
        }
    }

    private boolean validate(){
        if(newFname.length() == 0 || newFname.equals("First")){
            setResult(RESULT_CANCELED);
            Toast.makeText(this, "Enter valid first name!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(newLname.length() == 0 || newLname.equals("Last")){
            setResult(RESULT_CANCELED);
            Toast.makeText(this, "Enter valid last name!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(newPhone.length() == 0 || newPhone.equals("Phone")){
            setResult(RESULT_CANCELED);
            Toast.makeText(this, "Enter valid phone number!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(newEmail.length() == 0 || newEmail.equals("Email")){
            setResult(RESULT_CANCELED);
            Toast.makeText(this, "Enter valid email!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }
    }

    private void updateMentor(String fname, String lname, String phone, String email) {
        ContentValues values = new ContentValues(  );
        values.put( DBHelper.MENTOR_FNAME, fname );
        values.put( DBHelper.MENTOR_LNAME, lname);
        values.put( DBHelper.MENTOR_PHONE, phone );
        values.put( DBHelper.MENTOR_EMAIL, email );
        getContentResolver().update( MentorProvider.MENTOR_CONTENT_URI, values, mentorFilter,
                null);
        Toast.makeText( this, "Mentor Updated", Toast.LENGTH_SHORT ).show();
        setResult( RESULT_OK );
    }
}
