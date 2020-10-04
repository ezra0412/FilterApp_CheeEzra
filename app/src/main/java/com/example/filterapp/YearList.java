package com.example.filterapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filterapp.adapter.BtAdapterSingle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class YearList extends AppCompatActivity implements BtAdapterSingle.BtSingleListener {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    TextView errorMessage;
    List<String> dataList = new ArrayList<>();
    String staffID;
    String chosenOption;
    String filterID;
    String fromActivity;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year_list);

        recyclerView = findViewById(R.id.rv_year);
        errorMessage = findViewById(R.id.tv_errorMessage_year);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*
        if (getIntent().getStringExtra("staffID") != null)
            staffID = getIntent().getStringExtra("staffID");

        if (getIntent().getStringExtra("chosenOption") != null)
            chosenOption = getIntent().getStringExtra("chosenOption");

        if (getIntent().getStringExtra("filterID") != null)
            filterID = getIntent().getStringExtra("filterID");

         */
        fromActivity = getIntent().getStringExtra("fromActivity");

        if (fromActivity.equalsIgnoreCase("mainService")) {
            CollectionReference year = db.collection("sales");
            year.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot collectionReference = task.getResult();
                        if (collectionReference.isEmpty()) {
                            errorMessage.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                        } else {
                            errorMessage.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            for (QueryDocumentSnapshot documentSnapShots : task.getResult()) {
                                storeYear(documentSnapShots.getId());
                            }
                            storeAdapter();
                        }
                    }
                }

            });

        }


    }

    private void storeAdapter() {
        adapter = new BtAdapterSingle(dataList, this);
        recyclerView.setAdapter(adapter);
    }

    private void storeYear(String id) {
        dataList.add(id);
        Collections.reverse(dataList);
    }

    public void back(View view) {
        super.onBackPressed();
        finish();
    }

    @Override
    public void btSingleListener(int position) {
        startActivity(new Intent(this, MonthList.class));
    }
}
