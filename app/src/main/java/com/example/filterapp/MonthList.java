package com.example.filterapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filterapp.adapter.BtAdapterSingle;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MonthList extends AppCompatActivity implements BtAdapterSingle.BtSingleListener {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<String> dataList = new ArrayList<>();
    String chosenOptionThis;
    String fromActivity;
    String year;
    String staffID;
    String chosenOption;
    String customerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_list);

        fromActivity = getIntent().getStringExtra("fromActivity");
        year = getIntent().getStringExtra("year");

        if (fromActivity.equalsIgnoreCase("staffHistory")) {
            staffID = getIntent().getStringExtra("staffID");
            chosenOption = getIntent().getStringExtra("chosenOption");
        } else if (fromActivity.equalsIgnoreCase("customerHistory")) {
            customerID = getIntent().getStringExtra("customerID");
        }

        recyclerView = findViewById(R.id.rv_month);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dataList.add("Jan");
        dataList.add("Feb");
        dataList.add("Mar");
        dataList.add("Apr");
        dataList.add("May");
        dataList.add("Jun");
        dataList.add("Jul");
        dataList.add("Aug");
        dataList.add("Sep");
        dataList.add("Oct");
        dataList.add("Nov");
        dataList.add("Dec");

        adapter = new BtAdapterSingle(dataList, this);

        recyclerView.setAdapter(adapter);
    }

    public void back(View view) {
        super.onBackPressed();
    }

    @Override
    public void btSingleListener(int position) {
        LocalDateTime timeNow = LocalDateTime.now();
        int month = timeNow.getMonthValue();

        if (position > month - 1)
            Toast.makeText(MonthList.this, "Currectly is only " + convertInt(month - 1), Toast.LENGTH_SHORT).show();
        else {
            Intent intent;
            chosenOptionThis = convertInt(position);

            if (fromActivity.equalsIgnoreCase("filterList"))
                intent = new Intent(MonthList.this, FilterList.class);
            else if (fromActivity.equalsIgnoreCase("staffHistory")) {
                intent = new Intent(MonthList.this, PurchaseHistory.class);
            } else
                intent = new Intent();
            intent.putExtra("year", year);
            intent.putExtra("month", chosenOptionThis);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
        }
    }

    public String convertInt(int position) {
        switch (position) {
            case 0:
                return "Jan";

            case 1:
                return "Feb";

            case 2:
                return "Mar";

            case 3:
                return "Apr";

            case 4:
                return "May";

            case 5:
                return "Jun";

            case 6:
                return "Jul";

            case 7:
                return "Aug";

            case 8:
                return "Sep";

            case 9:
                return "Oct";

            case 10:
                return "Nov";

            case 11:
                return "Dec";
        }

        return null;
    }
}
