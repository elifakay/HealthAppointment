package com.example.elif.healthappointment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elif.healthappointment.Model.NullAppointments;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AppointmentsActivity extends AppCompatActivity {

    private ProgressDialog mAppointmentsProgress;
    private ListView mAppointmentsList;
    private TextView txtAppointmentsDoctorDepartment,txtAppointmentsDoctorNameSurname;

    private DatabaseReference mAppointmentsDatabase;

    private String currentDoctorKey="";

    ArrayList<NullAppointments> nullAppointments;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        mAppointmentsProgress = new ProgressDialog(this);
        mAppointmentsProgress.setTitle("Null Appointments List");
        mAppointmentsProgress.setMessage("Please wait while listing null appointments");
        mAppointmentsProgress.setCanceledOnTouchOutside(false);
        mAppointmentsProgress.show();

        //Appointments List
        mAppointmentsList = (ListView) findViewById(R.id.appointmentsList);

        //Doctor Name Surname  ---- Department
        txtAppointmentsDoctorNameSurname = (TextView) findViewById(R.id.txtAppointmentsDoctorNameSurname);
        txtAppointmentsDoctorDepartment = (TextView) findViewById(R.id.txtAppointmentsDoctorDepartment);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            currentDoctorKey = extra.getString("DOCTOR_KEY");
            txtAppointmentsDoctorNameSurname.setText(extra.getString("DOCTOR"));
            txtAppointmentsDoctorDepartment.setText(extra.getString("DOCTOR_DEPARTMENT"));

            mAppointmentsDatabase = FirebaseDatabase.getInstance().getReference().child("NullAppointments").child(currentDoctorKey);
        }

        customAdapter=new CustomAdapter();
        nullAppointments=new ArrayList<>();
        mAppointmentsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = -1;
                for (DataSnapshot snapshotDate : dataSnapshot.getChildren()) {

                    try {
                        String aDate = String.format("%s.%s.%s", snapshotDate.getKey().substring(0, 2), snapshotDate.getKey().substring(2, 4), snapshotDate.getKey().substring(4, 8));
                        Date now = new Date();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyy");
                        Date date = simpleDateFormat.parse(aDate);

                        if (date.getTime() > now.getTime() || aDate.equals(simpleDateFormat.format(now))) {

                            i++;
                            nullAppointments.add(i, new NullAppointments(aDate));

                            for (DataSnapshot snapshotTime : snapshotDate.getChildren()) {

                                nullAppointments.get(i).time.add(snapshotTime.getKey());
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    mAppointmentsList.setAdapter(customAdapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return nullAppointments.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.appointments_single_layout, null);

            final  TextView appointmentsDateView = (TextView) convertView.findViewById(R.id.txtSingleAppointmentDate);

            appointmentsDateView.setText(nullAppointments.get(position).getDate());
            ArrayList<String> time=new ArrayList<>();

            for (int i=0;i<nullAppointments.get(position).time.size();i++) {
                time.add(nullAppointments.get(position).time.get(i));
            }

            GridView gridView = (GridView) convertView.findViewById(R.id.gridviewAppoinment);
            GridViewAdapter gridAdapter = new GridViewAdapter(convertView.getContext(), R.layout.grid_item_layout, time);
            gridView.setAdapter(gridAdapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                    String item = (String) parent.getItemAtPosition(position);

                    Intent doneAppointmentsIntent = new Intent(AppointmentsActivity.this, DoneAppointmentsActivity.class);

                    Bundle dataSend=new Bundle();
                    dataSend.putString("DOCTOR_KEY",currentDoctorKey);
                    dataSend.putString("DOCTOR_NAME_SURNAME",txtAppointmentsDoctorNameSurname.getText().toString());
                    dataSend.putString("DOCTOR_DEPARTMENT",txtAppointmentsDoctorDepartment.getText().toString());
                    dataSend.putString("DATE",appointmentsDateView.getText().toString());
                    dataSend.putString("TIME",item);
                    doneAppointmentsIntent.putExtras(dataSend);

                    startActivity(doneAppointmentsIntent);

                    //Toast.makeText(getApplicationContext(), item + "Date: " + appointmentsDateView.getText(), Toast.LENGTH_SHORT).show();
                }
            });
            //}
            mAppointmentsProgress.hide();

            return convertView;
        }
    }
}

