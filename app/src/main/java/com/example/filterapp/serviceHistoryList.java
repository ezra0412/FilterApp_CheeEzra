package com.example.filterapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
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

public class serviceHistoryList extends AppCompatActivity implements BtAdapterDouble.BtDoubleListener {
    String year, filterID;
    TextView errorMessage;
    RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<BtLongDoubleItem> btLongDoubleItemList = new LinkedList<>();
    RecyclerView.Adapter adapter;
    BtLongDoubleItem btLongDoubleItem;
    List<String> filterIDList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_history_list);
        year = getIntent().getStringExtra("year");
        filterID = getIntent().getStringExtra("filterID");

        recyclerView = findViewById(R.id.rv_serviceHistory);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        CollectionReference getDetails = db.collection("sales").document(filterID.substring(0, 4))
                .collection(filterID.substring(4, 7)).document(filterID).collection("serviceDetails").document(year).collection(year);
        getDetails.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot collectionReference = task.getResult();
                    if (collectionReference.isEmpty()) {
                        Toast.makeText(serviceHistoryList.this, "Error, please try again later", Toast.LENGTH_SHORT).show();
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            searchDetails(documentSnapshot.getString("technicianID"), documentSnapshot.getString("serviceDate"), documentSnapshot.getId());
                        }
                    }
                }
            }
        });

    }

    public void searchDetails(final String staffID, final String date, final String id) {

        DocumentReference staffDetails = db.collection("staffDetails").document(staffID);
        staffDetails.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                storeObject(date, documentSnapshot.getString("fName") + " " + documentSnapshot.getString("lName"), id);
            }
        });
    }

    public void storeObject(String date, String name, String id) {
        btLongDoubleItem = new BtLongDoubleItem(date, name);
        btLongDoubleItemList.add(btLongDoubleItem);
        filterIDList.add(id);
        Collections.reverse(btLongDoubleItemList);
        storeAdapter();
    }

    public void storeAdapter() {
        adapter = new BtAdapterDouble(btLongDoubleItemList, this);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void btDoubleListener(int position) {

    }
}
