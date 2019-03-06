package com.example.joe.wgudegreeplan;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class CourseCursorAdapter extends CursorAdapter{
    public CourseCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(
                R.layout.course_list_item, parent, false
        );
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String courseName = cursor.getString(
                cursor.getColumnIndex(DBHelper.COURSE_NAME));
        //String courseStatus = cursor.getString( cursor.getColumnIndex( DBHelper.COURSE_STATUS ) );
        String courseStart = cursor.getString( cursor.getColumnIndex( DBHelper.COURSE_START ) );
        String courseEnd = cursor.getString( cursor.getColumnIndex( DBHelper.COURSE_END ) );
        //String courseNote = cursor.getString( cursor.getColumnIndex( DBHelper.COURSE_NOTE ) );



        int posCourseName = courseName.indexOf(10);
        if (posCourseName != -1) {
            courseName = courseName.substring(0, posCourseName) + " ...";
        }
        TextView tvCourseName = (TextView) view.findViewById(R.id.tvName);
        tvCourseName.setText(courseName);

        int posCourseStart = courseStart.indexOf(10);
        if (posCourseStart != -1) {
            courseStart = courseStart.substring(0, posCourseStart) + " ...";
        }
        TextView tvCourseStart = (TextView) view.findViewById(R.id.tvStart);
        tvCourseStart.setText(courseStart);

        int posCourseEnd = courseEnd.indexOf(10);
        if (posCourseEnd != -1) {
            courseEnd = courseEnd.substring(0, posCourseEnd) + " ...";
        }
        TextView tvCourseEnd = (TextView) view.findViewById(R.id.tvEnd);
        tvCourseEnd.setText(courseEnd);

    }
}