package com.example.filterapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filterapp.adapter.BtAdapterDouble;
import com.example.filterapp.classes.BtLongDoubleItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PurchaseHistory extends AppCompatActivity implements BtAdapterDouble.BtDoubleListener {
    RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<BtLongDoubleItem> btLongDoubleItemList = new LinkedList<>();
    RecyclerView.Adapter adapter;
    BtLongDoubleItem btLongDoubleItem;
    String year = "", customerID, nameChar;
    List<String> filterIDList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);

        recyclerView = findViewById(R.id.rv_purchaseHistory);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        year = getIntent().getStringExtra("year");
        customerID = getIntent().getStringExtra("customerID");

        nameChar = customerID.substring(0, 1);

        CollectionReference getDetails = db.collection("customerDetails").document("sorted")
                .collection(nameChar).document(customerID).collection("purchaseHistory").document(year).collection(year);
        getDetails.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot collectionReference = task.getResult();
                    if (collectionReference.isEmpty()) {
                        Toast.makeText(PurchaseHistory.this, "Error, please try again later", Toast.LENGTH_SHORT).show();
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            searchDetails(documentSnapshot.getId().trim());
                        }
                    }
                }
            }
        });

    }

    public void searchDetails(final String documentID) {

        DocumentReference filterDetails = db.collection("sales").document(year)
                .collection(documentID.substring(4, 7)).document(documentID);
        filterDetails.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                storeObject(documentSnapshot.getString("dayBrought"), documentSnapshot.getString("fModel"), documentID);
            }
        });
    }

    public void storeObject(String date, String filterModel, String id) {
        btLongDoubleItem = new BtLongDoubleItem(date, filterModel);
        btLongDoubleItemList.add(btLongDoubleItem);
        filterIDList.add(id);
        storeAdapter();

    }

    public void storeAdapter() {
        Collections.sort(btLongDoubleItemList);
        adapter = new BtAdapterDouble(btLongDoubleItemList, this);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void btDoubleListener(int position) {
    }

    public void back(View view) {
        super.onBackPressed();
    }
}
