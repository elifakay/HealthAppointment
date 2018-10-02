package com.example.elif.healthappointment;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.elif.healthappointment.Model.ShowAppointments;

import java.util.List;

public class ShowAppointmentsAdapter extends RecyclerView.Adapter<ShowAppointmentsAdapter.AppointmentViewHolder> {

    private List<ShowAppointments> mShowAppointmentsList;

    public ShowAppointmentsAdapter(List<ShowAppointments> mShowAppointmentsList) {
        this.mShowAppointmentsList = mShowAppointmentsList;
    }

    @Override
    public AppointmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.appoinments_show_single_layout, parent, false);

        return new AppointmentViewHolder(v);
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder {

        public TextView doctorNameView;
        public TextView departmentView;
        public TextView appointmentDateView;
        public TextView appointmentTimeView;
        public TextView complaintView;

        public AppointmentViewHolder(View mView) {
            super(mView);

            doctorNameView=(TextView)mView.findViewById(R.id.txtShowAppointmentDoctor);
            departmentView=(TextView)mView.findViewById(R.id.txtShowAppointmentDepartment);
            appointmentDateView=(TextView)mView.findViewById(R.id.txtShowAppointmentDate);
            appointmentTimeView=(TextView)mView.findViewById(R.id.txtShowAppointmentTime);
            complaintView=(TextView)mView.findViewById(R.id.txtShowAppointmentComplaint);
        }
    }
    @Override
    public void onBindViewHolder(final AppointmentViewHolder viewHolder, int i) {

        ShowAppointments a = mShowAppointmentsList.get(i);

        viewHolder.doctorNameView.setText(a.getDoctor_name());
        viewHolder.departmentView.setText(a.getDepartment());
        viewHolder.appointmentDateView.setText(a.getDate());
        viewHolder.appointmentTimeView.setText(a.getTime());
        viewHolder.complaintView.setText(a.getComplaint());
    }

    @Override
    public int getItemCount() {
        return mShowAppointmentsList.size();
    }
}

