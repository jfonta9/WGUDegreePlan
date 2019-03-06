package com.example.joe.wgudegreeplan;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class MentorAdapter extends ArrayAdapter<Mentor> {

    private final Context context;
    private final ArrayList<Mentor> data;
    private final int layoutResourceId;


    public MentorAdapter(Context context, int layoutResourceId, ArrayList<Mentor> data) {
        super( context, layoutResourceId, data );
        this.context = context;
        this.data = data;
        this.layoutResourceId = layoutResourceId;
    }



    static class ViewHolder {
        TextView tvName;
        TextView tvPhone;
        TextView tvEmail;
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
            holder.tvName = (TextView) row.findViewById( R.id.tvName );
            holder.tvPhone = (TextView) row.findViewById( R.id.tvPhone );
            holder.tvEmail = (TextView) row.findViewById( R.id.tvEmail );

            row.setTag( holder );
        } else {
            holder = (ViewHolder)row.getTag();
        }
        Mentor mentor = data.get(position);
        holder.tvName.setText( mentor.getMentorFname() + " " + mentor.getMentorLname() );
        holder.tvPhone.setText( mentor.getMentorPhone() );
        holder.tvEmail.setText( mentor.getMentorEmail() );
        return row;
    }

    @Override
    public long getItemId(int position) {
        Mentor mentor = super.getItem( position );
        return mentor.getMentorId();
    }
}
