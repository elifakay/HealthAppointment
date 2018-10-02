package com.example.elif.healthappointment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import com.example.elif.healthappointment.Model.Doctors;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;



public class MainActivity extends AppCompatActivity {

    private RecyclerView mDoctorsList;
    private PopupWindow mPopupWindow;
    private RelativeLayout mRelativeLayout;

    private DatabaseReference mDoctorsDatabase;

    private ProgressDialog mDoctorListProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl);

        //ProgressDialog
        mDoctorListProgress = new ProgressDialog(this);

        mDoctorListProgress.setTitle("Doctor List");
        mDoctorListProgress.setMessage("Please wait while listing doctors");
        mDoctorListProgress.setCanceledOnTouchOutside(false);
        mDoctorListProgress.show();

        mDoctorsDatabase= FirebaseDatabase.getInstance().getReference().child("Doctors");

        mDoctorsList = (RecyclerView) findViewById(R.id.doctorsList);
        mDoctorsList.setHasFixedSize(true);
        mDoctorsList.setLayoutManager(new LinearLayoutManager(this));

        Button btnShowAppoinment=(Button)findViewById(R.id.btnShowAppoinment);
        btnShowAppoinment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent appointmentsIntent = new Intent(MainActivity.this, ShowAppointmentActivity.class);
                startActivity(appointmentsIntent);
            }
        });

        Button btnLogOut=(Button)findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                sendToStart();
            }
        });
    }
    private void sendToStart() {
        Intent startIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(startIntent);
        finish();
    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Doctors, DoctorsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Doctors,DoctorsViewHolder>(
                Doctors.class,
                R.layout.doctor_single_layout,
                DoctorsViewHolder.class,
                mDoctorsDatabase
        ) {
            @Override
            protected void populateViewHolder(DoctorsViewHolder doctorsViewHolder, final Doctors doctors, final int position) {

                doctorsViewHolder.setNameSurname(doctors.getName_surname());
                doctorsViewHolder.setDepartment(doctors.getDepartment());
                doctorsViewHolder.setImage(doctors.getImage(), getApplicationContext());
                doctorsViewHolder.setExperiene("Deneyim:"+doctors.getExperience() );
                doctorsViewHolder.setCity("Şehir: "+doctors.getCity() );
                doctorsViewHolder.setGsm(doctors.getGsm() );

                doctorsViewHolder.mView.findViewById(R.id.btnSingleAppointment).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent appointmentsIntent = new Intent(MainActivity.this, AppointmentsActivity.class);

                        Bundle dataSend=new Bundle();
                        dataSend.putString("DOCTOR_KEY",getRef(position).getKey());
                        dataSend.putString("DOCTOR",doctors.getName_surname());
                        dataSend.putString("DOCTOR_DEPARTMENT",doctors.getDepartment());
                        appointmentsIntent.putExtras(dataSend);

                        startActivity(appointmentsIntent);
                    }
                });

                doctorsViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                        View customPopupView = inflater.inflate(R.layout.custom_popup_layout,null);

                        mPopupWindow = new PopupWindow(
                                customPopupView,
                                LayoutParams.WRAP_CONTENT,
                                LayoutParams.WRAP_CONTENT
                        );
                        if(Build.VERSION.SDK_INT>=21){
                            mPopupWindow.setElevation(5.0f);
                        }

                        TextView txtDoctorPopupAbout=(TextView)customPopupView.findViewById(R.id.txtDoctorPopupAbout);
                        txtDoctorPopupAbout.setText( doctors.getName_surname()+" Hakkında \n\n"+doctors.getAbout());

                        ImageButton imgBtnPopupClose = (ImageButton) customPopupView.findViewById(R.id.imgBtnPopupClose);
                        imgBtnPopupClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Dismiss the popup window
                                mPopupWindow.dismiss();
                            }
                        });
                        mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER,0,0);
                    }
                });

                mDoctorListProgress.hide();
            }
        };

        mDoctorsList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class DoctorsViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public DoctorsViewHolder(View itemView){
            super(itemView);

            mView=itemView;
        }
        public void setNameSurname(String nameSurname)
        {
            TextView doctorNameSurnameView=(TextView)mView.findViewById(R.id.txtDoctorSingleNameSurname);
            doctorNameSurnameView.setText(nameSurname);
        }
        public void setDepartment(String department)
        {
            TextView doctorDepartmentView=(TextView)mView.findViewById(R.id.txtDoctorSingleDepartment);
            doctorDepartmentView.setText(department);
        }
        public void setImage(String image, Context context)
        {
            ImageView doctorImageView=(ImageView)mView.findViewById(R.id.imgDoctorSingleImage);

            final ProgressBar progressBarImage=(ProgressBar)mView.findViewById(R.id.progressBarImage);
            progressBarImage.setVisibility(View.VISIBLE);

            Picasso.with(context)
                    .load(image)
                    .placeholder(R.drawable.doktor_profile)
                    .into(doctorImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressBarImage.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {

                        }
                    });
        }
        public void setExperiene(String experiene)
        {
            TextView doctorExperienceView=(TextView)mView.findViewById(R.id.txtDoctorSingleExperience);
            doctorExperienceView.setText(experiene);
        }
        public void setCity(String city)
        {
            TextView doctorCityView=(TextView)mView.findViewById(R.id.txtDoctorSingleCity);
            doctorCityView.setText(city);
        }
        public void setGsm(String gsm)
        {
            TextView doctorGsmView=(TextView)mView.findViewById(R.id.txtDoctorSingleGsm);
            doctorGsmView.setText(gsm);
        }
    }
}
