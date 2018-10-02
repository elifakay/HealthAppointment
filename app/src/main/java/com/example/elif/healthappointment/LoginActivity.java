package com.example.elif.healthappointment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private Button btnKayitOl, btnGirisYap;
    private EditText edtEmail, edtSifre;

    private ProgressDialog mLoginProgress;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        }

        //ProgressDialog
        mLoginProgress = new ProgressDialog(this);

        //Android
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtSifre = (EditText) findViewById(R.id.edtSifre);
        btnKayitOl = (Button) findViewById(R.id.btnKayitOl);
        btnGirisYap = (Button) findViewById(R.id.btnGirisYap);


        btnGirisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String password = edtSifre.getText().toString();

                LoginUser(email, password);

            }
        });

        btnKayitOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(register);

            }
        });
    }

    private void LoginUser(String email, String password) {

        if (!validate(email,password)) {
            onSignInFailed();
            return;
        }

        btnGirisYap.setEnabled(false);

        mLoginProgress.setTitle("Logging In");
        mLoginProgress.setMessage("Please wait while checking your information");
        mLoginProgress.setCanceledOnTouchOutside(false);
        mLoginProgress.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    mLoginProgress.dismiss();

                    FirebaseUser currenUser = mAuth.getCurrentUser();
                    String currentUId = currenUser.getUid();

                    mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUId);
                    mUserDatabase.keepSynced(true);

                    Intent homeIntent = new Intent(LoginActivity.this, MainActivity.class);
                    homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(homeIntent);
                    finish();

                } else {
                    mLoginProgress.hide();
                    onSignInFailed();
                }
            }
        });
    }

    private void onSignInFailed() {

        btnGirisYap.setEnabled(true);
        Toast.makeText(LoginActivity.this, "Cannot Sign in.Please check the from and try again.", Toast.LENGTH_SHORT).show();
    }

    public boolean validate(String email, String password) {
        boolean valid = true;

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Enter a valid email");
            valid = false;
        } else {
            edtEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 10) {
            edtSifre.setError("Between 6 and 10 characters");
            valid = false;
        } else {
            edtSifre.setError(null);
        }

        return valid;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Check if user signed in and update UI accordingly
        FirebaseUser currenUser = mAuth.getCurrentUser();
        if (currenUser != null) {
            Intent startIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(startIntent);
            finish();
        }
    }
}

