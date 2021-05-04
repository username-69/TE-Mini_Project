package com.example.vaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    //using default child and list so that there would be atleast one child when users create his account on App, because of fetching issues while adding more children
    private final List<childDB> defaultChildList = new ArrayList<>();
    private final DOB defaultDOB = new DOB(01, 01, 2000);
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser firebaseUser;
    private Button btnMainRegisterMe;
    private EditText edtMainRegisterEmail, edtMainRegisterPassword, getEdtMainRegisterConfirmPW;
    private ProgressBar progressBarRegisterMain;
    private childDB defaultChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        btnMainRegisterMe = findViewById(R.id.btnSubmit);
        edtMainRegisterEmail = findViewById(R.id.edtRegisterEmail);
        edtMainRegisterPassword = findViewById(R.id.edtRegisterPassword);
        getEdtMainRegisterConfirmPW = findViewById(R.id.edtRegisterConfirmPW);
        progressBarRegisterMain = findViewById(R.id.progressBarRegister);
        progressBarRegisterMain.setVisibility(View.GONE);

        btnMainRegisterMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterUser();
            }
        });
    }

    private void RegisterUser() {
        String getEmail = edtMainRegisterEmail.getText().toString();
        String getPassword = edtMainRegisterPassword.getText().toString();
        String getConfirmPW = getEdtMainRegisterConfirmPW.getText().toString();

        if (getEmail.isEmpty()) {
            edtMainRegisterEmail.setError("Email is required!");
            edtMainRegisterEmail.requestFocus();
            return;
        }

        if (getPassword.isEmpty()) {
            edtMainRegisterPassword.setError("Password is required!");
            edtMainRegisterPassword.requestFocus();
            return;
        }

        if (getConfirmPW.isEmpty()) {
            getEdtMainRegisterConfirmPW.setError("Confirm password field cannot be empty.");
            getEdtMainRegisterConfirmPW.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(getEmail).matches()) {
            edtMainRegisterEmail.setError("Email ID not valid.");
            return;
        }

        if (getPassword.length() < 8 || getPassword.length() > 20) {
            edtMainRegisterPassword.setError("Password should be between 8-20 characters.");
            edtMainRegisterPassword.requestFocus();
            return;
        }

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[~!@#$%^&*()<>_+{}]).{8,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(getPassword);
        if (!matcher.matches()) {
            edtMainRegisterPassword.setError("Not a valid password.");
            edtMainRegisterPassword.requestFocus();
            edtMainRegisterPassword.getText().clear();
            getEdtMainRegisterConfirmPW.getText().clear();
            return;
        }

        if (!getPassword.equals(getConfirmPW)) {
            edtMainRegisterPassword.setError("Password and Confirm Password do not match. Please try again.");
            getEdtMainRegisterConfirmPW.setError("Password and Confirm Password do not match. PLease try again.");
            edtMainRegisterPassword.requestFocus();
            return;
        }

        //initialisng the default child and also adding it to the default list
        defaultChild = new childDB(-1, "108Name108108", "Default Place", 100, 0, defaultDOB);
        //clearing it to be on the safe side
        defaultChildList.clear();
        defaultChildList.add(defaultChild);

        progressBarRegisterMain.setVisibility(View.VISIBLE);
        btnMainRegisterMe.requestFocus();
        mAuth.createUserWithEmailAndPassword(getEmail, getPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBarRegisterMain.setVisibility(View.VISIBLE);
                            Toast.makeText(RegisterActivity.this, "Task failed unsuccessfully!", Toast.LENGTH_SHORT).show();
                            if (firebaseUser != null) {
                                firebaseUser.sendEmailVerification();
                            }
                            edtMainRegisterEmail.getText().clear();
                            edtMainRegisterPassword.getText().clear();
                            getEdtMainRegisterConfirmPW.getText().clear();
                            btnMainRegisterMe.requestFocus();
                            progressBarRegisterMain.setVisibility(View.GONE);

//                                adding the default child to userDB and then to FBRT-DB
                            userDB defaultUser = new userDB(defaultChildList);
                            mDatabase
                                    .child("Users")
                                    .child(mAuth.getCurrentUser().getUid())
                                    .setValue(defaultUser);
                            openLoginActivity();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Failed to register, try again twice.", Toast.LENGTH_LONG).show();
                            progressBarRegisterMain.setVisibility(View.GONE);
                        }
                    }
                });
    }

    public void openLoginActivity() {
        Intent intentRegisterSuccess = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intentRegisterSuccess);
        Toast.makeText(RegisterActivity.this, "Kindly verify your Email ID.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent onBackIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(onBackIntent);
    }
}