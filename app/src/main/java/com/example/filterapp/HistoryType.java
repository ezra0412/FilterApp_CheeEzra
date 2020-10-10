package com.example.filterapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filterapp.adapter.BtAdapterSingle;

import java.util.ArrayList;
import java.util.List;

public class HistoryType extends AppCompatActivity implements BtAdapterSingle.BtSingleListener {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<String> dataList = new ArrayList<>();
    String staffID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_type);

        staffID = getIntent().getStringExtra("staffID");
        recyclerView = findViewById(R.id.rv_historyType);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dataList.add("Sales");
        dataList.add("Service");
        adapter = new BtAdapterSingle(dataList, this);

        recyclerView.setAdapter(adapter);
    }

    public void back(View view) {
        super.onBackPressed();
    }

    @Override
    public void btSingleListener(int position) {

        Intent intent = new Intent(HistoryType.this, YearList.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("staffID", staffID);
        intent.putExtra("chosenOption", position + "");
        intent.putExtra("fromActivity", "staffHistory");
        startActivity(intent);

    }
}
