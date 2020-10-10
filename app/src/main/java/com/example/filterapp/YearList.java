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
    String customerID;
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



        fromActivity = getIntent().getStringExtra("fromActivity");

        if (fromActivity.equalsIgnoreCase("filterList")) {
            CollectionReference year = db.collection("sales");
            year.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot collectionReference = task.getResult();
                        if (collectionReference.isEmpty()) {
                            errorMessage.setText("(No Filter History Found)");
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

        } else if (fromActivity.equalsIgnoreCase("staffHistory")) {
            CollectionReference year;
            staffID = getIntent().getStringExtra("staffID");
            chosenOption = getIntent().getStringExtra("chosenOption");

            if (chosenOption.equalsIgnoreCase("0"))
                year = db.collection("staffDetails").document(staffID)
                        .collection("sales");
            else
                year = db.collection("staffDetails").document(staffID)
                        .collection("service");

            year.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot collectionReference = task.getResult();
                        if (collectionReference.isEmpty()) {
                            errorMessage.setText("(No History Found)");
                            errorMessage.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                        } else {
                            errorMessage.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            for (QueryDocumentSnapshot documentSnapshots : task.getResult()) {
                                storeYear(documentSnapshots.getId());
                            }
                        }
                        storeAdapter();
                    }
                }
            });
        } else if (fromActivity.equalsIgnoreCase("customerHistory")) {
            customerID = getIntent().getStringExtra("customerID");
            String nameChar = customerID.substring(0, 1).toLowerCase();
            CollectionReference year = db.collection("customerDetails").document("sorted")
                    .collection(nameChar).document(customerID).collection("purchaseHistory");
            year.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot collectionReference = task.getResult();
                        if (collectionReference.isEmpty()) {
                            errorMessage.setText("(No Filter Brought Before)");
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

        } else if (fromActivity.equalsIgnoreCase("serviceHistory")) {
            filterID = getIntent().getStringExtra("filterID");
            String yearBrought = filterID.substring(0, 4);
            String monthBrought = filterID.substring(4, 7);
            CollectionReference year = db.collection("sales").document(yearBrought)
                    .collection(monthBrought).document(filterID).collection("serviceDetails");
            year.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot collectionReference = task.getResult();
                        if (collectionReference.isEmpty()) {
                            errorMessage.setText("(Filter Not Being Serviced Before)");
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
    }

    @Override
    public void btSingleListener(int position) {
        Intent intent;

        if (fromActivity.equalsIgnoreCase("serviceHistory")) {
            intent = new Intent(YearList.this, serviceHistoryList.class);
            intent.putExtra("filterID", filterID);
        }
        else if (fromActivity.equalsIgnoreCase("staffHistory")) {
            intent = new Intent(YearList.this, MonthList.class);
            intent.putExtra("staffID", staffID);
            intent.putExtra("chosenOption", chosenOption);
        } else if (fromActivity.equalsIgnoreCase("customerHistory")) {
            intent = new Intent(YearList.this, PurchaseHistory.class);
            intent.putExtra("customerID", customerID);
        } else
            intent = new Intent(YearList.this, MonthList.class);

        intent.putExtra("year", dataList.get(position));
        intent.putExtra("fromActivity", fromActivity);
        startActivity(intent);
    }
}
