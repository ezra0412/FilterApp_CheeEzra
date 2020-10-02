package com.example.filterapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filterapp.classes.StaffDetails;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class StaffDetail extends AppCompatActivity {
    TextView name, email, phone, branch, position;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String staffID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_detail);
        name = findViewById(R.id.tv_name_customerDetail);
        email = findViewById(R.id.tv_email_customerDetail);
        phone = findViewById(R.id.tv_mobile_customerDetail);
        branch = findViewById(R.id.tv_branch_customerDetail);
        position = findViewById(R.id.tv_position_customerDetail);

        staffID = getIntent().getStringExtra("staffID");

        DocumentReference staffDetails = db.collection("staffDetails").document(staffID);
        staffDetails.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                StaffDetails staffDetails = value.toObject(StaffDetails.class);
                name.setText(staffDetails.fullName());
                email.setText(staffDetails.getEmail());
                phone.setText(staffDetails.getMobile());
                branch.setText(staffDetails.getBranch());
                position.setText(staffDetails.getPosition());

            }
        });

    }

    public void history(View view) {
        Toast.makeText(StaffDetail.this, "History", Toast.LENGTH_LONG).show();
    }

    public void update(View view) {
        Toast.makeText(StaffDetail.this, "update", Toast.LENGTH_LONG).show();
    }

    public void delete(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.setMessage("Are you sure you wanna delete this staff")
                .setTitle("Confirmation")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // CONFIRM

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // CANCEL
                    }
                }).setIcon(getDrawable(R.drawable.ic_warning))
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.red));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.light_grey));
            }
        });

        dialog.show();
    }

    public void back(View view) {
        super.onBackPressed();
        finish();
    }
}
