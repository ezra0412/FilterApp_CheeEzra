package com.example.filterapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CustomerList extends AppCompatActivity implements BtAdapterDouble.BtDoubleListener {
    String chosenOption;
    TextView tvChar, errorMessage;
    RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<BtLongDoubleItem> btLongDoubleItemList = new LinkedList<>();
    List<CustomerDetails> unsortedList = new LinkedList<>();
    RecyclerView.Adapter adapter;
    BtLongDoubleItem btLongDoubleItem;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);
        chosenOption = getIntent().getStringExtra("chosenOption");
        tvChar = findViewById(R.id.tv_char_customerList);
        tvChar.setText(chosenOption.toUpperCase());
        errorMessage = findViewById(R.id.tv_error_message_customerList);
        recyclerView = findViewById(R.id.rv_customerList);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        CollectionReference getDetails = db.collection("customerDetails").document("sorted").collection(chosenOption);
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
                            storeObject(documentSnapshot.toObject(CustomerDetails.class));
                            counter++;
                        }
                        storeAdapter();
                    }
                }
            }
        });


    }

    public void storeObject(CustomerDetails customerDetails) {
        unsortedList.add(customerDetails);
        btLongDoubleItem = new BtLongDoubleItem(unsortedList.get(counter).getfName() + " " + unsortedList.get(counter).getlName(), unsortedList.get(counter).getMobile());
        btLongDoubleItemList.add(btLongDoubleItem);
    }

    public void storeAdapter() {
        Collections.sort(btLongDoubleItemList);
        adapter = new BtAdapterDouble(btLongDoubleItemList, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void btDoubleListener(int position) {
        String customerID = btLongDoubleItemList.get(position).getItem1().substring(0, 1).toLowerCase() + btLongDoubleItemList.get(position).getItem2();
        Intent intent = new Intent(CustomerList.this, CustomerDetail.class);
        intent.putExtra("customerID", customerID);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void back(View view) {
        super.onBackPressed();
        finish();
    }
}
