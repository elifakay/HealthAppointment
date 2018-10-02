package com.example.elif.healthappointment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.elif.healthappointment.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private Button btnKayitOl;
    private EditText edtKayitKullaniciAdi,edtKayitEmail, edtKayitSifre;

    private ProgressDialog mRegisterProgress;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        //ProgressDialog
        mRegisterProgress = new ProgressDialog(this);

        //Android
        edtKayitKullaniciAdi = (EditText) findViewById(R.id.edtKayitKullaniciAdi);
        edtKayitEmail = (EditText) findViewById(R.id.edtKayitEmail);
        edtKayitSifre = (EditText) findViewById(R.id.edtKayitSifre);
        btnKayitOl = (Button) findViewById(R.id.btnKayitOl);

        btnKayitOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=edtKayitKullaniciAdi.getText().toString();
                String email = edtKayitEmail.getText().toString();
                String password = edtKayitSifre.getText().toString();

                RegisterUser(username,email, password);

            }
        });
    }

    private void onRegisterFailed() {

        Toast.makeText(getBaseContext(), "Cannot Save in.Please check the from and try again.", Toast.LENGTH_LONG).show();
        btnKayitOl.setEnabled(true);
    }

    private void RegisterUser(final String username, String email, String password) {

        if (!validate(username,email,password)) {
            onRegisterFailed();
            return;
        }

        btnKayitOl.setEnabled(false);

        mRegisterProgress.setTitle("Registering User");
        mRegisterProgress.setMessage("Please wait while we create your account !");
        mRegisterProgress.setCanceledOnTouchOutside(false);
        mRegisterProgress.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String uId = currentUser.getUid();

                    mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uId);

                    Users user=new Users(username,"default","default","0");

                    mUserDatabase.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mRegisterProgress.dismiss();

                                Intent mainIntent=new Intent(RegisterActivity.this,MainActivity.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                                finish();
                            }
                            else
                            {
                                mRegisterProgress.hide();
                                onRegisterFailed();
                            }
                        }
                    });

                } else {
                    mRegisterProgress.hide();
                    btnKayitOl.setEnabled(true);
                    Toast.makeText(RegisterActivity.this, "Cannot Sign in.Please check the from and try again.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public boolean validate(String username, String email, String password) {
        boolean valid = true;

        if (username.isEmpty() || username.length() < 3 || username.length() > 10) {
            edtKayitKullaniciAdi.setError("Enter a valid username");
            valid = false;
        } else {
            edtKayitKullaniciAdi.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtKayitEmail.setError("Enter a valid email");
            valid = false;
        } else {
            edtKayitEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 10) {
            edtKayitSifre.setError("Between 6 and 10 characters");
            valid = false;
        } else {
            edtKayitSifre.setError(null);
        }

        return valid;
    }

}

