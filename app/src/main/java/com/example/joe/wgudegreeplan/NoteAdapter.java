package com.example.joe.wgudegreeplan;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NoteAdapter extends ArrayAdapter<Note> {

    private final Context context;
    private final ArrayList<Note> data;
    private final int layoutResourceId;


    public NoteAdapter(Context context, int layoutResourceId, ArrayList<Note> data) {
        super( context, layoutResourceId, data );
        this.context = context;
        this.data = data;
        this.layoutResourceId = layoutResourceId;
    }



    static class ViewHolder {
        TextView tvNoteContent;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder = null;
        // Get the data item for this position
        // Check if an existing view is being reused, otherwise inflate the view
        if (row == null) {
            // convertView = LayoutInflater.from(getContext()).inflate(R.layout.mentor_list_item, parent, false);
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate( layoutResourceId, parent, false );

            holder = new ViewHolder();
            holder.tvNoteContent = (TextView) row.findViewById(R.id.noteText );

            row.setTag( holder );
        } else {
            holder = (ViewHolder)row.getTag();
        }
        Note note = data.get(position);
        holder.tvNoteContent.setText( note.getNoteContent() );

        return row;
    }

    @Override
    public long getItemId(int position) {
        Note note = super.getItem( position );
        return note.getId();
    }
}
