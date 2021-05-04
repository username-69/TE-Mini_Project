package com.example.vaccinationtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VaccinatedActivity extends AppCompatActivity {

    private FirebaseUser mUser;
    private DatabaseReference mDatabase;
    private DatabaseReference doctorReference;

    private TextView doctorName;
    private TextView hospitalName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccinated);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid());
        doctorReference = FirebaseDatabase.getInstance().getReference().child("DoctorDB");

        doctorName = findViewById(R.id.textView14);
        hospitalName = findViewById(R.id.textView15);

        doctorName.setText("Dr. Kalyani");
        hospitalName.setText("ZZ Hospital");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent selectChildIntent = new Intent(VaccinatedActivity.this, SelectChildActivity.class);
        startActivity(selectChildIntent);
    }
}