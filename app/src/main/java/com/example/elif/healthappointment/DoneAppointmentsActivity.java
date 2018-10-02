package com.example.elif.healthappointment;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class DoneAppointmentsActivity extends AppCompatActivity {

    private TextView txtDoneAppointmentDoctor,txtDoneAppointmentDepartment,txtDoneAppointmentDate,txtDoneAppointmentTime,txtDoneAppointmentAbout;
    private EditText edtDoneAppointmentComplaint;
    private Button btnDoneAppointment;

    private DatabaseReference mRootRef;
    private DatabaseReference mDepartmentsDatabase;
    private FirebaseUser mCurrentUser;

    private String mCurrentDoctorId,date,time,department,doctorNameSurname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_appointments);

        txtDoneAppointmentDoctor=(TextView)findViewById(R.id.txtDoneAppointmentDoctor);
        txtDoneAppointmentDepartment=(TextView)findViewById(R.id.txtDoneAppointmentDepartment);
        txtDoneAppointmentDate=(TextView)findViewById(R.id.txtDoneAppointmentDate);
        txtDoneAppointmentTime=(TextView)findViewById(R.id.txtDoneAppointmentTime);
        txtDoneAppointmentAbout=(TextView)findViewById(R.id.txtDoneAppointmentAbout);
        edtDoneAppointmentComplaint=(EditText)findViewById(R.id.edtDoneAppointmentComplaint);
        btnDoneAppointment=(Button)findViewById(R.id.btnDoneAppointment);

        mRootRef= FirebaseDatabase.getInstance().getReference();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        Bundle extra = getIntent().getExtras();
        if (extra != null)
        {
            date=extra.getString("DATE");
            time=extra.getString("TIME");
            department=extra.getString("DOCTOR_DEPARTMENT");
            doctorNameSurname=extra.getString("DOCTOR_NAME_SURNAME");
            mCurrentDoctorId=extra.getString("DOCTOR_KEY");

            txtDoneAppointmentDepartment.setText(department);
            txtDoneAppointmentDate.setText("Tarih : "+date);
            txtDoneAppointmentTime.setText("Saat : "+time);
            txtDoneAppointmentDoctor.setText(doctorNameSurname);

            mDepartmentsDatabase = FirebaseDatabase.getInstance().getReference().child("Departments").child(txtDoneAppointmentDepartment.getText().toString());
            mDepartmentsDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String about = dataSnapshot.child("about").getValue().toString();
                    txtDoneAppointmentAbout.setText(about);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        btnDoneAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppoinmentSend();

                Intent showAppoinment=new Intent(DoneAppointmentsActivity.this, ShowAppointmentActivity.class);
                startActivity(showAppoinment);
                finish();
            }
        });

    }

    private void AppoinmentSend() {

        if (!TextUtils.isEmpty(edtDoneAppointmentComplaint.getText().toString())) {

            String currentUserRef = "Appointments/" + mCurrentUser.getUid() + "/" + mCurrentDoctorId;
            String appointmentUserRef = "Appointments/" + mCurrentDoctorId + "/" + mCurrentUser.getUid();

            DatabaseReference userAppointmentPush = mRootRef.child("Appointments").child(mCurrentUser.getUid()).child(mCurrentDoctorId).push();

            String pushId = userAppointmentPush.getKey();

            HashMap<String,String> appointmentMap=new HashMap<>();
            appointmentMap.put("date",date);
            appointmentMap.put("time",time);
            appointmentMap.put("doctor_name",doctorNameSurname);
            appointmentMap.put("department",department);
            appointmentMap.put("complaint",edtDoneAppointmentComplaint.getText().toString());

            Map appointmentDoctorUserMap = new HashMap();
            appointmentDoctorUserMap.put(currentUserRef + "/" + pushId, appointmentMap);
            appointmentDoctorUserMap.put(appointmentUserRef + "/" + pushId, appointmentMap);


            mRootRef.updateChildren(appointmentDoctorUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                    if (databaseError != null) {
                        Log.d("APPOINMENT_LOG", databaseError.getMessage().toString());
                    }
                }
            });
        }
        else
        {
            Toast.makeText(this, "Lütfen Şikayetinizi Giriniz!...", Toast.LENGTH_SHORT).show();
        }
    }
}

