package com.example.filterapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.filterapp.adapter.BtAdapterSingle;

import java.util.ArrayList;
import java.util.List;

public class StaffPositionChoice extends AppCompatActivity implements BtAdapterSingle.BtSingleListener {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<String> dataList = new ArrayList<>();
    String chosenOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_position_choice);

        recyclerView = findViewById(R.id.rv_staffPosition);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dataList.add("Sales");
        dataList.add("Technician");
        dataList.add("Admin");

        adapter = new BtAdapterSingle(dataList, this);

        recyclerView.setAdapter(adapter);

    }

    public void back(View view) {
        super.onBackPressed();
    }

    @Override
    public void btSingleListener(int position) {
        switch (position) {
            case 0:
                chosenOption = "sales";
                break;

            case 1:
                chosenOption = "technician";
                break;

            case 2:
                chosenOption = "admin";
        }
        Intent intent = new Intent(StaffPositionChoice.this, StaffDetailsList.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("chosenOption", chosenOption);
        startActivity(intent);

    }

}
