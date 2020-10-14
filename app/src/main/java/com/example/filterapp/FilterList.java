package com.example.filterapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filterapp.adapter.BtAdapterDouble;
import com.example.filterapp.classes.BtLongDoubleItem;
import com.example.filterapp.classes.FilterDetails;
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

public class FilterList extends AppCompatActivity implements BtAdapterDouble.BtDoubleListener {
    RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<BtLongDoubleItem> btLongDoubleItemList = new LinkedList<>();
    RecyclerView.Adapter adapter;
    BtLongDoubleItem btLongDoubleItem;
    String year, month;
    TextView message;
    List<FilterDetails> filterDetailsList = new LinkedList<>();
    List<String> filterIDList = new LinkedList<>();
    Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_list);
        year = getIntent().getStringExtra("year");
        month = getIntent().getStringExtra("month");
        recyclerView = findViewById(R.id.rv_filterList);
        message = findViewById(R.id.tv_errorMessage_filterList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadingDialog = new Dialog(this);

        CollectionReference getFilters = db.collection("sales").document(year)
                .collection(month);
        getFilters.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot collectionReference = task.getResult();
                    if (collectionReference.isEmpty()) {
                        recyclerView.setVisibility(View.INVISIBLE);
                        message.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        message.setVisibility(View.INVISIBLE);
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            storeObject(documentSnapshot.toObject(FilterDetails.class), documentSnapshot.getId());
                        }
                        storeAdapter();
                    }
                }
            }
        });
    }

    private void storeObject(FilterDetails filterDetails, String id) {
        filterDetailsList.add(filterDetails);
        filterIDList.add(id);
        btLongDoubleItem = new BtLongDoubleItem(filterDetails.getDayBrought(), filterDetails.getInvoiceNumber());
        btLongDoubleItemList.add(btLongDoubleItem);
    }

    public void storeAdapter() {
        Collections.reverse(btLongDoubleItemList);
        Collections.reverse(filterDetailsList);
        Collections.reverse(filterIDList);
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

        Intent intent = new Intent(FilterList.this, FilterDetail.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("filterDetails", filterDetailsList.get(position));
        intent.putExtra("documentID", filterIDList.get(position));
        startActivity(intent);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismiss();
            }
        }, 200);
    }

    public void back(View view) {
        super.onBackPressed();
    }
}
