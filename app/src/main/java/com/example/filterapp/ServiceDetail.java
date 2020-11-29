package com.example.filterapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.filterapp.classes.CustomerDetails;
import com.example.filterapp.classes.FilterDetails;
import com.example.filterapp.classes.ServiceDetails;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ServiceDetail extends AppCompatActivity {
    ImageView filterImage;
    TextView mInvNum, mName, mDate, mTime, mF1, mF2, mF3, mF4, mF5, mWt, mWts, mFcd, mNote, errorMessage;
    String filterID;
    ServiceDetails serviceDetails;
    FilterDetails filterDetails;
    CustomerDetails customerDetails;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);
        filterImage = findViewById(R.id.img_filterImage_serviceDetails);
        mInvNum = findViewById(R.id.tv_invNum_serviceDetails);
        mName = findViewById(R.id.tv_name_serviceDetails);
        mDate = findViewById(R.id.tv_date_serviceDetails);
        mTime = findViewById(R.id.tv_time_serviceDetails);
        mF1 = findViewById(R.id.tv_f1_serviceDetails);
        mF2 = findViewById(R.id.tv_f2_serviceDetails);
        mF3 = findViewById(R.id.tv_f3_serviceDetails);
        mF4 = findViewById(R.id.tv_f4_serviceDetails);
        mF5 = findViewById(R.id.tv_f5_serviceDetails);
        mWt = findViewById(R.id.tv_wt_serviceDetails);
        mWts = findViewById(R.id.tv_wts_serviceDetails);
        mFcd = findViewById(R.id.tv_fcd_serviceDetails);
        errorMessage = findViewById(R.id.tv_error_serviceDetails);
        loadingDialog = new Dialog(this);

        mNote = findViewById(R.id.tv_note_serviceDetails);

        serviceDetails = getIntent().getParcelableExtra("serviceDetails");
        filterID = getIntent().getStringExtra("filterID");
        setImage(serviceDetails.getServiceID());

        String yearBrought = filterID.substring(0, 4);
        String monthBrought = filterID.substring(4, 7);
        DocumentReference staffDetail = db.collection("sales").document(yearBrought).collection(monthBrought).document(filterID);
        staffDetail.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                FilterDetails tempo = documentSnapshot.toObject(FilterDetails.class);
                storeUserDetails(tempo.getfName().substring(0, 1).toLowerCase(), tempo.getMobile());
                storeFilterDetails(tempo);
                mInvNum.setText(tempo.getInvoiceNumber());
            }
        });

        mDate.setText(serviceDetails.getServiceDate());
        mTime.setText(serviceDetails.getServiceTime());
        mF1.setText(serviceDetails.getChangedFilter1());
        mF2.setText(serviceDetails.getChangedFilter2());
        mF3.setText(serviceDetails.getChangedFilter3());
        mF4.setText(serviceDetails.getChangedFilter4());
        mF5.setText(serviceDetails.getChangedFilter5());
        mWt.setText("x " + serviceDetails.getChangedWt());
        mWts.setText("x " + serviceDetails.getChangedWt_s());
        mFcd.setText("x " + serviceDetails.getChangedFC_D());

    }

    private void setImage(final String imageLocation) {
        StorageReference profileRef = storageReference.child("filterPictures/" + filterID + "/" + imageLocation + "/filterPictures.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(filterImage);
                errorMessage.setVisibility(View.INVISIBLE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                errorMessage.setText("Fetching Image...");
                errorMessage.setTextColor(getColor(R.color.red));
                errorMessage.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setImage(imageLocation);
                    }
                }, 6000);

            }
        });
    }

    private void storeUserDetails(String fName, String mobile) {
        DocumentReference customerDetails = db.collection("customerDetails").document("sorted").collection(fName).document(fName + mobile);
        customerDetails.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                CustomerDetails tempo = documentSnapshot.toObject(CustomerDetails.class);
                mName.setText(tempo.fullName());


                storeUserDetails2(tempo);
            }
        });
    }

    private void storeUserDetails2(CustomerDetails tempo) {
        customerDetails = tempo;
    }

    private void storeFilterDetails(FilterDetails filterDetails) {
        this.filterDetails = filterDetails;
    }

    public void openFilterDetails(View view) {
        ProgressBar progressBar;
        final Sprite style = new Wave();
        loadingDialog.setContentView(R.layout.pop_up_loading_screen);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressBar = loadingDialog.findViewById(R.id.sk_loadingPU);
        progressBar.setIndeterminateDrawable(style);

        loadingDialog.setCanceledOnTouchOutside(false);

        loadingDialog.show();
        Intent intent = new Intent(ServiceDetail.this, FilterDetail.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("filterDetails", filterDetails);
        intent.putExtra("documentID", filterID);
        startActivity(intent);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismiss();
            }
        }, 1000);
    }

    public void customerDetails(View view) {
        ProgressBar progressBar;
        final Sprite style = new Wave();
        loadingDialog.setContentView(R.layout.pop_up_loading_screen);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressBar = loadingDialog.findViewById(R.id.sk_loadingPU);
        progressBar.setIndeterminateDrawable(style);

        loadingDialog.setCanceledOnTouchOutside(false);

        loadingDialog.show();
        final Intent intent = new Intent(ServiceDetail.this, CustomerDetail.class);
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

    public void back(View view) {
        super.onBackPressed();
    }
}
