package com.example.vaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class VaccinatedActivity extends AppCompatActivity {

    private FirebaseUser mUser;
    private DatabaseReference mDatabase;
    private DatabaseReference doctorReference;

    private final userDB localUser = new userDB();
    private final List<DoctorDB> doctorList = new ArrayList<>();
    private childDB localUserChild = new childDB();
    private VaccineData localUserChildVaccine = new VaccineData();
    private TextView childName;
    private TextView childAge;
    private TextView vaccineName;
    private TextView vaccineDose;
    private TextView doctorName;
    private TextView hospitalName;

    private int[] requiredVaccineMetaDataFromQRValidation = new int[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccinated);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid());
        doctorReference = FirebaseDatabase.getInstance().getReference().child("DoctorDB");

        Intent intent = getIntent();
        requiredVaccineMetaDataFromQRValidation = intent.getIntArrayExtra("contentsForModifyingCloudData");

        childName = findViewById(R.id.textView);
        childAge = findViewById(R.id.textView12);
        vaccineName = findViewById(R.id.textView13);
        vaccineDose = findViewById(R.id.textView16);
        doctorName = findViewById(R.id.textView17);
        hospitalName = findViewById(R.id.textView18);

        if (requiredVaccineMetaDataFromQRValidation.length == 2) {

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    localUser.setUserChildren(snapshot.getValue(userDB.class).getUserChildren());
                    localUserChild = localUser.getUserChildren().get(requiredVaccineMetaDataFromQRValidation[0]);
                    localUserChildVaccine = localUserChild.getChildVaccines().get(requiredVaccineMetaDataFromQRValidation[1]);
                    sendDataToCloud();

                    childName.setText(localUserChild.getChildName());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        childAge.setText(String.valueOf(Math.toIntExact(
                                ChronoUnit.WEEKS.between(
                                        LocalDate.of(localUserChild.getChildDOB().getYear(), localUserChild.getChildDOB().getMonth(), localUserChild.getChildDOB().getDate()),
                                        LocalDate.now()))));
                        localUserChild.setChildAge(Math.toIntExact(
                                ChronoUnit.WEEKS.between(
                                        LocalDate.of(localUserChild.getChildDOB().getYear(), localUserChild.getChildDOB().getMonth(), localUserChild.getChildDOB().getDate()),
                                        LocalDate.now())));
                    }
                    vaccineName.setText(localUserChildVaccine.getVaccineName());
                    vaccineDose.setText(String.valueOf(localUserChildVaccine.getVaccineDose()));
                    doctorName.setText("Dr. Kalyani, M.B.B.S. M.D. Pediatrics");
                    hospitalName.setText("ZZ Hospital, City 1");
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(VaccinatedActivity.this, "Fault in the child. Cannot even retrive him/her data!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Fault in the child!", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendDataToCloud() {
        localUserChildVaccine.setVaccincated(true);
        localUserChild.getChildVaccines().set(requiredVaccineMetaDataFromQRValidation[1], localUserChildVaccine);
        localUser.getUserChildren().set(requiredVaccineMetaDataFromQRValidation[0], localUserChild);
        mDatabase.setValue(localUser);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent selectChildIntent = new Intent(VaccinatedActivity.this, SelectChildActivity.class);
        startActivity(selectChildIntent);
    }
}