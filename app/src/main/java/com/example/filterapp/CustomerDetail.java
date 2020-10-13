package com.example.filterapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.filterapp.classes.Address;
import com.example.filterapp.classes.CustomerDetails;
import com.example.filterapp.classes.JavaMailAPI;
import com.example.filterapp.fcm.AppNotification;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.filterapp.MainActivity.getPositionCode;
import static com.example.filterapp.MainActivity.getStaffDetailsStatic;
import static com.example.filterapp.MainActivity.isVerfiedAdmin;

public class CustomerDetail extends AppCompatActivity {
    TextView mName, mEmail, mMobile, mAddress, mNote,mStatus;
    Button delete;
    String customerID;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Dialog adminDialog;
    boolean showPasswordPU = false;
    Boolean verified = false;
    Dialog loadingDialog;
    CustomerDetails customerDetails = new CustomerDetails();
    Address defAdd = new Address();
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);
        mName = findViewById(R.id.tv_name_customerDetail);
        mEmail = findViewById(R.id.tv_email_customerDetail);
        mMobile = findViewById(R.id.tv_mobile_customerDetail);
        mAddress = findViewById(R.id.tv_address_customerDetail);
        mNote = findViewById(R.id.tv_note_customerDetail);
        mStatus = findViewById(R.id.tv_status_customerDetails);
        delete = findViewById(R.id.bt_delete_customerDetail);
        adminDialog = new Dialog(this);
        loadingDialog = new Dialog(this);
        requestQueue = Volley.newRequestQueue(this);

        customerDetails = getIntent().getParcelableExtra("customerDetails");
        customerID = customerDetails.getfName().substring(0, 1).toLowerCase() + customerDetails.getMobile();

        mName.setText(customerDetails.fullName());
        mEmail.setText(customerDetails.getEmail());
        mMobile.setText(customerDetails.getMobile());
        mNote.setText(customerDetails.getNote());

        if (customerDetails.isDeleted()) {
            delete.setText("Restore");
            mStatus.setTextColor(getColor(R.color.red));
            mStatus.setText("TERMINATED");

        } else {
            delete.setText("Delete");
            mStatus.setTextColor(getColor(R.color.dark_green));
            mStatus.setText("ACTIVE");
        }

        if (getPositionCode() == 1)
            delete.setVisibility(View.VISIBLE);
        else
            delete.setVisibility(View.GONE);

        DocumentReference userAddress = db.collection("customerDetails").document("sorted")
                .collection(customerID.substring(0, 1)).document(customerID)
                .collection("address").document("address");
        userAddress.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Address address = documentSnapshot.toObject(Address.class);
                mAddress.setText(address.formatedAddress());
                setAddress(address);
            }
        });
    }

    private void setAddress(Address address) {
        defAdd = address;
    }


    public void locate(View view) {
        Intent GpsPage = new Intent(CustomerDetail.this, GPS.class);
        GpsPage.putExtra("lan", defAdd.getLan());
        GpsPage.putExtra("lon", defAdd.getLon());
        GpsPage.putExtra("name", customerDetails.fullName());
        GpsPage.putExtra("mobile", customerDetails.getMobile());
        GpsPage.putExtra("address", defAdd.formatedAddress());
        GpsPage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(GpsPage);
    }

    public void back(View view) {
        super.onBackPressed();
    }

    public void edit(View view) {
        Intent intent = new Intent(CustomerDetail.this, AddCustomer.class);
        intent.putExtra("customerID", customerID);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void delete(View view) {
        if (customerDetails.isDeleted()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final AlertDialog dialog = builder.setMessage("Are you sure you wanna restore this customer")
                    .setTitle(Html.fromHtml("<font color='#1D8A43'>RESTORATION CONFIRMATION</font>"))
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            DocumentReference restoreUser = db.collection("customerDetails").document("sorted")
                                    .collection(customerID.substring(0, 1)).document(customerID);
                            restoreUser.update("deleted", false).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    String title = "Customer Restored";
                                    String body = customerDetails.fullName() + " is being restored by " + getStaffDetailsStatic().fullName() + ".";
                                    AppNotification sendAppNotification = new AppNotification();
                                    requestQueue.add(sendAppNotification.sendNotification("customerDeleted", title, body));
                                }
                            });
                            delete.setText("Delete");
                            mStatus.setText("ACTIVE");
                            mStatus.setTextColor(getColor(R.color.dark_green));
                            Toast.makeText(CustomerDetail.this, "Customer Restored", Toast.LENGTH_SHORT).show();
                            customerDetails.setDeleted(false);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(CustomerDetail.this, "Customer Restore Canceled", Toast.LENGTH_SHORT).show();
                        }
                    })

                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            Toast.makeText(CustomerDetail.this, "Customer Restore Canceled", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .create();


            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {

                    dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.light_green));
                    dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.cancel_grey));

                    dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
                    dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);

                    dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextSize(17);
                    dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setTextSize(17);

                }
            });

            dialog.show();
        } else {
            ProgressBar progressBar;
            Sprite style = new Wave();
            loadingDialog.setContentView(R.layout.pop_up_loading_screen);
            loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressBar = loadingDialog.findViewById(R.id.sk_loadingPU);
            progressBar.setIndeterminateDrawable(style);
            loadingDialog.setCanceledOnTouchOutside(false);

            if (isVerfiedAdmin()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final AlertDialog dialog = builder.setMessage("Are you sure you wan to terminate this customer?")
                        .setTitle(Html.fromHtml("<font color='#ff0f0f'>DELETE CONFIRMATION</font>"))
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                DocumentReference userDetails = db.collection("customerDetails").document("sorted")
                                        .collection(customerID.substring(0, 1)).document(customerID);
                                userDetails.update("deleted",true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        loadingDialog.dismiss();

                                        CollectionReference adminEmails = db.collection("adminDetails").document("adminList").collection("notificationPreference");
                                        adminEmails.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                    if (documentSnapshot.getBoolean("positionChangeEmail")) {
                                                        deleteCustomerEmail(documentSnapshot.getString("email"));
                                                    }
                                                }
                                            }
                                        });

                                        String title = "Customer Deleted";
                                        String body = customerDetails.fullName() + " is being deleted by " + getStaffDetailsStatic().fullName() + ".";
                                        AppNotification sendAppNotification = new AppNotification();
                                        requestQueue.add(sendAppNotification.sendNotification("customerDeleted", title, body));


                                        delete.setText("Restore");
                                        mStatus.setText("TERMINATED");

                                        mStatus.setTextColor(getColor(R.color.red));
                                        customerDetails.setDeleted(true);
                                        Toast.makeText(CustomerDetail.this, "Customer details deleted", Toast.LENGTH_LONG).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        loadingDialog.dismiss();
                                        Toast.makeText(CustomerDetail.this, "Fail to delete customer details", Toast.LENGTH_LONG).show();
                                    }
                                });


                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(CustomerDetail.this, "Canceled", Toast.LENGTH_LONG).show();

                            }
                        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                Toast.makeText(CustomerDetail.this, "Canceled", Toast.LENGTH_LONG).show();

                            }
                        }).create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.red));
                        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.cancel_grey));

                        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
                        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);

                        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextSize(17);
                        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setTextSize(17);

                    }
                });
                dialog.show();

            } else {

                final ImageView close, showPassword;
                Button done;
                final EditText etPassword;
                adminDialog.setContentView(R.layout.pop_up_admin_password_check);
                adminDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                close = adminDialog.findViewById(R.id.img_close_adminPU);

                showPassword = adminDialog.findViewById(R.id.img_showPassword_adminPU);

                etPassword = adminDialog.findViewById(R.id.et_enterCode_adminPU);

                done = adminDialog.findViewById(R.id.bt_done_adminPU);

                adminDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (!verified)
                            Toast.makeText(CustomerDetail.this, "Admin verification failed", Toast.LENGTH_SHORT).show();

                    }
                });

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adminDialog.dismiss();
                    }
                });

                showPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!showPasswordPU) {
                            showPassword.setImageResource(R.drawable.hide_password_icon);
                            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            etPassword.setSelection(etPassword.getText().length());
                            showPasswordPU = true;
                        } else {
                            showPassword.setImageResource(R.drawable.show_password_icon);
                            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            etPassword.setSelection(etPassword.getText().length());
                            showPasswordPU = false;
                        }
                    }
                });

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String password = etPassword.getText().toString().trim();

                        if (password.isEmpty()) {
                            etPassword.setError("Cannot leave empty");
                            etPassword.requestFocus();
                            return;
                        }


                        DocumentReference passwordCheck = db.collection("adminDetails").document("loginDetails");
                        passwordCheck.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (password.equals(documentSnapshot.getString("verificationPassword"))) {
                                    verified = true;

                                    Toast.makeText(CustomerDetail.this, "Admin verification successfully", Toast.LENGTH_SHORT).show();
                                    adminDialog.dismiss();

                                    AlertDialog.Builder builder = new AlertDialog.Builder(CustomerDetail.this);
                                    final AlertDialog dialog = builder.setMessage("Are you sure you wan to terminate this customer?")
                                            .setTitle(Html.fromHtml("<font color='#ff0f0f'>DELETE CONFIRMATION</font>"))
                                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    loadingDialog.show();

                                                    DocumentReference userDetails = db.collection("customerDetails").document("sorted")
                                                            .collection(customerID.substring(0, 1)).document(customerID);
                                                    userDetails.update("deleted",true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            loadingDialog.dismiss();

                                                            CollectionReference adminEmails = db.collection("adminDetails").document("adminList").collection("notificationPreference");
                                                            adminEmails.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                                        if (documentSnapshot.getBoolean("positionChangeEmail")) {
                                                                            deleteCustomerEmail(documentSnapshot.getString("email"));
                                                                        }
                                                                    }
                                                                }
                                                            });

                                                            String title = "Customer Deleted";
                                                            String body = customerDetails.fullName() + " is being deleted by " + getStaffDetailsStatic().fullName() + ".";
                                                            AppNotification sendAppNotification = new AppNotification();
                                                            requestQueue.add(sendAppNotification.sendNotification("customerDeleted", title, body));


                                                            delete.setText("Restore");
                                                            mStatus.setText("TERMINATED");
                                                            mStatus.setTextColor(getColor(R.color.red));
                                                            customerDetails.setDeleted(true);
                                                            Toast.makeText(CustomerDetail.this, "Customer details deleted", Toast.LENGTH_LONG).show();

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            loadingDialog.dismiss();
                                                            Toast.makeText(CustomerDetail.this, "Fail to delete customer details", Toast.LENGTH_LONG).show();
                                                        }
                                                    });


                                                }
                                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Toast.makeText(CustomerDetail.this, "Canceled", Toast.LENGTH_LONG).show();

                                                }
                                            }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                                                @Override
                                                public void onCancel(DialogInterface dialogInterface) {
                                                    Toast.makeText(CustomerDetail.this, "Canceled", Toast.LENGTH_LONG).show();

                                                }
                                            }).create();
                                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                        @Override
                                        public void onShow(DialogInterface dialogInterface) {
                                            dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.red));
                                            dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.cancel_grey));

                                            dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
                                            dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);

                                            dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextSize(17);
                                            dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setTextSize(17);

                                        }
                                    });

                                    dialog.show();

                                } else {
                                    adminDialog.dismiss();
                                }
                            }
                        });
                    }
                });
                adminDialog.show();
            }
        }


    }

    private void getAdminEmail() {
        CollectionReference adminEmails = db.collection("adminDetails").document("adminList").collection("emailNotificationPreference");
        adminEmails.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.getBoolean("customerDeleted")) {
                        deleteCustomerEmail(documentSnapshot.getString("email"));
                    }
                }
            }
        });
    }

    private void deleteCustomerEmail(String email) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss ");
        Date dt = new Date();
        String message;

        message = customerDetails.fullName() + " is being deleted by admin " + getStaffDetailsStatic().fullName() + " at " + formatter.format(dt) + ".\n\n"
                + "------Customer Details------\n"
                + "Name: " + customerDetails.fullName() + "\n"
                + "Mobile: " + customerDetails.getMobile() + "\n"
                + "Email: " + customerDetails.getEmail() + "\n"
                + "Address: \n"
                + defAdd.formatedAddress() + "\n"
                + "Note: \n"
                + customerDetails.getNote();

        JavaMailAPI javaMailAPI = new JavaMailAPI(this, email,
                "Customer Deleted", message);

        javaMailAPI.execute();

    }

    public void history(View view) {
        Intent intent = new Intent(CustomerDetail.this, YearList.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("customerID", customerID);
        intent.putExtra("fromActivity", "customerHistory");
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getPositionCode() == 1)
            delete.setVisibility(View.VISIBLE);
        else
            delete.setVisibility(View.GONE);
    }
}

