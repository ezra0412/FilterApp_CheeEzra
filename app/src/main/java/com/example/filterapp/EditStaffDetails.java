package com.example.filterapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.filterapp.classes.EmailNotification;
import com.example.filterapp.classes.JavaMailAPI;
import com.example.filterapp.classes.StaffDetails;
import com.example.filterapp.fcm.AppNotification;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.filterapp.MainActivity.staffDetailsStatic;

public class EditStaffDetails extends AppCompatActivity {
    TextView mFName, mLName, mEmail, mBranch, mPosition, verify;
    EditText mMobile;
    String staffID;
    boolean mobileChanged = false;
    boolean branchChanged = false;
    boolean positionChanged = false;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Dialog branchDialog, positionDialog, adminDialog;
    boolean verifiedAdmin = false;
    boolean showPasswordPU = false;
    RequestQueue requestQueue;
    StaffDetails staffDetailsThis = new StaffDetails();
    RequestQueue requestQueue2;
    RequestQueue requestQueue3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_staff_details);
        mFName = findViewById(R.id.tv_fName_editSD);
        mLName = findViewById(R.id.tv_lName_editSD);
        mEmail = findViewById(R.id.tv_email_editSD);
        mBranch = findViewById(R.id.tv_branch_editSD);
        mPosition = findViewById(R.id.tv_position_editSD);
        mMobile = findViewById(R.id.et_mobile_editSE);
        verify = findViewById(R.id.tv_verifiy_staffED);
        branchDialog = new Dialog(this);
        positionDialog = new Dialog(this);
        adminDialog = new Dialog(this);
        staffDetailsThis = getIntent().getParcelableExtra("staffDetails");
        requestQueue = Volley.newRequestQueue(this);
        requestQueue2 = Volley.newRequestQueue(this);
        requestQueue3 = Volley.newRequestQueue(this);


        final TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mobileChanged = true;
            }
        };

        staffID = getIntent().getStringExtra("staffID");

        mFName.setText("  " + staffDetailsThis.getfName());
        mLName.setText("  " + staffDetailsThis.getlName());
        mEmail.setText("  " + staffDetailsThis.getEmail());
        mBranch.setText("  Branch " + staffDetailsThis.getBranch());
        mPosition.setText("  " + staffDetailsThis.getPosition());
        mMobile.setText("  " + staffDetailsThis.getMobile());
        mMobile.addTextChangedListener(textWatcher);

    }


    public void branchPopUp(View view) {
        ImageView close;
        Button branchLG, branchPB, branchPT;
        //Change the back ground as open
        mBranch.setBackgroundResource(R.drawable.spinner_open);
        branchDialog.setContentView(R.layout.pop_up_branch);
        //Change the background to transparent
        branchDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        close = branchDialog.findViewById(R.id.img_close_branchPU);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBranch.setBackgroundResource(R.drawable.spinner_close);
                branchDialog.dismiss();
            }
        });

        branchLG = branchDialog.findViewById(R.id.bt_lg_branchPU);

        branchPB = branchDialog.findViewById(R.id.bt_pb_branchPU);

        branchPT = branchDialog.findViewById(R.id.bt_pt_branchPU);

        branchLG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                branchChanged = true;
                mBranch.setText("  Branch LG");
                branchDialog.dismiss();
            }
        });


        branchPB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                branchChanged = true;
                mBranch.setText("  Branch PB");
                branchDialog.dismiss();
            }
        });

        branchPT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                branchChanged = true;
                mBranch.setText("  Branch PT");
                branchDialog.dismiss();
            }
        });

        branchDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //Change the background back to close
                mBranch.setBackgroundResource(R.drawable.spinner_close);
            }
        });

        branchDialog.show();

    }

    /**
     * Pop up the position for the staff to chose
     *
     * @param view
     */
    public void positionPopUp(View view) {
        ImageView close;
        Button sales, technician, admin;
        mPosition.setBackgroundResource(R.drawable.spinner_open);
        positionDialog.setContentView(R.layout.pop_up_position);
        positionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        close = positionDialog.findViewById(R.id.img_close_positionPU);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPosition.setBackgroundResource(R.drawable.spinner_close);
                positionDialog.dismiss();
            }
        });

        sales = positionDialog.findViewById(R.id.bt_sales_positionPU);

        technician = positionDialog.findViewById(R.id.bt_technician_positionPU);

        admin = positionDialog.findViewById(R.id.bt_admin_positionPU);

        sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                positionChanged = true;
                mPosition.setText("  Sales");
                //Set the text (Not yet verified) to be invisible as only admin need to be verified first only can be signed up
                verify.setVisibility(View.INVISIBLE);
                positionDialog.dismiss();
            }
        });


        technician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                positionChanged = true;
                mPosition.setText("  Technician");
                verify.setVisibility(View.INVISIBLE);
                positionDialog.dismiss();
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                positionChanged = true;
                mPosition.setText("  Admin");
                //Set the text (Not yet verified) to visible as admin need to be verified first only can be signed up
                verify.setVisibility(View.VISIBLE);
                positionDialog.dismiss();

                //Call up the function which check if the admin is verified if the admin is not yet verified
                if (!verifiedAdmin)
                    adminCheck();
            }
        });

        positionDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                mPosition.setBackgroundResource(R.drawable.spinner_close);
            }
        });

        positionDialog.show();

    }

    public void adminCheck() {
        final ImageView close, showPassword;
        Button done;
        final EditText etPassword;
        adminDialog.setContentView(R.layout.pop_up_admin_password_check);
        adminDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        close = adminDialog.findViewById(R.id.img_close_adminPU);

        showPassword = adminDialog.findViewById(R.id.img_showPassword_adminPU);

        etPassword = adminDialog.findViewById(R.id.et_enterCode_adminPU);

        done = adminDialog.findViewById(R.id.bt_done_adminPU);


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
                            //Indicated that this admin is verified to sign up
                            verifiedAdmin = true;
                            //Change the text message
                            verify.setText("(Verified Successfully)");
                            //Change the text color
                            verify.setTextColor(getResources().getColor(R.color.green));
                            Toast.makeText(EditStaffDetails.this, "Admin verified successfully", Toast.LENGTH_SHORT).show();
                            adminDialog.dismiss();
                        } else {
                            verifiedAdmin = false;
                            //Change the text message
                            verify.setText("(Verification Failed)");
                            Toast.makeText(EditStaffDetails.this, "Admin verification failed", Toast.LENGTH_SHORT).show();
                            adminDialog.dismiss();
                        }
                    }
                });
            }
        });
        adminDialog.show();

        adminDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                closeKeyboard();
            }
        });
    }

    public void update(View view) {
        String mobile = mMobile.getText().toString().trim();
        String branch = mBranch.getText().toString().trim();
        final String position = mPosition.getText().toString().trim();


        Map<String, Object> updatedStaffDetails = new HashMap<>();
        if (branchChanged || positionChanged || mobileChanged) {

            if (mobile.isEmpty()) {
                mMobile.setError("Mobile cannot be empty");
                mMobile.requestFocus();
            }

            if (!Patterns.PHONE.matcher(mobile).matches()) {
                mMobile.setError("Invalid mobile format");
                mMobile.requestFocus();
            }

            if (branch.equalsIgnoreCase("Branch LG"))
                branch = "LG";

            else if (branch.equalsIgnoreCase("Branch PB"))
                branch = "PB";

            else
                branch = "PT";

            if (mobileChanged) {
                mobileChangeUser(staffDetailsThis.getEmail(), mMobile.getText().toString().trim());

                AppNotification appNotification = new AppNotification();
                requestQueue.add(appNotification.specifUser(staffDetailsThis.getToken(),
                        "Mobile Changed By Admin", staffDetailsThis.fullName()
                                + " changed your mobile to " + mobile));
                updatedStaffDetails.put("mobile", mobile);
                staffDetailsThis.setMobile(mobile);
            }

            Map<String, String> dummyData = new HashMap<>();
            dummyData.put("placeHolder", "dummyData");
            EmailNotification emailNotification = new EmailNotification();
            emailNotification.setEmail(staffDetailsThis.getEmail());
            AppNotification appNotificationDefault = new AppNotification();

            if (positionChanged) {

                positionChangeUser(staffDetailsThis.getEmail(), staffDetailsThis.getPosition());

                CollectionReference adminEmails = db.collection("adminDetails").document("adminList").collection("emailNotificationPreference");
                adminEmails.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.getBoolean("changePosition")) {
                                positionChange(documentSnapshot.getString("email"), staffDetailsThis.getPosition(), position);
                            }
                        }
                    }
                });

                String title = "Admin Changed Staff's Position";
                String body = staffDetailsStatic.fullName()
                        + " changed " + staffDetailsThis.fullName() + " position from " + staffDetailsThis.getPosition()
                        + " -> " + position;
                AppNotification sendAppNotification = new AppNotification();
                requestQueue2.add(sendAppNotification.sendNotification("changePosition", title, body,"3","","","",""));

                if (staffDetailsThis.getPosition().equalsIgnoreCase("admin")) {
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

                    if (position.equalsIgnoreCase("sales")) {
                        DocumentReference addSalesPosition = db.collection("staffDetails").document("sales")
                                .collection("sales").document(staffID);
                        addSalesPosition.set(dummyData);
                    } else {
                        DocumentReference addTechnicianPosition = db.collection("staffDetails").document("technician")
                                .collection("technician").document(staffID);
                        addTechnicianPosition.set(dummyData);
                    }
                } else if (staffDetailsThis.getPosition().equalsIgnoreCase("sales")) {
                    DocumentReference deletePosition = db.collection("staffDetails").document("sales")
                            .collection("sales").document(staffID);
                    deletePosition.delete();

                    if (position.equalsIgnoreCase("admin")) {
                        FirebaseMessaging.getInstance().subscribeToTopic("staffSignUp");
                        FirebaseMessaging.getInstance().subscribeToTopic("changeBranch");
                        FirebaseMessaging.getInstance().subscribeToTopic("changePosition");
                        FirebaseMessaging.getInstance().subscribeToTopic("customerDeleted");
                        FirebaseMessaging.getInstance().subscribeToTopic("staffStartNavigation");
                        FirebaseMessaging.getInstance().subscribeToTopic("soldNewFilter");
                        FirebaseMessaging.getInstance().subscribeToTopic("serviceDone");
                        FirebaseMessaging.getInstance().subscribeToTopic("companyEmailDetailsChanged");
                        FirebaseMessaging.getInstance().subscribeToTopic("adminVerificationPassChange");
                        FirebaseMessaging.getInstance().subscribeToTopic("identityVerificationPassChange");
                        FirebaseMessaging.getInstance().subscribeToTopic("staffDeleted");

                        DocumentReference upDatePosition = db.collection("adminDetails").document("adminList")
                                .collection("emailNotificationPreference").document(staffID);
                        upDatePosition.set(emailNotification);


                        DocumentReference upDatePositionApp = db.collection("adminDetails").document("adminList")
                                .collection("appNotificationPreference").document(staffID)
                                .collection("app").document("app");
                        upDatePositionApp.set(appNotificationDefault);

                    } else {
                        DocumentReference upDatePosition = db.collection("staffDetails").document("technician")
                                .collection("technician").document(staffID);
                        upDatePosition.set(dummyData);
                    }
                } else {
                    DocumentReference deletePosition = db.collection("staffDetails").document("technician")
                            .collection("technician").document(staffID);
                    deletePosition.delete();

                    if (position.equalsIgnoreCase("admin")) {
                        FirebaseMessaging.getInstance().subscribeToTopic("staffSignUp");
                        FirebaseMessaging.getInstance().subscribeToTopic("changeBranch");
                        FirebaseMessaging.getInstance().subscribeToTopic("changePosition");
                        FirebaseMessaging.getInstance().subscribeToTopic("customerDeleted");
                        FirebaseMessaging.getInstance().subscribeToTopic("staffStartNavigation");
                        FirebaseMessaging.getInstance().subscribeToTopic("soldNewFilter");
                        FirebaseMessaging.getInstance().subscribeToTopic("serviceDone");
                        FirebaseMessaging.getInstance().subscribeToTopic("companyEmailDetailsChanged");
                        FirebaseMessaging.getInstance().subscribeToTopic("adminVerificationPassChange");
                        FirebaseMessaging.getInstance().subscribeToTopic("identityVerificationPassChange");
                        FirebaseMessaging.getInstance().subscribeToTopic("staffDeleted");

                        DocumentReference upDatePosition = db.collection("adminDetails").document("adminList")
                                .collection("emailNotificationPreference").document(staffID);
                        upDatePosition.set(emailNotification);


                        DocumentReference upDatePositionApp = db.collection("adminDetails").document("adminList")
                                .collection("appNotificationPreference").document(staffID);
                        upDatePositionApp.set(appNotificationDefault);
                    } else {
                        DocumentReference upDatePosition = db.collection("staffDetails").document("sales")
                                .collection("sales").document(staffID);
                        upDatePosition.set(dummyData);
                    }
                }

                AppNotification appNotification = new AppNotification();
                requestQueue.add(appNotification.specifUser(staffDetailsThis.getToken(),
                        "Position Changed", staffDetailsStatic.fullName()
                                + " changed your position from " + staffDetailsThis.getPosition()
                                + " -> " + position));
                updatedStaffDetails.put("position", position);
                staffDetailsThis.setPosition(position);
            }

            if (branchChanged) {

                branchChangeUser(staffDetailsThis.getEmail(), staffDetailsThis.getBranch(), branch);

                final String finalBranch = branch;

                CollectionReference adminEmails = db.collection("adminDetails").document("adminList")
                        .collection("emailNotificationPreference");

                adminEmails.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.getBoolean("changeBranch")) {
                                branchChange(documentSnapshot.getString("email"), staffDetailsThis.getBranch(), finalBranch);
                            }
                        }
                    }
                });

                String title = "Admin Changed Staff's Branch";
                String body = staffDetailsStatic.fullName()
                        + " changed " + staffDetailsThis.fullName() + " branch from " + staffDetailsThis.getBranch()
                        + " -> " + branch;
                AppNotification sendAppNotification = new AppNotification();
                requestQueue3.add(sendAppNotification.sendNotification("changeBranch", title, body,"2",staffID,"","",""));


                AppNotification appNotification = new AppNotification();
                requestQueue.add(appNotification.specifUser(staffDetailsThis.getToken(),
                        "Branch Changed By Admin", staffDetailsThis.fullName()
                                + " changed your branch from " + staffDetailsThis.getBranch()
                                + " -> " + branch));
                updatedStaffDetails.put("branch", branch);
                staffDetailsThis.setBranch(branch);
            }

            DocumentReference update = db.collection("staffDetails").document(staffID);
            update.update(updatedStaffDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(EditStaffDetails.this, "Staff details updated successfully", Toast.LENGTH_SHORT).show();
                    mobileChanged = false;
                    branchChanged = false;
                    positionChanged = false;
                    Intent intent = new Intent(EditStaffDetails.this, StaffDetail.class);
                    intent.putExtra("staffDetails", staffDetailsThis);
                    intent.putExtra("staffID", staffID);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditStaffDetails.this, "Failed to update staff details", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(EditStaffDetails.this, "Nothing changed", Toast.LENGTH_SHORT).show();
        }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void back(View view) {
        if (branchChanged || positionChanged || mobileChanged) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final AlertDialog dialog = builder.setMessage("Are you sure you wanna go back? All changed won't be saved")
                    .setTitle(Html.fromHtml("<font color='#ff0f0f'>BACK CONFIRMATION</font>"))
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(EditStaffDetails.this, StaffDetail.class);
                            intent.putExtra("staffDetails", staffDetailsThis);
                            intent.putExtra("staffID", staffID);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
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
        } else {
            Intent intent = new Intent(EditStaffDetails.this, StaffDetail.class);
            intent.putExtra("staffDetails", staffDetailsThis);
            intent.putExtra("staffID", staffID);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    public void positionChange(String email, String oldPosition, String newPosition) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss ");
        Date dt = new Date();
        String message;

        message = staffDetailsStatic.fullName() + " changed " + staffDetailsThis.fullName() + " position at" + formatter.format(dt) + ".\n\n"
                + "\tFrom " + oldPosition + " -> " + newPosition + "\n\n"
                + "Staff position can be updated directly from the admin pages.";

        JavaMailAPI javaMailAPI = new JavaMailAPI(this, email,
                "Staff Position Changed by Admin", message);

        javaMailAPI.execute();
    }


    private void branchChange(String email, String original, String changed) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss ");
        Date dt = new Date();
        String message;

        message = staffDetailsStatic.fullName() + " changed " + staffDetailsThis.fullName() + " branch at" + formatter.format(dt) + ".\n\n"
                + "\tFrom " + original + " -> " + changed + "\n\n"
                + "Staff branch can be updated directly from the admin pages.";

        JavaMailAPI javaMailAPI = new JavaMailAPI(this, email,
                "Staff Branch Changed By Admin", message);

        javaMailAPI.execute();
    }

    public void positionChangeUser(String email, String oldPosition) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss ");
        Date dt = new Date();
        String message;

        message = "Dear " + staffDetailsThis.fullName() + ", \n\n"
                + "\tYour position is being changed by " + staffDetailsStatic.fullName() + " at " + formatter.format(dt) + ". "
                + "Your are now no longer " + oldPosition + " your position had been changed to " + mPosition.getText().toString().trim() + ". \n\n"
                + "From " + oldPosition + " -> " + mPosition.getText().toString().trim() + "\n"
                + "\tIf there's any issue feel free to ask the admin to change it for you, or you can ask for the security code inorder to change it your self.\n\n"
                + "Regards,\n"
                + "Ashita";

        JavaMailAPI javaMailAPI = new JavaMailAPI(this, email,
                "Position Changed By Admin", message);

        javaMailAPI.execute();
    }


    private void branchChangeUser(String email, String original, String changed) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss ");
        Date dt = new Date();
        String message;

        message = "Dear " + staffDetailsThis.fullName() + ", \n\n"
                + "\tYour position is being changed by " + staffDetailsStatic.fullName() + " at " + formatter.format(dt) + ". "
                + "Your are now no longer from " + original + " your branch had been changed to " + changed + ". \n\n"
                + "From " + original + " -> " + changed + "\n"
                + "\tIf there's any issue feel free to ask the admin to change it for you, or you can ask for the security code inorder to change it your self.\n\n"
                + "Regards,\n"
                + "Ashita";

        JavaMailAPI javaMailAPI = new JavaMailAPI(this, email,
                "Branch Changed By Admin", message);

        javaMailAPI.execute();
    }

    private void mobileChangeUser(String email, String mobile) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss ");
        Date dt = new Date();
        String message;

        message = "Dear " + staffDetailsThis.fullName() + ", \n\n"
                + "\tYour mobile is being changed by " + staffDetailsStatic.fullName() + " at " + formatter.format(dt) + ". "
                + "Your mobile number now is " + mobile + ". \n\n"
                + "\tIf it's an mistake feel free to change it your self from My Account which you can access from the side bar at main page.\n\n"
                + "Regards,\n"
                + "Ashita";

        JavaMailAPI javaMailAPI = new JavaMailAPI(this, email,
                "Mobile Changed By Admin", message);

        javaMailAPI.execute();
    }
}
