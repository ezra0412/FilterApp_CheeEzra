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

public class Abcd extends AppCompatActivity implements BtAdapterSingle.BtSingleListener {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<String> dataList = new ArrayList<>();
    String chosenOption;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abcd);
        recyclerView = findViewById(R.id.rv_abc);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dataList.add("A");
        dataList.add("B");
        dataList.add("C");
        dataList.add("D");
        dataList.add("E");
        dataList.add("F");
        dataList.add("G");
        dataList.add("H");
        dataList.add("I");
        dataList.add("J");
        dataList.add("K");
        dataList.add("L");
        dataList.add("M");
        dataList.add("N");
        dataList.add("O");
        dataList.add("P");
        dataList.add("Q");
        dataList.add("R");
        dataList.add("S");
        dataList.add("T");
        dataList.add("U");
        dataList.add("V");
        dataList.add("W");
        dataList.add("X");
        dataList.add("Y");
        dataList.add("Z");

        adapter = new BtAdapterSingle(dataList, this);

        recyclerView.setAdapter(adapter);
    }

    public void back(View view) {
        super.onBackPressed();
    }

    @Override
    public void btSingleListener(int position) {
        convertInt(position);
        Intent intent = new Intent(Abcd.this, CustomerList.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("chosenOption", chosenOption);
        startActivity(intent);

    }

    public void convertInt(int position) {
        switch (position) {
            case 0:
                chosenOption = "a";
                break;

            case 1:
                chosenOption = "b";
                break;

            case 2:
                chosenOption = "c";
                break;

            case 3:
                chosenOption = "d";
                break;

            case 4:
                chosenOption = "e";
                break;

            case 5:
                chosenOption = "f";
                break;

            case 6:
                chosenOption = "g";
                break;

            case 7:
                chosenOption = "h";
                break;

            case 8:
                chosenOption = "i";
                break;

            case 9:
                chosenOption = "j";
                break;

            case 10:
                chosenOption = "k";
                break;

            case 11:
                chosenOption = "l";
                break;

            case 12:
                chosenOption = "m";
                break;

            case 13:
                chosenOption = "n";
                break;

            case 14:
                chosenOption = "o";
                break;

            case 15:
                chosenOption = "p";
                break;

            case 16:
                chosenOption = "q";
                break;

            case 17:
                chosenOption = "r";
                break;

            case 18:
                chosenOption = "s";
                break;

            case 19:
                chosenOption = "t";
                break;

            case 20:
                chosenOption = "u";
                break;

            case 21:
                chosenOption = "v";
                break;

            case 22:
                chosenOption = "w";
                break;

            case 23:
                chosenOption = "x";
                break;

            case 24:
                chosenOption = "y";
                break;

            case 25:
                chosenOption = "z";
                break;
        }
    }
}
