package com.example.filterapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filterapp.adapter.BtAdapterDouble;
import com.example.filterapp.classes.BtLongDoubleItem;
import com.example.filterapp.classes.ServiceDetails;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
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
    RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<BtLongDoubleItem> btLongDoubleItemList = new LinkedList<>();
    RecyclerView.Adapter adapter;
    BtLongDoubleItem btLongDoubleItem;
    List<ServiceDetails> serviceDetailsList = new LinkedList<>();
    int collectionSize;
    Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_history_list);
        year = getIntent().getStringExtra("year");
        filterID = getIntent().getStringExtra("filterID");

        loadingDialog = new Dialog(this);

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
                            searchDetails(documentSnapshot.toObject(ServiceDetails.class), collectionReference.size());
                        }
                    }
                }
            }
        });

    }

    public void searchDetails(final ServiceDetails tempo, int size) {
        collectionSize = size;
        serviceDetailsList.add(tempo);
        DocumentReference staffDetails = db.collection("staffDetails").document(tempo.getTechnicianID());
        staffDetails.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                storeObject(tempo.getServiceDate(), documentSnapshot.getString("fName") + " " + documentSnapshot.getString("lName"));
            }
        });
    }

    public void storeObject(String date, String name) {
        btLongDoubleItem = new BtLongDoubleItem(date, name);
        btLongDoubleItemList.add(btLongDoubleItem);

        if (btLongDoubleItemList.size() == collectionSize) {
            Collections.reverse(btLongDoubleItemList);
            Collections.reverse(serviceDetailsList);
            storeAdapter();
        }
    }

    public void storeAdapter() {
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

        Intent intent = new Intent(serviceHistoryList.this, ServiceDetail.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("serviceDetails", serviceDetailsList.get(position));
        intent.putExtra("filterID", filterID);
        startActivity(intent);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismiss();
            }
        }, 200);
    }

    public void back(View view){
        super.onBackPressed();
    }
}
