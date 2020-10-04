package com.example.filterapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryList extends AppCompatActivity {
    String staffID;
    int chosenOption;
    TextView mTitle;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_list);

        staffID = getIntent().getStringExtra("staffID");
        chosenOption = getIntent().getIntExtra("chosenOption", 0);
        mTitle = findViewById(R.id.tv_title_historyList);

        if (chosenOption == 0) {
            mTitle.setText("Sales History");

        } else {
            mTitle.setText("Service History");
        }
    }
}
