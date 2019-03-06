package com.example.joe.wgudegreeplan;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

public class MentorInsertActivity extends AppCompatActivity {


    private EditText editFname;
    private EditText editLname;
    private EditText editPhone;
    private EditText editEmail;
    private Mentor mentor;
    private Course course;
    private int courseId;
    private String mentorFname;
    private String mentorLname;
    private String mentorPhone;
    private String mentorEmail;
    private String action;
    private Uri uri;
    private String courseFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_mentor_insert );

        editFname = findViewById( R.id.mentorFname );
        editLname = findViewById( R.id.mentorLname );
        editPhone = findViewById( R.id.mentorPhone );
        editEmail = findViewById( R.id.mentorEmail );

        Intent intent = getIntent();
        uri = intent.getParcelableExtra( CourseProvider.CONTENT_ITEM_TYPE );
        courseId = Integer.parseInt( uri.getLastPathSegment() );
        setTitle( R.string.new_mentor );
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


    private void finishInsert() {
        mentorFname = editFname.getText().toString().trim();
        mentorLname = editLname.getText().toString().trim();
        mentorPhone = editPhone.getText().toString().trim();
        mentorEmail = editEmail.getText().toString().trim();

        if (validate()) {
            insertMentor(mentorFname, mentorLname, mentorPhone, mentorEmail, courseId);
            finish();
            setResult(RESULT_OK);
        }
    }


    private boolean validate(){
        if(mentorFname.length() == 0 || mentorFname.equals("First")){
            setResult(RESULT_CANCELED);
            Toast.makeText(this, "Enter valid first name!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(mentorLname.length() == 0 || mentorLname.equals("Last")){
            setResult(RESULT_CANCELED);
            Toast.makeText(this, "Enter valid last name!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mentorPhone.length() == 0 || mentorPhone.equals("Phone")){
            setResult(RESULT_CANCELED);
            Toast.makeText(this, "Enter valid phone number!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mentorEmail.length() == 0 || mentorEmail.equals("Email")){
            setResult(RESULT_CANCELED);
            Toast.makeText(this, "Enter valid email!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }
    }


    private void insertMentor(String first, String last, String phone, String email, int courseId){
        ContentValues values = new ContentValues();
        values.put( DBHelper.MENTOR_FNAME, first );
        values.put( DBHelper.MENTOR_LNAME, last );
        values.put( DBHelper.MENTOR_PHONE, phone );
        values.put( DBHelper.MENTOR_EMAIL, email );
        values.put( DBHelper.COURSE_ID, courseId );
        getContentResolver().insert( MentorProvider.MENTOR_CONTENT_URI, values );
        setResult( RESULT_OK );
    }
}

