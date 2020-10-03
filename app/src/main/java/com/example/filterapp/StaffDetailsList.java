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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class StaffDetailsList extends AppCompatActivity implements BtAdapterDouble.BtDoubleListener {
    String chosenOption;
    TextView errorMessage, topBar;
    RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<BtLongDoubleItem> btLongDoubleItemList = new LinkedList<>();
    RecyclerView.Adapter adapter;
    BtLongDoubleItem btLongDoubleItem;
    HashMap<String, String> staffIDs = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_details_list);

        chosenOption = getIntent().getStringExtra("chosenOption");

        errorMessage = findViewById(R.id.tv_errorMessage_staffList);
        recyclerView = findViewById(R.id.rv_staffList);

        topBar = findViewById(R.id.tv_topBar_customerList);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (chosenOption.equalsIgnoreCase("admin"))
            topBar.setText("Admin List");
        else if (chosenOption.equalsIgnoreCase("sales"))
            topBar.setText("Sales List");
        else {
            topBar.setText("Technician List");
        }

        if (chosenOption.equalsIgnoreCase("admin")) {
            CollectionReference getDetails = db.collection("adminDetails").document("adminList").collection("emailNotificationPreference");
            getDetails.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                searchDetails(documentSnapshot.getId().trim());
                            }
                        }
                    }
                }
            });
        } else {
            CollectionReference getDetails = db.collection("staffDetails").document(chosenOption).collection(chosenOption);
            getDetails.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                searchDetails(documentSnapshot.getId());

                            }

                        }
                    }
                }
            });

        }

    }

    public void searchDetails(final String documentID) {
        DocumentReference staffDetail = db.collection("staffDetails").document(documentID);
        staffDetail.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                storeObject(documentSnapshot.getString("fName"), documentSnapshot.getString("lName"), documentSnapshot.getString("mobile"), documentID);
            }
        });
    }

    public void storeObject(String fName, String lName, String mobile, String id) {
        btLongDoubleItem = new BtLongDoubleItem(fName + " " + lName, mobile);
        btLongDoubleItemList.add(btLongDoubleItem);
        staffIDs.put(fName.substring(0, 1) + mobile, id);
        storeAdapter();

    }

    public void storeAdapter() {
        Collections.sort(btLongDoubleItemList);
        adapter = new BtAdapterDouble(btLongDoubleItemList, this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void btDoubleListener(int position) {
        Intent intent = new Intent(this, StaffDetail.class);
        intent.putExtra("staffID", staffIDs.get(btLongDoubleItemList.get(position).getItem1().substring(0, 1) +
                btLongDoubleItemList.get(position).getItem2()));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void back(View view) {
        super.onBackPressed();
        finish();
    }
}
