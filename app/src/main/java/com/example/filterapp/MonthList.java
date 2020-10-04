package com.example.filterapp;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filterapp.adapter.BtAdapterSingle;

import java.util.ArrayList;
import java.util.List;

public class MonthList extends AppCompatActivity implements BtAdapterSingle.BtSingleListener {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<String> dataList = new ArrayList<>();
    String chosenOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_list);

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
        convertInt(position);
    }

    public void convertInt(int position) {
        switch (position) {
            case 0:
                chosenOption = "Jan";
                break;

            case 1:
                chosenOption = "Feb";
                break;

            case 2:
                chosenOption = "Mar";
                break;

            case 3:
                chosenOption = "Apr";
                break;

            case 4:
                chosenOption = "May";
                break;

            case 5:
                chosenOption = "Jun";
                break;

            case 6:
                chosenOption = "Jul";
                break;

            case 7:
                chosenOption = "Aug";
                break;

            case 8:
                chosenOption = "Sep";
                break;

            case 9:
                chosenOption = "Oct";
                break;

            case 10:
                chosenOption = "Nov";
                break;

            case 11:
                chosenOption = "Dec";
                break;
        }

    }
}
