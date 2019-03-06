package com.example.joe.wgudegreeplan;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AssessmentAdapter extends ArrayAdapter<Assessment> {

    private final Context context;
    private final ArrayList<Assessment> data;
    private final int layoutResourceId;


    public AssessmentAdapter(Context context, int layoutResourceId, ArrayList<Assessment> data) {
        super( context, layoutResourceId, data );
        this.context = context;
        this.data = data;
        this.layoutResourceId = layoutResourceId;
    }



    static class ViewHolder {
        TextView tvName;
        TextView tvType;
        TextView tvDueDate;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        AssessmentAdapter.ViewHolder holder = null;
        // Get the data item for this position
        // Check if an existing view is being reused, otherwise inflate the view
        if (row == null) {
            // convertView = LayoutInflater.from(getContext()).inflate(R.layout.mentor_list_item, parent, false);
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate( layoutResourceId, parent, false );

            holder = new ViewHolder();
            holder.tvName = (TextView) row.findViewById( R.id.tvName );
            holder.tvType = (TextView) row.findViewById( R.id.tvType );
            holder.tvDueDate = (TextView) row.findViewById( R.id.tvDueDate );
            row.setTag( holder );

        } else {
            holder = (AssessmentAdapter.ViewHolder)row.getTag();
        }
        Assessment a = data.get(position);
        holder.tvName.setText( a.getAssessmentTitle() );
        holder.tvType.setText( a.getAssessmentType());
        holder.tvDueDate.setText( a.getAssessmentDueDate() );
        return row;
    }


    @Override
    public long getItemId(int position) {
        Assessment a = super.getItem( position );
        return a.getAssessmentId();
    }

}
