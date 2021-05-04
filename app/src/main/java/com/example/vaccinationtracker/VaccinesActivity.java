package com.example.vaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VaccinesActivity extends AppCompatActivity {

    private final userDB localUser = new userDB();
    private final childDB locaUserChild = new childDB();
    private DatabaseReference mDatabase;
    private FirebaseUser firebaseUser;
    private Button button;
    private TableLayout vaccineTableLayout;
    private List<VaccineData> localUserChildVaccine = new ArrayList<>();
    private int childIDFromSelectActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccines);

        Intent intentFromChildSelection = getIntent();
        childIDFromSelectActivity = intentFromChildSelection.getIntExtra("childID", -1);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());
        vaccineTableLayout = findViewById(R.id.layoutVaccineTable);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                localUser.setUserChildren(snapshot.getValue(userDB.class).getUserChildren());
                addDataToTable();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(VaccinesActivity.this, "Faulty child! Cannot Vaccinate.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addDataToTable() {
        localUserChildVaccine = localUser.getUserChildren().get(childIDFromSelectActivity).getChildVaccines();
        if (localUserChildVaccine.size() > 0) {
            for (int iterator = 0; iterator < localUserChildVaccine.size(); iterator++) {
                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(new TableLayout.LayoutParams(0, 0));
                tableRow.setBackground(getDrawable(R.drawable.border));

/*
                TextView tVVaccineName = new TextView(this);
                tVVaccineName.setText(localUserChildVaccine.get(iterator).getVaccineName() +"-"+ localUserChildVaccine.get(iterator).getVaccineDose());
                tVVaccineName.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT));
                tVVaccineName.setTextSize(14);
                tVVaccineName.setTextColor(Color.parseColor("#000000"));
                tVVaccineName.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                tVVaccineName.setPadding(5, 5, 5, 5);
*/

                Integer parsingInt = localUserChildVaccine.get(iterator).getVaccineWeek();
                TextView tVAge = new TextView(this);
                tVAge.setText(parsingInt.toString());
                tVAge.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT));
                tVAge.setTextSize(14);
                tVAge.setTextColor(Color.parseColor("#000000"));
                tVAge.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                tVAge.setPadding(10, 10, 10, 10);

                CheckBox vaccineStatus = new CheckBox(this);
                vaccineStatus.setId(108 + iterator);
                vaccineStatus.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT));
                vaccineStatus.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                vaccineStatus.setPadding(10, 10, 10, 10);
                vaccineStatus.setText(localUserChildVaccine.get(iterator).getVaccineName() + " - Dose " + localUserChildVaccine.get(iterator).getVaccineDose());
                if (localUserChildVaccine.get(iterator).isVaccincated()) {
                    vaccineStatus.setChecked(true);
                }
                vaccineStatus.setOnClickListener(getFinalVaccineStatus(vaccineStatus));

//            adding data to rows and then further adding row to column
//                tableRow.addView(tVVaccineName);
                tableRow.addView(vaccineStatus);
                tableRow.addView(tVAge);
                vaccineTableLayout.addView(tableRow);
            }
        } else
            Toast.makeText(VaccinesActivity.this, "No vaccination for faulty child!", Toast.LENGTH_LONG).show();
    }

    private View.OnClickListener getFinalVaccineStatus(CheckBox changedVaccineStatus) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (changedVaccineStatus.isChecked()) {
/*
                    if(!validateVaccineStatus())
                        changedVaccineStatus.setChecked(false);
*/
                    Intent intentQRValidation = new Intent(VaccinesActivity.this, QRValidationActivity.class);
                    int checkboxID = changedVaccineStatus.getId();
                    int[] content = new int[2];
                    content[0] = childIDFromSelectActivity;
                    content[1] = changedVaccineStatus.getId();
                    content[1] -= 108;
                    intentQRValidation.putExtra("contentsForQR", content);
                    startActivity(intentQRValidation);
                } else {
                    Toast.makeText(VaccinesActivity.this, "Selected vaccine is already taken!", Toast.LENGTH_LONG).show();
                    changedVaccineStatus.setChecked(true);
                }
            }
        };
    }

    private void openQRValidationActivity() {
        Intent intentQRValidation = new Intent(VaccinesActivity.this, QRValidationActivity.class);
        startActivity(intentQRValidation);
    }

    public void openSelectChildActivity() {
        Intent intent = new Intent(this, SelectChildActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        openSelectChildActivity();
    }
}