package com.example.filterapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filterapp.adapter.BtAdapterDouble;
import com.example.filterapp.classes.BtLongDoubleItem;
import com.example.filterapp.classes.FilterDetails;
import com.example.filterapp.classes.ServiceDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class HistoryList extends AppCompatActivity implements BtAdapterDouble.BtDoubleListener {
    String staffID;
    String chosenOption;
    TextView mTitle, mError;
    RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String year, month;
    List<BtLongDoubleItem> btLongDoubleItemList = new LinkedList<>();
    List<FilterDetails> filterDetailsList = new LinkedList<>();
    List<String> commissionList = new LinkedList<>();
    List<ServiceDetails> serviceDetailsList = new LinkedList<>();
    List<String> filterIDList = new LinkedList<>();
    RecyclerView.Adapter adapter;
    int collectionCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_list);

        staffID = getIntent().getStringExtra("staffID");
        chosenOption = getIntent().getStringExtra("chosenOption");
        mTitle = findViewById(R.id.tv_title_historyList);
        mError = findViewById(R.id.tv_error_historyList);
        recyclerView = findViewById(R.id.rv_historyList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        year = getIntent().getStringExtra("year");
        month = getIntent().getStringExtra("month");

        if (chosenOption.equalsIgnoreCase("0")) {
            mTitle.setText("Sales History");
            CollectionReference getSales = db.collection("staffDetails").document(staffID).collection("sales").document(year)
                    .collection(month);
            getSales.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot collectionReference = task.getResult();
                        if (collectionReference.isEmpty()) {
                            recyclerView.setVisibility(View.INVISIBLE);
                            mError.setVisibility(View.VISIBLE);
                        } else {
                            mError.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                searchDetails(documentSnapshot.getId().trim(), documentSnapshot.getString("commission"), "", collectionReference.size());
                            }
                        }
                    }
                }
            });


        } else {
            mTitle.setText("Service History");
            CollectionReference getSales = db.collection("staffDetails").document(staffID).collection("service").document(year)
                    .collection(month);
            getSales.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot collectionReference = task.getResult();
                        if (collectionReference.isEmpty()) {
                            recyclerView.setVisibility(View.INVISIBLE);
                            mError.setVisibility(View.VISIBLE);
                        } else {
                            mError.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                searchDetails(documentSnapshot.getId().trim(), documentSnapshot.getString("commission"), documentSnapshot.getString("filterID"), collectionReference.size());
                            }
                        }
                    }
                }
            });

        }


    }

    public void searchDetails(final String documentID, final String commission, final String filterID, int size) {
        collectionCounter = size;

        if (chosenOption.equalsIgnoreCase("0")) {
            String yearBrought = documentID.substring(0, 4);
            String monthBrought = documentID.substring(4, 7);
            DocumentReference staffDetail = db.collection("sales").document(yearBrought).collection(monthBrought).document(documentID);
            final ServiceDetails serviceDetails = new ServiceDetails();
            staffDetail.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                    storeObject(commission, documentSnapshot.toObject(FilterDetails.class), serviceDetails, documentID);
                }
            });
        } else {
            String yearBrought = filterID.substring(0, 4);
            String monthBrought = filterID.substring(4, 7);
            final FilterDetails filterDetails = new FilterDetails();
            DocumentReference staffDetail = db.collection("sales").document(yearBrought).collection(monthBrought).document(filterID)
                    .collection("serviceDetails").document(year).collection(year).document(documentID);
            staffDetail.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                    storeObject(commission, filterDetails, documentSnapshot.toObject(ServiceDetails.class), filterID);
                }
            });
        }
    }


    public void storeObject(String commission, FilterDetails filterDetails, ServiceDetails serviceDetails, String filterID) {
        BtLongDoubleItem btLongDoubleItem;

        if (chosenOption.equalsIgnoreCase("0")) {
            btLongDoubleItem = new BtLongDoubleItem(filterDetails.getDayBrought(), filterDetails.getInvoiceNumber());
            btLongDoubleItemList.add(btLongDoubleItem);
            filterDetailsList.add(filterDetails);
            commissionList.add(commission);
            filterIDList.add(filterID);

        } else {
            btLongDoubleItem = new BtLongDoubleItem(serviceDetails.getServiceDate(), "Commission: RM " + commission);
            btLongDoubleItemList.add(btLongDoubleItem);
            serviceDetailsList.add(serviceDetails);
            commissionList.add(commission);
            filterIDList.add(filterID);
        }

        if (btLongDoubleItemList.size() == collectionCounter) {
            Collections.reverse(btLongDoubleItemList);
            Collections.reverse(filterDetailsList);
            Collections.reverse(commissionList);
            Collections.reverse(serviceDetailsList);
            Collections.reverse(filterIDList);
            storeAdapter();
        }
    }

    public void storeAdapter() {

        adapter = new BtAdapterDouble(btLongDoubleItemList, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void btDoubleListener(int position) {

        if (chosenOption.equalsIgnoreCase("0")) {

            Intent intent = new Intent(HistoryList.this, SalesDetails.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("commission", commissionList.get(position));
            intent.putExtra("filterDetails", filterDetailsList.get(position));
            intent.putExtra("filterID", filterIDList.get(position));
            startActivity(intent);
        } else {
            Intent intent = new Intent(HistoryList.this, ServiceDetail.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("serviceDetails", serviceDetailsList.get(position));
            intent.putExtra("filterID", filterIDList.get(position));
            startActivity(intent);
        }
    }

    public void back(View view) {
        super.onBackPressed();
    }
}
