package com.example.vaccinationtracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class QRValidationActivity extends AppCompatActivity {

    private final userDB localUser = new userDB();
    private final VaccineData modifiedVaccine = new VaccineData();
    private final String stringLong = "134988775697";
    private final long tempDoctorID = Long.parseLong(stringLong);
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;
    private DatabaseReference doctorReference;
    private Button scanButton;
    private Button backButton;
    private List<VaccineData> localUserChildVaccine = new ArrayList<>();
    private long qrDoctorID;
    private int[] requiredVaccineMetaData = new int[2];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrvalidation);

        scanButton = findViewById(R.id.scanButton);
        backButton = findViewById(R.id.returnButton);

        Intent intent = getIntent();
        requiredVaccineMetaData = intent.getIntArrayExtra("contentsForQR");

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid());
        doctorReference = FirebaseDatabase.getInstance().getReference().child("DoctorDB");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                localUser.setUserChildren(snapshot.getValue(userDB.class).getUserChildren());
                if (requiredVaccineMetaData.length == 2) {
                    localUserChildVaccine = localUser.getUserChildren().get(requiredVaccineMetaData[0]).getChildVaccines();
                } else {
                    Toast.makeText(QRValidationActivity.this, "Fetching the data from Select Activity Failed!", Toast.LENGTH_SHORT).show();
                }
                scanButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        scanQR();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QRValidationActivity.this, "Fault in your child!", Toast.LENGTH_SHORT).show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentGoBack = new Intent(QRValidationActivity.this, VaccinesActivity.class);
                startActivity(intentGoBack);
            }
        });

/*        if(requiredVaccineMetaData.length > 2){
            modifiedVaccine = localUserChildVaccine.get(requiredVaccineMetaData[1]);
            modifiedVaccine.setVaccincated(true);
            {
                localUserChildVaccine.set(requiredVaccineMetaData[1], modifiedVaccine);
                localUser.getUserChildren().get(requiredVaccineMetaData[0]).setChildVaccines(localUserChildVaccine);
                mDatabase.setValue(localUser);
            }        }else{
            Toast.makeText(QRValidationActivity.this, "Vaccination Failed!", Toast.LENGTH_LONG).show();
        }*/
    }

    private void scanQR() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Getting Doctor details...");
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() != null) {
                qrDoctorID = Long.parseLong(intentResult.getContents());
                if (qrDoctorID == tempDoctorID) {
                    System.out.println(qrDoctorID);
                    Intent vaccinatedIntent = new Intent(getBaseContext(), VaccinatedActivity.class);
                    startActivity(vaccinatedIntent);
                } else {
                    Toast.makeText(getBaseContext(), "Doctor cannot be validated. Your child was not vaccinated.", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getBaseContext(), "QR code is empty.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getBaseContext(), "QR code is invalid.", Toast.LENGTH_SHORT).show();
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}