package com.example.filterapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filterapp.adapter.BtAdapterDouble;
import com.example.filterapp.classes.BtLongDoubleItem;
import com.example.filterapp.classes.FilterDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;

public class PurchaseHistory extends AppCompatActivity implements BtAdapterDouble.BtDoubleListener {
    RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<BtLongDoubleItem> btLongDoubleItemList = new LinkedList<>();
    RecyclerView.Adapter adapter;
    BtLongDoubleItem btLongDoubleItem;
    String year = "", customerID, nameChar;
    List<FilterDetails> filterDetailsList = new LinkedList<>();
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
                filterIDList.add(documentID);
                storeObject(documentSnapshot.toObject(FilterDetails.class));
            }
        });
    }

    public void storeObject(FilterDetails filterDetails) {
        btLongDoubleItem = new BtLongDoubleItem(filterDetails.getDayBrought(), filterDetails.getfModel());
        btLongDoubleItemList.add(btLongDoubleItem);
        filterDetailsList.add(filterDetails);
        storeAdapter();

    }

    public void storeAdapter() {
        adapter = new BtAdapterDouble(btLongDoubleItemList, this);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void btDoubleListener(int position) {
        Intent intent = new Intent(PurchaseHistory.this, FilterDetail.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("filterDetails", filterDetailsList.get(position));
        intent.putExtra("documentID", filterIDList.get(position));
        startActivity(intent);
    }

    public void back(View view) {
        super.onBackPressed();
    }
}
