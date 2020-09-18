package com.example.filterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class FilterDetail extends AppCompatActivity {
    String documentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_detail);
        documentID = getIntent().getStringExtra("documentID");


    }
}
