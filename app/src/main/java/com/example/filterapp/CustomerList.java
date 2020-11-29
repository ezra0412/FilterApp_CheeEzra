package com.example.filterapp;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filterapp.adapter.BtAdapterDouble;
import com.example.filterapp.classes.BtLongDoubleItem;
import com.example.filterapp.classes.CustomerDetails;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
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
    List<CustomerDetails> customerDetailsList = new LinkedList<>();
    RecyclerView.Adapter adapter;
    BtLongDoubleItem btLongDoubleItem;
    Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);
        chosenOption = getIntent().getStringExtra("chosenOption");
        tvChar = findViewById(R.id.tv_char_customerList);
        tvChar.setText(chosenOption.toUpperCase());
        errorMessage = findViewById(R.id.tv_error_message_customerList);
        recyclerView = findViewById(R.id.rv_customerList);

        loadingDialog = new Dialog(this);

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
                        }
                        storeAdapter();
                    }
                }
            }
        });


    }

    public void storeObject(CustomerDetails customerDetails) {
        btLongDoubleItem = new BtLongDoubleItem(customerDetails.fullName(), customerDetails.getMobile());
        btLongDoubleItemList.add(btLongDoubleItem);
        customerDetailsList.add(customerDetails);
    }

    public void storeAdapter() {
        Collections.sort(btLongDoubleItemList);
        Collections.sort(customerDetailsList);
        adapter = new BtAdapterDouble(btLongDoubleItemList, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void btDoubleListener(int position) {
        ProgressBar progressBar;
        final Sprite style = new Wave();
        loadingDialog.setContentView(R.layout.pop_up_loading_screen);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressBar = loadingDialog.findViewById(R.id.sk_loadingPU);
        progressBar.setIndeterminateDrawable(style);
        loadingDialog.show();

        loadingDialog.setCanceledOnTouchOutside(false);

        Intent intent = new Intent(CustomerList.this, CustomerDetail.class);
        intent.putExtra("customerDetails", customerDetailsList.get(position));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loadingDialog.show();
        loadingDialog.dismiss();
    }

    public void back(View view) {
        super.onBackPressed();
    }

}
