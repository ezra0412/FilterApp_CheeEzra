package com.example.filterapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.filterapp.classes.CustomerDetails;
import com.example.filterapp.classes.FilterDetails;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class FilterDetail extends AppCompatActivity {
    String documentID, customerID;
    TextView name, invoiceNum, fModel, f1, f2, f3, f4, f5, f1LC, f2LC,
            f3LC, f4LC, f5LC, part1, part2, part3, part1LC, part2LC, part3LC, note;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ImageView filterPic;
    TextView errorMessage;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    public static Bitmap imageBit = null;
    FilterDetails filterDetails;
    public static boolean serviced;
    CustomerDetails customerDetails = new CustomerDetails();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_detail);
        documentID = getIntent().getStringExtra("documentID");

        filterDetails = getIntent().getParcelableExtra("filterDetails");


        name = findViewById(R.id.tv_name_filterDetails);
        invoiceNum = findViewById(R.id.tv_invNum_filterDetails);
        fModel = findViewById(R.id.tv_fModel_filterDetails);
        f1 = findViewById(R.id.tv_f1_filterDetails);
        f2 = findViewById(R.id.tv_f2_filterDetails);
        f3 = findViewById(R.id.tv_f3_filterDetails);
        f4 = findViewById(R.id.tv_f4_filterDetails);
        f5 = findViewById(R.id.tv_f5_filterDetails);
        f1LC = findViewById(R.id.tv_f1LC_filterDetails);
        f2LC = findViewById(R.id.tv_f2LC_filterDetails);
        f3LC = findViewById(R.id.tv_f3LC_filterDetails);
        f4LC = findViewById(R.id.tv_f4LC_filterDetails);
        f5LC = findViewById(R.id.tv_f5LC_filterDetails);
        part1 = findViewById(R.id.tv_part1_filterDetails);
        part2 = findViewById(R.id.tv_part2_filterDetails);
        part3 = findViewById(R.id.tv_part3_filterDetails);
        part1LC = findViewById(R.id.tv_part1LC_filterDetails);
        part2LC = findViewById(R.id.tv_part2LC_filterDetails);
        part3LC = findViewById(R.id.tv_part3LC_filterDetails);
        note = findViewById(R.id.tv_note_filterDetails);
        filterPic = findViewById(R.id.img_filterPic_filterDetails);
        errorMessage = findViewById(R.id.tv_errorMessage_filterDetails);
    }

    public void setFilterDetails() {
        if (!serviced) {
            if (filterDetails.getImageLocation().isEmpty()) {
                errorMessage.setVisibility(View.VISIBLE);
                invoiceNum.setText(filterDetails.getInvoiceNumber());
                fModel.setText(filterDetails.getfModel());
                f1.setText(filterDetails.getFilter1());
                f2.setText(filterDetails.getFilter2());
                f3.setText(filterDetails.getFilter3());
                f4.setText(filterDetails.getFilter4());
                f5.setText(filterDetails.getFilter5());
                f1LC.setText(filterDetails.getFilter1LC());
                f2LC.setText(filterDetails.getFilter2LC());
                f3LC.setText(filterDetails.getFilter3LC());
                f4LC.setText(filterDetails.getFilter4LC());
                f5LC.setText(filterDetails.getFilter5LC());
                part1.setText("x " + filterDetails.getWt());
                part2.setText("x " + filterDetails.getWt_s());
                part3.setText("x " + filterDetails.getFc_D());
                part1LC.setText(filterDetails.getWt_sLC());
                part2LC.setText(filterDetails.getWt_sLC());
                part3LC.setText(filterDetails.getFc_DLC());
                note.setText(filterDetails.getNote());
                setName(filterDetails.getfName(), filterDetails.getMobile());
            } else {

                StorageReference profileRef = storageReference.child("filterPictures/" + documentID + "/" + filterDetails.getImageLocation() + "/filterPictures.jpg");
                profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(filterPic);
                        errorMessage.setVisibility(View.INVISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        setImage(filterDetails.getImageLocation());
                        errorMessage.setText("Fetching Image...");
                        errorMessage.setTextColor(getColor(R.color.red));
                        errorMessage.setVisibility(View.VISIBLE);
                    }
                });

                invoiceNum.setText(filterDetails.getInvoiceNumber());
                fModel.setText(filterDetails.getfModel());
                f1.setText(filterDetails.getFilter1());
                f2.setText(filterDetails.getFilter2());
                f3.setText(filterDetails.getFilter3());
                f4.setText(filterDetails.getFilter4());
                f5.setText(filterDetails.getFilter5());
                f1LC.setText(filterDetails.getFilter1LC());
                f2LC.setText(filterDetails.getFilter2LC());
                f3LC.setText(filterDetails.getFilter3LC());
                f4LC.setText(filterDetails.getFilter4LC());
                f5LC.setText(filterDetails.getFilter5LC());
                part1.setText("x " + filterDetails.getWt());
                part2.setText("x " + filterDetails.getWt_s());
                part3.setText("x " + filterDetails.getFc_D());
                part1LC.setText(filterDetails.getWt_sLC());
                part2LC.setText(filterDetails.getWt_sLC());
                part3LC.setText(filterDetails.getFc_DLC());
                note.setText(filterDetails.getNote());
                setName(filterDetails.getfName(), filterDetails.getMobile());
            }
        } else {
            String year = documentID.substring(0, 4);
            String month = documentID.substring(4, 7);
            DocumentReference filterDetailsUpdate = db.collection("sales").document(year).collection(month).document(documentID);
            filterDetailsUpdate.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    filterPic.setImageBitmap(imageBit);
                    errorMessage.setVisibility(View.INVISIBLE);
                    imageBit = null;
                    serviced = false;
                    FilterDetails filterDetailsUpdated = documentSnapshot.toObject(FilterDetails.class);
                    invoiceNum.setText(filterDetailsUpdated.getInvoiceNumber());
                    fModel.setText(filterDetailsUpdated.getfModel());
                    f1.setText(filterDetailsUpdated.getFilter1());
                    f2.setText(filterDetailsUpdated.getFilter2());
                    f3.setText(filterDetailsUpdated.getFilter3());
                    f4.setText(filterDetailsUpdated.getFilter4());
                    f5.setText(filterDetailsUpdated.getFilter5());
                    f1LC.setText(filterDetailsUpdated.getFilter1LC());
                    f2LC.setText(filterDetailsUpdated.getFilter2LC());
                    f3LC.setText(filterDetailsUpdated.getFilter3LC());
                    f4LC.setText(filterDetailsUpdated.getFilter4LC());
                    f5LC.setText(filterDetailsUpdated.getFilter5LC());
                    part1.setText("x " + filterDetailsUpdated.getWt());
                    part2.setText("x " + filterDetailsUpdated.getWt_s());
                    part3.setText("x " + filterDetailsUpdated.getFc_D());
                    part1LC.setText(filterDetailsUpdated.getWt_sLC());
                    part2LC.setText(filterDetailsUpdated.getWt_sLC());
                    part3LC.setText(filterDetailsUpdated.getFc_DLC());
                    note.setText(filterDetailsUpdated.getNote());
                    setName(filterDetailsUpdated.getfName(), filterDetailsUpdated.getMobile());
                }
            });
        }

    }

    private void setImage(final String imageLocation) {
        StorageReference profileRef = storageReference.child("filterPictures/" + documentID + "/" + imageLocation + "/filterPictures.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(filterPic);
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


    public void setName(String fName, String mobile) {
        customerID = fName.substring(0, 1).toLowerCase() + mobile;
        final DocumentReference customerDetail = db.collection("customerDetails").document("sorted").
                collection(fName.substring(0, 1).toLowerCase()).document(customerID);
        customerDetail.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                customerDetails = documentSnapshot.toObject(CustomerDetails.class);
                name.setText(customerDetails.fullName());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                name.setText("Customer Deleted");
            }
        });
    }

    public void openCustomerDetails(View view) {
        Intent intent = new Intent(FilterDetail.this, CustomerDetail.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("customerDetails", customerDetails);
        startActivity(intent);
    }

    public void generateQRCode(View view) {
        Intent intent = new Intent(FilterDetail.this, QRcode.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("documentID", documentID);
        startActivity(intent);
    }

    public void serviceFilter(View view) {
        if (customerDetails.isDeleted()){
            Toast.makeText(FilterDetail.this,"Customer Terminated",Toast.LENGTH_SHORT).show();
        }else {
            Intent intent = new Intent(FilterDetail.this, ServiceFilter.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("documentID", documentID);
            intent.putExtra("customerDetails",customerDetails);
            intent.putExtra("filterDetails",filterDetails);
            startActivity(intent);
        }
    }

    public void history(View view) {
        Intent intent = new Intent(FilterDetail.this, YearList.class);
        intent.putExtra("filterID", documentID);
        intent.putExtra("fromActivity", "serviceHistory");
        startActivity(intent);
    }

    public void back(View view) {
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();

        setFilterDetails();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        serviced = false;
        imageBit = null;
    }
}
