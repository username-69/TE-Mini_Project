package com.example.vaccinationtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }

    @Override
    public void onBackPressed() {
        Intent returnHelp = new Intent(HelpActivity.this, LoginActivity.class);
        startActivity(returnHelp);
        super.onBackPressed();
    }
}