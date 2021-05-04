package com.example.vaccinationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;

public class SelectChildActivity extends AppCompatActivity {

    //creating ArrayList of String for storing the names of children
    ArrayList<String> userChildrenList = new ArrayList<>();
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private Button btnMainAddChild;
    private Button btnMainSubmit;
    private FirebaseAuth mAuth;
    private Spinner spinnerChildren;
    private Button btnSignOut;
    private userDB currentUser;
    private userDB userFetchedFromDB;
    private int spinnerItemNumber = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectchild);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        btnMainAddChild = findViewById(R.id.btnAddChild);
        btnMainSubmit = findViewById(R.id.btnSubmitChild);
        btnSignOut = findViewById(R.id.btnSignOut);
        spinnerChildren = findViewById(R.id.spinnerShowChildren);

        btnMainAddChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddChildActivity();
            }
        });

        //Warning!!! Check if the reference given is exactly the same as in MainActivity3
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                clearing the ArrayList to ensure nothing from past appears as Karma!
                userChildrenList.clear();
                userChildrenList.add("Select a child - ");

//                fetching the names through the following route, FB DB -> Local Object user's child list -> Adapter(Only Names)
                userFetchedFromDB = new userDB(snapshot.getValue(userDB.class).getUserChildren());
//                adding all the names to the Adapter
                for (int i = 0; i < userFetchedFromDB.getUserChildren().size(); i++) {
                    //adding the names from local object
                    if (!(userFetchedFromDB.getUserChildren().get(i).getChildID() < 0)) {
                        Log.d("ChildrenName", userFetchedFromDB.getUserChildren().get(i).getChildName());
                        userChildrenList.add(userFetchedFromDB.getUserChildren().get(i).getChildName());
                    }
                }
                spinnerFunction();
                notificationFunction();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SelectChildActivity.this, "Database Fetching failed!", Toast.LENGTH_SHORT).show();
            }
        });

        btnMainSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openVaccinesActivity(spinnerItemNumber);
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Toast.makeText(SelectChildActivity.this, "Logged out successfully. Please don't forget to vaccinate your child though!", Toast.LENGTH_LONG).show();
                Intent intentUserSignOut = new Intent(SelectChildActivity.this, LoginActivity.class);
                startActivity(intentUserSignOut);
            }
        });

    }

    private void notificationFunction() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(SelectChildActivity.this, "My Notification");
            builder.setContentTitle("Vaccination Reminder!");
            builder.setContentText("Vaccine Name for This child is due.");
            builder.setSmallIcon(R.mipmap.ic_launcher_custom);
            builder.setAutoCancel(true);
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(SelectChildActivity.this);
            managerCompat.notify(1, builder.build());
        }
    }

    private void spinnerFunction() {
        for (String defaultChildName : userChildrenList) {
            if (defaultChildName == "108Name108108") {
                userChildrenList.remove(defaultChildName);
            }
        }
        ArrayAdapter<String> childAdapter = new ArrayAdapter<String>(
                SelectChildActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                userChildrenList
        );
        childAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerChildren.setAdapter(childAdapter);
        spinnerChildren.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    Toast vaccinationReadyMsg = Toast.makeText(SelectChildActivity.this, userChildrenList.get(i) + " ready for vaccination!", Toast.LENGTH_LONG);
                    vaccinationReadyMsg.show();
                }
                spinnerItemNumber = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(SelectChildActivity.this, "Please select the child to be vaccinated!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void openAddChildActivity() {
        Intent intent = new Intent(this, AddChildActivity.class);
        startActivity(intent);
    }

    public void openVaccinesActivity(int selectedChild) {
        if (selectedChild > 0) {
            Intent intent = new Intent(this, VaccinesActivity.class);
            intent.putExtra("childID", selectedChild - 1);
            startActivity(intent);
        } else {
            Toast.makeText(SelectChildActivity.this, "Please select the child first.", Toast.LENGTH_LONG).show();
        }
    }
}