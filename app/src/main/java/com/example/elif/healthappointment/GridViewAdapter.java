package com.example.elif.healthappointment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GridViewAdapter extends ArrayAdapter<String> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<String> data = new ArrayList<String>();

    public GridViewAdapter(Context context, int layoutResourceId, ArrayList<String> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null)
        {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.appointmentTime = (TextView) row.findViewById(R.id.txtGridItemAppointments);
            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }

        String item = data.get(position);
        holder.appointmentTime.setText(item);
        return row;
    }

    static class ViewHolder {
        TextView appointmentTime;
    }
}
