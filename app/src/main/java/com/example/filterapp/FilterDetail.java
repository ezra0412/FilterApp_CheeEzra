package com.example.filterapp;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FilterDetail extends AppCompatActivity {
    String documentID;
    TextView name, invoiceNum, fModel, f1, f2, f3, f4, f5, f1LC, f2LC,
            f3LC, f4LC, f5LC, part1, part2, part3, part1LC, part2LC, part3LC, note;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_detail);
        documentID = getIntent().getStringExtra("documentID");
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
        setFilterDetails();
    }

    public void setFilterDetails() {
        String year = documentID.substring(0, 4);
        final String month = documentID.substring(4, 7);
        DocumentReference filterDetails = db.collection("sales").document(year).collection(month).document(documentID);
        filterDetails.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                FilterDetails filterDetails = documentSnapshot.toObject(FilterDetails.class);
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
                part1.setText(filterDetails.getWt());
                part2.setText(filterDetails.getWt_s());
                part3.setText(filterDetails.getFC_D());
                part1LC.setText(filterDetails.getWt_sLC());
                part2LC.setText(filterDetails.getWt_sLC());
                part3LC.setText(filterDetails.getFC_DLC());
                note.setText(filterDetails.getNote());
                setName(filterDetails.getfName(), filterDetails.getMobile());
            }
        });
    }

    public void setName(String fName, String mobile) {
        String documentID2 = fName.substring(0, 1).toLowerCase() + mobile;

        DocumentReference customerDetails = db.collection("customerDetails").document("sorted").
                collection(fName.substring(0, 1).toLowerCase()).document(documentID2);
        customerDetails.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                name.setText(documentSnapshot.getString("lName") + " " + documentSnapshot.getString("fName"));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                name.setText("Customer Deleted");
            }
        });
    }
}
