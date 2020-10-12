package com.example.filterapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.filterapp.classes.JavaMailAPI;
import com.example.filterapp.classes.StaffDetails;
import com.example.filterapp.fcm.AppNotification;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.filterapp.MainActivity.staffDetailsStatic;

public class StaffDetail extends AppCompatActivity {
    TextView name, email, phone, branch, position;
    String staffID;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    StaffDetails staffDetailsThis = new StaffDetails();
    RequestQueue requestQueue, requestQueue2;
    Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_detail);
        name = findViewById(R.id.tv_name_customerDetail);
        email = findViewById(R.id.tv_email_customerDetail);
        phone = findViewById(R.id.tv_mobile_customerDetail);
        branch = findViewById(R.id.tv_branch_customerDetail);
        position = findViewById(R.id.tv_position_customerDetail);

        loadingDialog = new Dialog(this);

        staffID = getIntent().getStringExtra("staffID");
        staffDetailsThis = getIntent().getParcelableExtra("staffDetails");
        requestQueue = Volley.newRequestQueue(this);
        requestQueue2 = Volley.newRequestQueue(this);

        name.setText(staffDetailsThis.fullName());
        email.setText(staffDetailsThis.getEmail());
        phone.setText(staffDetailsThis.getMobile());
        branch.setText(staffDetailsThis.getBranch());
        position.setText(staffDetailsThis.getPosition());
        storeDetails(staffDetailsThis);

    }

    private void storeDetails(StaffDetails staffDetails) {
        staffDetailsThis = staffDetails;
    }

    public void history(View view) {
        Intent intent = new Intent(StaffDetail.this, HistoryType.class);
        intent.putExtra("staffID", staffID);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    public void update(View view) {
        Intent intent = new Intent(this, EditStaffDetails.class);
        intent.putExtra("staffDetails", staffDetailsThis);
        intent.putExtra("staffID", staffID);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    public void delete(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.setMessage("Are you sure you wanna delete this staff?")
                .setTitle(Html.fromHtml("<font color='#ff0f0f'>DELETE CONFIRMATION</font>"))
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ProgressBar progressBar;
                        final Sprite style = new Wave();
                        loadingDialog.setContentView(R.layout.pop_up_loading_screen);
                        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        progressBar = loadingDialog.findViewById(R.id.sk_loadingPU);
                        progressBar.setIndeterminateDrawable(style);

                        loadingDialog.setCanceledOnTouchOutside(false);

                        loadingDialog.show();

                        if (position.getText().toString().trim().equalsIgnoreCase("admin")) {
                            FirebaseMessaging.getInstance().unsubscribeFromTopic("staffSignUp");
                            FirebaseMessaging.getInstance().unsubscribeFromTopic("changeBranch");
                            FirebaseMessaging.getInstance().unsubscribeFromTopic("changePosition");
                            FirebaseMessaging.getInstance().unsubscribeFromTopic("customerDeleted");
                            FirebaseMessaging.getInstance().unsubscribeFromTopic("staffStartNavigation");
                            FirebaseMessaging.getInstance().unsubscribeFromTopic("soldNewFilter");
                            FirebaseMessaging.getInstance().unsubscribeFromTopic("serviceDone");
                            FirebaseMessaging.getInstance().unsubscribeFromTopic("companyEmailDetailsChanged");
                            FirebaseMessaging.getInstance().unsubscribeFromTopic("adminVerificationPassChange");
                            FirebaseMessaging.getInstance().unsubscribeFromTopic("identityVerificationPassChange");
                            FirebaseMessaging.getInstance().unsubscribeFromTopic("staffDeleted");


                            DocumentReference upDatePosition = db.collection("adminDetails").document("adminList")
                                    .collection("emailNotificationPreference").document(staffID);
                            upDatePosition.delete();


                            DocumentReference upDatePositionApp = db.collection("adminDetails").document("adminList")
                                    .collection("appNotificationPreference").document(staffID);
                            upDatePositionApp.delete();


                        } else if (position.getText().toString().trim().equalsIgnoreCase("sales")) {
                            DocumentReference deletePosition = db.collection("staffDetails").document("sales")
                                    .collection("sales").document(staffID);
                            deletePosition.delete();

                        } else {
                            DocumentReference deletePosition = db.collection("staffDetails").document("technician")
                                    .collection("technician").document(staffID);
                            deletePosition.delete();
                        }

                        CollectionReference deleteSalesHistory = db.collection("staffDetails").document("staffID").collection("sales");
                        deleteSalesHistory.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().size() > 0) {
                                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                            DocumentReference deleteSalesHistoryDocument = db.collection("staffDetails").document("staffID").collection("sales")
                                                    .document(queryDocumentSnapshot.getId());
                                            deleteSalesHistoryDocument.delete();
                                        }
                                    }
                                }
                            }
                        });

                        CollectionReference deleteServiceHistory = db.collection("staffDetails").document("staffID").collection("service");
                        deleteServiceHistory.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().size() > 0) {
                                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                            DocumentReference deleteServiceHistoryDocument = db.collection("staffDetails").document("staffID").collection("service")
                                                    .document(queryDocumentSnapshot.getId());
                                            deleteServiceHistoryDocument.delete();
                                        }
                                    }
                                }
                            }
                        });

                        Map<String, Object> changeStatusMap = new HashMap<>();
                        changeStatusMap.put("token", FieldValue.delete());
                        changeStatusMap.put("deleted", true);

                        DocumentReference changeStatus = db.collection("staffDetails").document(staffID);
                        changeStatus.update(changeStatusMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                AppNotification appNotification = new AppNotification();
                                requestQueue.add(appNotification.specifUser(staffDetailsThis.getToken(),
                                        "Account Terminated", "Your account is being terminated by " + staffDetailsStatic.fullName()
                                                + " you will no longer being able to use Ashita app and all of your account history will be erased."));

                                CollectionReference adminEmails = db.collection("adminDetails").document("adminList")
                                        .collection("emailNotificationPreference");

                                adminEmails.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            if (documentSnapshot.getBoolean("staffDeleted")) {
                                                staffDeleted(documentSnapshot.getString("email"));
                                            }
                                        }
                                    }
                                });

                                String title = "Admin Changed Staff's Branch";
                                String body = staffDetailsStatic.fullName()
                                        + " changed " + staffDetailsThis.fullName() + " branch from " + staffDetailsThis.getBranch()
                                        + " -> " + branch;
                                AppNotification sendAppNotification = new AppNotification();
                                requestQueue2.add(sendAppNotification.sendNotification("changeBranch", title, body));
                                loadingDialog.dismiss();
                                Toast.makeText(StaffDetail.this, "Staff Deleted", Toast.LENGTH_SHORT).show();
                                onBackPressed();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                loadingDialog.dismiss();
                                Toast.makeText(StaffDetail.this, "Error when deleting", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(StaffDetail.this, "Canceled", Toast.LENGTH_SHORT).show();
                    }
                })

                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Toast.makeText(StaffDetail.this, "Canceled", Toast.LENGTH_SHORT).show();
                    }
                })
                .create();


        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.red));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.cancel_grey));

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(17);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextSize(17);

            }
        });

        dialog.show();
    }

    private void staffDeleted(String email) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss ");
        Date dt = new Date();
        String message;

        message = staffDetailsStatic.fullName() + " terminated " + staffDetailsThis.fullName() + " account " + formatter.format(dt) + ".\n\n"
                + staffDetailsThis.fullName() + " will no longer to be able to use Ashita app.";

        JavaMailAPI javaMailAPI = new JavaMailAPI(this, email,
                "Staff Account Terminated", message);

        javaMailAPI.execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        staffID = getIntent().getStringExtra("staffID");
        staffDetailsThis = getIntent().getParcelableExtra("staffDetails");

    }

    public void back(View view) {
        super.onBackPressed();
    }
}
