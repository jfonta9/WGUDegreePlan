package com.example.joe.wgudegreeplan;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class TermCursorAdapter extends CursorAdapter{
    public TermCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(
                R.layout.term_list_item, parent, false
        );
    }




    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String termName = cursor.getString(
                cursor.getColumnIndex(DBHelper.TERM_NAME));

        int posTermName = termName.indexOf(10);
        if (posTermName != -1) {
            termName = termName.substring(0, posTermName) + " ...";
        }

        TextView tvTermName = (TextView) view.findViewById(R.id.tvName);
        tvTermName.setText(termName);

        String termStart = cursor.getString(
                cursor.getColumnIndex(DBHelper.TERM_START));

        int posTermStart = termStart.indexOf(10);
        if (posTermStart != -1) {
            termStart = termStart.substring(0, posTermStart) + " ...";
        }

        TextView tvTermStart = (TextView) view.findViewById(R.id.tvStart);
        tvTermStart.setText(termStart);

        String termEnd = cursor.getString(
                cursor.getColumnIndex(DBHelper.TERM_END));

        int posTermEnd = termEnd.indexOf(10);
        if (posTermEnd != -1) {
            termEnd = termEnd.substring(0, posTermEnd) + " ...";
        }

        TextView tvTermEnd = (TextView) view.findViewById(R.id.tvEnd);
        tvTermEnd.setText(termEnd);

    }
}
