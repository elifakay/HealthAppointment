package com.example.elif.healthappointment;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.elif.healthappointment.Model.ShowAppointments;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShowAppointmentActivity extends AppCompatActivity {

    private final List<ShowAppointments> showAppointmentsList=new ArrayList<>();

    private TextView txtNotAppointments;
    private RecyclerView mShowAppointmentsList;
    private ShowAppointmentsAdapter appointmentsAdapter;

    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;

    private String mCurrentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_appointment);

        mAuth=FirebaseAuth.getInstance();
        mCurrentUserId=mAuth.getCurrentUser().getUid();

        mRootRef= FirebaseDatabase.getInstance().getReference();

        txtNotAppointments=(TextView)findViewById(R.id.txtNotAppointments);

        mShowAppointmentsList=(RecyclerView)findViewById(R.id.lstShowAppointments);
        mShowAppointmentsList.setHasFixedSize(true);
        mShowAppointmentsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        appointmentsAdapter=new ShowAppointmentsAdapter(showAppointmentsList);

        mShowAppointmentsList.setAdapter(appointmentsAdapter);

        loadAppointments();

    }

    private void loadAppointments() {

        DatabaseReference AppointmentsRef=mRootRef.child("Appointments").child(mCurrentUserId);

        AppointmentsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        String aDate=snapshot.child("date").getValue().toString();
                        Date now = new Date();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyy");
                        Date date = simpleDateFormat.parse(aDate);

                        if (date.getTime() > now.getTime() || aDate.equals(simpleDateFormat.format(now))) {

                            ShowAppointments appointments = snapshot.getValue(ShowAppointments.class);
                            showAppointmentsList.add(appointments);
                            appointmentsAdapter.notifyDataSetChanged();
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if (showAppointmentsList.size()==0)
                {
                    txtNotAppointments.setVisibility(View.VISIBLE);
                }
                else
                {
                    txtNotAppointments.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

