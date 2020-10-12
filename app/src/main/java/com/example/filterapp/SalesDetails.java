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

import androidx.appcompat.app.AppCompatActivity;

import com.example.filterapp.classes.CustomerDetails;
import com.example.filterapp.classes.FilterDetails;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SalesDetails extends AppCompatActivity {
    TextView mInvNum, mName, mDate, mTime, mFModel, mSoldPrice, mCommission;
    FilterDetails filterDetails;
    String commission, filterID;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CustomerDetails customerDetails;
    Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_details);
        mInvNum = findViewById(R.id.tv_invNum_salesDetails);
        mName = findViewById(R.id.tv_customerName_salesDetails);
        mDate = findViewById(R.id.tv_date_salesDetails);
        mTime = findViewById(R.id.tv_time_salesDetails);
        mFModel = findViewById(R.id.tv_fModel_salesDetails);
        mSoldPrice = findViewById(R.id.tv_soldPrice_salesDetails);
        mCommission = findViewById(R.id.tv_comission_salesDetails);

        loadingDialog = new Dialog(this);

        filterID = getIntent().getStringExtra("filterID");

        filterDetails = getIntent().getParcelableExtra("filterDetails");
        commission = getIntent().getStringExtra("commission");

        findCustomer(filterDetails.getfName(), filterDetails.getMobile());
        mInvNum.setText(filterDetails.getInvoiceNumber());
        mDate.setText(filterDetails.getDayBrought());
        mTime.setText(filterDetails.getTimeBrought());
        mFModel.setText(filterDetails.getfModel());
        mSoldPrice.setText("Rm " + filterDetails.getPrice());
        mCommission.setText("Rm " + commission);

    }

    private void findCustomer(String fName, String mobile) {
        DocumentReference customerDetails = db.collection("customerDetails").document("sorted").collection(fName.substring(0, 1).toLowerCase()).document(fName.substring(0, 1).toLowerCase() + mobile);
        customerDetails.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                CustomerDetails tempo = documentSnapshot.toObject(CustomerDetails.class);
                mName.setText(tempo.fullName());
                if (tempo.isDeleted())
                    mName.setTextColor(getColor(R.color.red));
                else
                    mName.setTextColor(getColor(R.color.medium_green));
                storeCustomerDetails(tempo);
            }
        });
    }

    private void storeCustomerDetails(CustomerDetails tempo) {
        customerDetails = tempo;
    }

    public void back(View view) {
        super.onBackPressed();
    }

    public void openCustomer(View view) {
        ProgressBar progressBar;
        final Sprite style = new Wave();
        loadingDialog.setContentView(R.layout.pop_up_loading_screen);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressBar = loadingDialog.findViewById(R.id.sk_loadingPU);
        progressBar.setIndeterminateDrawable(style);

        loadingDialog.setCanceledOnTouchOutside(false);

        loadingDialog.show();
        final Intent intent = new Intent(SalesDetails.this, CustomerDetail.class);
        intent.putExtra("customerDetails", customerDetails);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismiss();
            }
        }, 1000);
    }

    public void openFilterDetails(View view) {
        Intent intent = new Intent(SalesDetails.this, FilterDetail.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("filterDetails", filterDetails);
        intent.putExtra("documentID", filterID);
        startActivity(intent);
    }
}
