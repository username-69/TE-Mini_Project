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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private Button btnMainSignIn;
    private Button btnMainRegister;
    private EditText edtMainSignInEmail, edtMainSignInPassword;
    private TextView mainForgotPassword;
    private ProgressBar progressBarMainSignIn;
    private Button button;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null)
            if (currentUser.isEmailVerified())
                openSelectChildActivity();
            else {
                Toast.makeText(LoginActivity.this, "Failed to login. Email ID not verified!", Toast.LENGTH_SHORT).show();
                firebaseUser.sendEmailVerification();
            }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        button = findViewById(R.id.btnHelp);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHelpActivity();
            }
        });

        progressBarMainSignIn = findViewById(R.id.progressBarSignIn);
        progressBarMainSignIn.setVisibility(View.GONE);

        btnMainSignIn = findViewById(R.id.btnSignIn);
        btnMainSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInSuccessful();
            }
        });

        btnMainRegister = findViewById(R.id.btnRegister);
        btnMainRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();
            }
        });

        mainForgotPassword = findViewById(R.id.forgotPassword);
        mainForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPasswordMethod();
            }
        });
    }

    public void openHelpActivity() {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }

    private void logInSuccessful() {

        edtMainSignInEmail = findViewById(R.id.edtSignInEmail);
        edtMainSignInPassword = findViewById(R.id.edtSignInPassword);

        String signInEmail = edtMainSignInEmail.getText().toString();
        String signInPassword = edtMainSignInPassword.getText().toString();

        if (signInEmail.isEmpty()) {
            edtMainSignInEmail.setError("Email ID is required.");
            edtMainSignInEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(signInEmail).matches()) {
            edtMainSignInEmail.setError("Not a valid email ID.");
            edtMainSignInEmail.requestFocus();
            return;
        }

        if (signInPassword.isEmpty()) {
            edtMainSignInPassword.setError("Password is also required!");
            edtMainSignInPassword.requestFocus();
            return;
        }

        progressBarMainSignIn.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(signInEmail, signInPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    firebaseUser = mAuth.getCurrentUser();
                    if (firebaseUser.isEmailVerified()) {
                        Toast.makeText(LoginActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                        edtMainSignInEmail.getText().clear();
                        edtMainSignInPassword.getText().clear();
                        openSelectChildActivity();
                        progressBarMainSignIn.setVisibility(View.GONE);
                    } else {
                        firebaseUser.sendEmailVerification();
                        Toast.makeText(LoginActivity.this, "Email ID not verified.", Toast.LENGTH_LONG).show();
                        progressBarMainSignIn.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Failed to login! Please check your credentials.", Toast.LENGTH_SHORT).show();
                    edtMainSignInPassword.getText().clear();
                    progressBarMainSignIn.setVisibility(View.GONE);
                }
            }
        });
    }

    private void forgotPasswordMethod() {
        edtMainSignInEmail = findViewById(R.id.edtSignInEmail);
        edtMainSignInPassword = findViewById(R.id.edtSignInPassword);

        String signInEmail = edtMainSignInEmail.getText().toString();
        String signInPassword = edtMainSignInPassword.getText().toString();

        if (signInEmail.isEmpty()) {
            edtMainSignInEmail.setError("Email ID is required.");
            edtMainSignInEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(signInEmail).matches()) {
            edtMainSignInEmail.setError("Not a valid email ID.");
            edtMainSignInEmail.requestFocus();
            return;
        }

        if (!signInPassword.isEmpty()) {
            edtMainSignInPassword.setError("Password is not required!");
            edtMainSignInPassword.requestFocus();
            return;
        }
//        Following is how the code should have been, first check if user is verified. But nonetheless for the sake of working sending password reset link without that condition.
/*
        if (firebaseUser.isEmailVerified()) {
            mAuth.sendPasswordResetEmail(signInEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Please check your Email.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Please try again after sometime.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(LoginActivity.this, "Cannot send reset password link to an unregistered user.", Toast.LENGTH_LONG).show();
        }
*/
        mAuth.sendPasswordResetEmail(signInEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Please check your Email.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Please try again after sometime.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void openSelectChildActivity() {
        Intent intentSignInSuccess = new Intent(LoginActivity.this, SelectChildActivity.class);
        startActivity(intentSignInSuccess);
    }

    public void openRegisterActivity() {
        Intent intentRegisterActivity = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intentRegisterActivity);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}