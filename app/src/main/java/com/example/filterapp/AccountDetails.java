package com.example.filterapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.example.filterapp.classes.EmailNotification;
import com.example.filterapp.classes.JavaMailAPI;
import com.example.filterapp.fcm.AppNotification;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.filterapp.MainActivity.getPositionCode;
import static com.example.filterapp.MainActivity.getStaffDetailsStatic;
import static com.example.filterapp.MainActivity.setPositionCode;
import static com.example.filterapp.MainActivity.staffDetailsStatic;

public class AccountDetails extends AppCompatActivity {
    boolean showPasswordPU = false;
    boolean verifiedAdmin = false;
    boolean showPasswordPPU = false;
    boolean showPassword2PPU = false;
    boolean showPasswordPIC = false;
    int selected = 0;

    String emailCU, passwordCU;
    String oldPosition;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    TextView mEmail, mBranch, mPosition, verify;
    EditText mFName, mLName, mMobile;
    ImageView pi;
    Dialog loadingDialog, branchDialog, positionDialog, adminDialog, changePasswordDialog, checkUserDialog;

    RequestQueue requestQueue1;
    RequestQueue requestQueue2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);
        pi = findViewById(R.id.img_pi_myAccount);
        loadingDialog = new Dialog(this);
        branchDialog = new Dialog(this);
        positionDialog = new Dialog(this);
        adminDialog = new Dialog(this);
        changePasswordDialog = new Dialog(this);
        checkUserDialog = new Dialog(this);
        mFName = findViewById(R.id.et_fName_accountDetails);
        mLName = findViewById(R.id.et_lName_accountDetails);
        mEmail = findViewById(R.id.tv_email_accountDetails);
        mMobile = findViewById(R.id.et_mobile_accountDetails);
        mBranch = findViewById(R.id.tv_branch_accountDetails);
        mPosition = findViewById(R.id.tv_position_accountDetails);
        verify = findViewById(R.id.tv_verify_accountDetails);

        requestQueue1 = Volley.newRequestQueue(this);
        requestQueue2 = Volley.newRequestQueue(this);


        mEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AccountDetails.this, "Email cannot be changed", Toast.LENGTH_SHORT).show();
            }
        });

        mBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected = 1;
                mBranch.setBackground(getDrawable(R.drawable.spinner_open));
                identityCheck();
            }
        });

        mPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected = 2;
                mPosition.setBackground(getDrawable(R.drawable.spinner_open));
                identityCheck();
            }
        });

        mFName.setText("  " + staffDetailsStatic.getfName());
        mLName.setText("  " + staffDetailsStatic.getlName());
        mEmail.setText("  " + mAuth.getCurrentUser().getEmail());
        mMobile.setText("  " + staffDetailsStatic.getMobile());
        if (staffDetailsStatic.getBranch().equalsIgnoreCase("LG")) {
            mBranch.setText("  Branch LG");
        } else if (staffDetailsStatic.getBranch().equalsIgnoreCase("PB")) {
            mBranch.setText("  Branch PB");
        } else {
            mBranch.setText("  Branch PT");
        }
        mPosition.setText("  " + staffDetailsStatic.getPosition());
        if (staffDetailsStatic.getPosition().equalsIgnoreCase("admin")) {
            verifiedAdmin = true;
        }

        if (staffDetailsStatic.isProfilePic()) {
            StorageReference profileRef = storageReference.child("staff/" + mAuth.getCurrentUser().getUid() + "/profileImage.jpg");
            profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(pi);
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, requestCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri image = data.getData();
                uploadImageToFirebase(image);
            }
        }
    }

    private void uploadImageToFirebase(Uri image) {
        ProgressBar progressBar;
        Sprite style = new Wave();
        loadingDialog.setContentView(R.layout.pop_up_loading_screen);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressBar = loadingDialog.findViewById(R.id.sk_loadingPU);
        progressBar.setIndeterminateDrawable(style);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();
        staffDetailsStatic.setProfilePic(true);
        DocumentReference userDetails = db.collection("staffDetails").document(mAuth.getCurrentUser().getUid());
        userDetails.update("profilePic",true);
        final StorageReference imageRefence = storageReference.child("staff/" + mAuth.getCurrentUser().getUid() + "/profileImage.jpg");
        imageRefence.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRefence.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(pi);
                        loadingDialog.dismiss();
                        staffDetailsStatic.setProfilePic(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingDialog.dismiss();
                        Toast.makeText(AccountDetails.this, "Failed to change profile picture", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public void changeImage(View view) {
        Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openGallery, 1000);
    }

    public void removePI(View view) {
        ProgressBar progressBar;
        Sprite style = new Wave();
        loadingDialog.setContentView(R.layout.pop_up_loading_screen);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressBar = loadingDialog.findViewById(R.id.sk_loadingPU);
        progressBar.setIndeterminateDrawable(style);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();
        StorageReference profileRef = storageReference.child("staff/" + mAuth.getCurrentUser().getUid() + "/profileImage.jpg");
        profileRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                loadingDialog.dismiss();
                staffDetailsStatic.setProfilePic(false);
                Toast.makeText(AccountDetails.this, "Profile picture removed", Toast.LENGTH_LONG).show();
                pi.setImageDrawable(getDrawable(R.drawable.profile_picture));

                DocumentReference userDetails = db.collection("staffDetails").document(mAuth.getCurrentUser().getUid());
                userDetails.update("profilePic",false);
                staffDetailsStatic.setProfilePic(false);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingDialog.dismiss();
                Toast.makeText(AccountDetails.this, "Error! Please try again later", Toast.LENGTH_LONG).show();

            }
        });
    }

    public void identityCheck() {
        final ImageView close, showPassword;
        Button done;
        TextView message;
        final EditText etPassword;
        adminDialog.setContentView(R.layout.pop_up_admin_password_check);
        adminDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        close = adminDialog.findViewById(R.id.img_close_adminPU);

        showPassword = adminDialog.findViewById(R.id.img_showPassword_adminPU);

        etPassword = adminDialog.findViewById(R.id.et_enterCode_adminPU);

        done = adminDialog.findViewById(R.id.bt_done_adminPU);

        message = adminDialog.findViewById(R.id.tv_message_adminPU);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminDialog.dismiss();
            }
        });

        message.setText("Please enter security code to proceed.");

        etPassword.setHint("Enter security code");

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
                        if (password.equals(documentSnapshot.getString("changeDetailsPassword"))) {
                            if (selected == 1) {
                                branchPopUp();
                            } else {
                                positionPopUp();
                            }
                            adminDialog.dismiss();
                        } else {
                            Toast.makeText(AccountDetails.this, "Identity verification failed", Toast.LENGTH_SHORT).show();
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
                mBranch.setBackground(getDrawable(R.drawable.spinner_close));
                mPosition.setBackground(getDrawable(R.drawable.spinner_close));

                closeKeyboard();
            }
        });
    }


    public void branchPopUp() {
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
                mBranch.setText("  Branch LG");
                branchDialog.dismiss();
            }
        });


        branchPB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBranch.setText("  Branch PB");
                branchDialog.dismiss();
            }
        });

        branchPT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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


    public void positionPopUp() {
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

        mPosition.setBackgroundResource(R.drawable.spinner_open);


        sales = positionDialog.findViewById(R.id.bt_sales_positionPU);

        technician = positionDialog.findViewById(R.id.bt_technician_positionPU);

        admin = positionDialog.findViewById(R.id.bt_admin_positionPU);

        sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifiedAdmin = false;
                mPosition.setText("  Sales");
                //Set the text (Not yet verified) to be invisible as only admin need to be verified first only can be signed up
                verify.setVisibility(View.INVISIBLE);
                positionDialog.dismiss();
            }
        });


        technician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifiedAdmin = false;
                mPosition.setText("  Technician");
                verify.setVisibility(View.INVISIBLE);
                positionDialog.dismiss();
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPosition.setText("  Admin");
                verifiedAdmin = false;

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

    /**
     * Pop up which call staff to enter the code verify their admin identity
     */
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
                            Toast.makeText(AccountDetails.this, "Admin verified successfully", Toast.LENGTH_SHORT).show();
                            adminDialog.dismiss();
                        } else {
                            verifiedAdmin = false;
                            //Change the text message
                            verify.setText("(Verification Failed)");
                            Toast.makeText(AccountDetails.this, "Admin verification failed", Toast.LENGTH_SHORT).show();
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
        String fName, lName, mobile, branch, position;
        fName = mFName.getText().toString().trim();
        lName = mLName.getText().toString().trim();
        mobile = mMobile.getText().toString().trim();
        branch = mBranch.getText().toString().trim();
        position = mPosition.getText().toString().trim();
        Map<String, Object> user = new HashMap<>();

        //Information verification
        if (fName.isEmpty()) {
            mFName.setError("First name cannot be empty");
            mFName.requestFocus();
            return;
        }

        if (lName.isEmpty()) {
            mLName.setError("Last name cannot be empty");
            mLName.requestFocus();
            return;
        }

        if (mobile.isEmpty()) {
            mMobile.setError("Mobile cannot be empty");
            mMobile.requestFocus();
            return;
        }

        if (!Patterns.PHONE.matcher(mobile).matches()) {
            mMobile.setError("Invalid mobile format");
            mMobile.requestFocus();
            return;
        }

        if (branch.equalsIgnoreCase("No branch selected")) {
            Toast.makeText(AccountDetails.this, "Please select your branch", Toast.LENGTH_SHORT).show();
            return;
        }

        if (position.equalsIgnoreCase("No position selected")) {
            Toast.makeText(AccountDetails.this, "Please select your position", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!verifiedAdmin && position.equals("Admin")) {
            Toast.makeText(AccountDetails.this, "Admin verification failed", Toast.LENGTH_SHORT).show();
            return;
        }

        closeKeyboard();

        if (branch.equalsIgnoreCase("Branch LG"))
            branch = "LG";

        else if (branch.equalsIgnoreCase("Branch PB"))
            branch = "PB";

        else
            branch = "PT";

        user.put("fName", capitalize(fName));
        user.put("lName", capitalize(lName));
        user.put("mobile", mobile);
        user.put("branch", branch);
        user.put("position", position);

        Map<String, String> dummyData = new HashMap<>();
        dummyData.put("placeHolder", "dummyData");

        EmailNotification emailNotification = new EmailNotification();
        emailNotification.setEmail(mAuth.getCurrentUser().getEmail());
        AppNotification appNotification = new AppNotification();

        switch (getPositionCode()) {
            case 1: {
                if (!position.equalsIgnoreCase("admin")) {
                    oldPosition = "Admin";
                    if (position.equalsIgnoreCase("sales")) {
                        setPositionCode(2);
                        DocumentReference upDatePosition = db.collection("staffDetails").document("sales")
                                .collection("sales").document(mAuth.getCurrentUser().getUid());
                        upDatePosition.set(dummyData);
                    } else if (position.equalsIgnoreCase("technician")) {
                        setPositionCode(3);
                        DocumentReference upDatePosition = db.collection("staffDetails").document("technician")
                                .collection("technician").document(mAuth.getCurrentUser().getUid());
                        upDatePosition.set(dummyData);
                    }

                    DocumentReference deletePosition = db.collection("adminDetails").document("adminList")
                            .collection("appNotificationPreference").document(mAuth.getCurrentUser().getUid());
                    deletePosition.delete();

                    DocumentReference deletePositionApp = db.collection("adminDetails").document("adminList")
                            .collection("emailNotificationPreference").document(mAuth.getCurrentUser().getUid());
                    deletePositionApp.delete();

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

                    CollectionReference adminEmails = db.collection("adminDetails").document("adminList").collection("emailNotificationPreference");
                    adminEmails.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                if (documentSnapshot.getBoolean("changePosition")) {
                                    positionChange(documentSnapshot.getString("email"));
                                }
                            }
                        }
                    });

                    String title = "Staff Changed Position";
                    String body = user.get("fName") + " " + user.get("lName") + " From " + oldPosition + " -> " + user.get("position");
                    AppNotification sendAppNotification = new AppNotification();
                    requestQueue1.add(sendAppNotification.sendNotification("changePosition", title, body));

                }
                break;
            }

            case 2: {
                if (!position.equalsIgnoreCase("sales")) {
                    oldPosition = "Sales";
                    if (position.equalsIgnoreCase("admin")) {
                        setPositionCode(1);

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
                                .collection("emailNotificationPreference").document(mAuth.getCurrentUser().getUid());
                        upDatePosition.set(emailNotification);


                        DocumentReference upDatePositionApp = db.collection("adminDetails").document("adminList")
                                .collection("appNotificationPreference").document(mAuth.getCurrentUser().getUid());
                        upDatePositionApp.set(appNotification);

                    } else if (position.equalsIgnoreCase("technician")) {
                        setPositionCode(3);
                        DocumentReference upDatePosition = db.collection("staffDetails").document("technician")
                                .collection("technician").document(mAuth.getCurrentUser().getUid());
                        upDatePosition.set(dummyData);
                    }
                    DocumentReference deletePosition = db.collection("staffDetails").document("sales")
                            .collection("sales").document(mAuth.getCurrentUser().getUid());
                    deletePosition.delete();

                    CollectionReference adminEmails = db.collection("adminDetails").document("adminList").collection("emailNotificationPreference");
                    adminEmails.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                if (documentSnapshot.getBoolean("changePosition")) {
                                    positionChange(documentSnapshot.getString("email"));
                                }
                            }
                        }
                    });

                    String title = "Staff Changed Position";
                    String body = user.get("fName") + " " + user.get("lName") + " From " + oldPosition + " -> " + user.get("position");
                    AppNotification sendAppNotification = new AppNotification();
                    requestQueue1.add(sendAppNotification.sendNotification("changePosition", title, body));
                }
                break;
            }

            case 3: {
                if (!position.equalsIgnoreCase("technician")) {
                    oldPosition = "Technician";
                    if (position.equalsIgnoreCase("admin")) {
                        setPositionCode(1);

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
                                .collection("emailNotificationPreference").document(mAuth.getCurrentUser().getUid());
                        upDatePosition.set(emailNotification);


                        DocumentReference upDatePositionApp = db.collection("adminDetails").document("adminList")
                                .collection("appNotificationPreference").document(mAuth.getCurrentUser().getUid())
                                .collection("app").document("app");
                        upDatePositionApp.set(appNotification);

                    } else if (position.equalsIgnoreCase("sales")) {
                        setPositionCode(2);
                        DocumentReference upDatePosition = db.collection("staffDetails").document("sales")
                                .collection("sales").document(mAuth.getCurrentUser().getUid());
                        upDatePosition.set(dummyData);
                    }
                    DocumentReference deletePosition = db.collection("staffDetails").document("technician")
                            .collection("technician").document(mAuth.getCurrentUser().getUid());
                    deletePosition.delete();

                    CollectionReference adminEmails = db.collection("adminDetails").document("adminList").collection("emailNotificationPreference");
                    adminEmails.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                if (documentSnapshot.getBoolean("changePosition")) {
                                    positionChange(documentSnapshot.getString("email"));
                                }
                            }
                        }
                    });

                    String title = "Staff Changed Position";
                    String body = user.get("fName") + " " + user.get("lName") + " From " + oldPosition + " -> " + user.get("position");
                    AppNotification sendAppNotification = new AppNotification();
                    requestQueue1.add(sendAppNotification.sendNotification("changePosition", title, body));

                }
                break;
            }
        }

        if (getStaffDetailsStatic().getBranch().equalsIgnoreCase("LG")) {
            if (branch.equalsIgnoreCase("PB")) {
                CollectionReference adminEmails = db.collection("adminDetails").document("adminList")
                        .collection("emailNotificationPreference");
                adminEmails.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.getBoolean("changeBranch")) {
                                branchChange(documentSnapshot.getString("email"), "LG", "PB");
                            }
                        }
                    }
                });

                getStaffDetailsStatic().setBranch("PB");

                String title = "Staff Changed Branch";
                String body = user.get("fName") + " " + user.get("lName") + " From LG -> PB";
                AppNotification sendAppNotification = new AppNotification();
                requestQueue2.add(sendAppNotification.sendNotification("changeBranch", title, body));
            } else if (branch.equalsIgnoreCase("PT")) {
                CollectionReference adminEmails = db.collection("adminDetails").document("adminList")
                        .collection("emailNotificationPreference");
                adminEmails.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.getBoolean("changeBranch")) {
                                branchChange(documentSnapshot.getString("email"), "LG", "PT");
                            }
                        }
                    }
                });

                getStaffDetailsStatic().setBranch("PT");
                String title = "Staff Changed Branch";
                String body = user.get("fName") + " " + user.get("lName") + " From LG -> PT";
                AppNotification sendAppNotification = new AppNotification();
                requestQueue2.add(sendAppNotification.sendNotification("changeBranch", title, body));
            }

        } else if (getStaffDetailsStatic().getBranch().equalsIgnoreCase("PB")) {
            if (branch.equalsIgnoreCase("LG")) {
                CollectionReference adminEmails = db.collection("adminDetails").document("adminList")
                        .collection("emailNotificationPreference");
                adminEmails.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.getBoolean("changeBranch")) {
                                branchChange(documentSnapshot.getString("email"), "PB", "LG");
                            }
                        }
                    }
                });

                getStaffDetailsStatic().setBranch("LG");
                String title = "Staff Changed Branch";
                String body = user.get("fName") + " " + user.get("lName") + " From PB -> LG";
                AppNotification sendAppNotification = new AppNotification();
                requestQueue2.add(sendAppNotification.sendNotification("changeBranch", title, body));

            } else if (branch.equalsIgnoreCase("PT")) {
                CollectionReference adminEmails = db.collection("adminDetails").document("adminList")
                        .collection("emailNotificationPreference");
                adminEmails.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.getBoolean("changeBranch")) {
                                branchChange(documentSnapshot.getString("email"), "PB", "PT");
                            }
                        }
                    }
                });

                getStaffDetailsStatic().setBranch("PT");
                String title = "Staff Changed Branch";
                String body = user.get("fName") + " " + user.get("lName") + " From PB -> PT";
                AppNotification sendAppNotification = new AppNotification();
                requestQueue2.add(sendAppNotification.sendNotification("changeBranch", title, body));
            }
        } else if (getStaffDetailsStatic().getBranch().equalsIgnoreCase("PT")) {
            if (branch.equalsIgnoreCase("LG")) {
                CollectionReference adminEmails = db.collection("adminDetails").document("adminList")
                        .collection("emailNotificationPreference");
                adminEmails.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.getBoolean("changeBranch")) {
                                branchChange(documentSnapshot.getString("email"), "PT", "LG");
                            }
                        }
                    }
                });

                getStaffDetailsStatic().setBranch("LG");
                String title = "Staff Changed Branch";
                String body = user.get("fName") + " " + user.get("lName") + " From PT -> LG";
                AppNotification sendAppNotification = new AppNotification();
                requestQueue2.add(sendAppNotification.sendNotification("changeBranch", title, body));
            } else if (branch.equalsIgnoreCase("PB")) {
                CollectionReference adminEmails = db.collection("adminDetails").document("adminList")
                        .collection("emailNotificationPreference");
                adminEmails.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.getBoolean("changeBranch")) {
                                branchChange(documentSnapshot.getString("email"), "PT", "PB");
                            }
                        }
                    }
                });

                getStaffDetailsStatic().setBranch("PB");
                String title = "Staff Changed Branch";
                String body = user.get("fName") + " " + user.get("lName") + " From PT -> PB";
                AppNotification sendAppNotification = new AppNotification();
                requestQueue2.add(sendAppNotification.sendNotification("changeBranch", title, body));
            }
        }


        DocumentReference staffDetailsDB = db.collection("staffDetails").document(mAuth.getCurrentUser().getUid());
        staffDetailsDB.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AccountDetails.this, "User Details Updated", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AccountDetails.this, "Error, fail to update user details", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void changePassword(View view) {
        DocumentReference userDetails = db.collection("staffDetails").document(mAuth.getCurrentUser().getUid());
        userDetails.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getBoolean("google")) {
                    Toast.makeText(AccountDetails.this, "Google login cannot change password", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    changePasswordPopUp();
                }
            }
        });
    }

    public void changePasswordPopUp() {
        final ImageView closeCU, showPasswordCU, showPassword2CU;
        TextView message, title, tvPassword, tvPassword2;
        final Button updateCU;
        final EditText etEmail, etPasswordCU;

        checkUserDialog.setContentView(R.layout.pop_up_change_password);
        checkUserDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        closeCU = checkUserDialog.findViewById(R.id.img_close_passwordPU);
        showPasswordCU = checkUserDialog.findViewById(R.id.img_showPassword_passwordPU);
        showPassword2CU = checkUserDialog.findViewById(R.id.img_showPassword2_passwordPU);
        updateCU = checkUserDialog.findViewById(R.id.bt_update_passwordPU);
        etEmail = checkUserDialog.findViewById(R.id.et_password_passwordPU);
        etPasswordCU = checkUserDialog.findViewById(R.id.et_password2_passwordPU);
        title = checkUserDialog.findViewById(R.id.tv_title_passwordPU);
        message = checkUserDialog.findViewById(R.id.tv_message_passwordPU);
        tvPassword = checkUserDialog.findViewById(R.id.tv_password_passwordPU);
        tvPassword2 = checkUserDialog.findViewById(R.id.tv_password2_passwordPU);

        checkUserDialog.setCanceledOnTouchOutside(false);
        checkUserDialog.show();
        closeCU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AccountDetails.this, "Password not changed", Toast.LENGTH_LONG).show();
                checkUserDialog.dismiss();
            }
        });

        showPasswordCU.setVisibility(View.INVISIBLE);
        message.setVisibility(View.INVISIBLE);
        tvPassword.setText("Email");
        tvPassword2.setText("Password");
        title.setText("Identity verification");
        etEmail.setHint("  Enter Email");
        etEmail.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        etPasswordCU.setHint("  Enter Password");
        updateCU.setText("Verify");
        showPassword2CU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!showPasswordPIC) {
                    showPassword2CU.setImageResource(R.drawable.hide_password_icon);
                    etPasswordCU.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etPasswordCU.setSelection(etPasswordCU.getText().length());
                    showPasswordPIC = true;
                } else {
                    showPassword2CU.setImageResource(R.drawable.show_password_icon);
                    etPasswordCU.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etPasswordCU.setSelection(etPasswordCU.getText().length());
                    showPasswordPIC = false;
                }
            }
        });

        updateCU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailCU = etEmail.getText().toString().trim();
                passwordCU = etPasswordCU.getText().toString().trim();

                if (emailCU.isEmpty()) {
                    etEmail.setError("Email cannot be empty");
                    etEmail.requestFocus();
                    return;
                }

                //Check if the email format is correct
                if (!Patterns.EMAIL_ADDRESS.matcher(emailCU).matches()) {
                    etEmail.setError("Invalid email format");
                    etEmail.requestFocus();
                    return;
                }

                if (passwordCU.isEmpty()) {
                    etPasswordCU.setError("Password cannot be empty");
                    etPasswordCU.requestFocus();
                    return;
                }

                if (!emailCU.equalsIgnoreCase(mAuth.getCurrentUser().getEmail())) {
                    mAuth.signOut();
                    startActivity(new Intent(AccountDetails.this, LoginPage.class));
                    Toast.makeText(AccountDetails.this, "Wrong email, signed out", Toast.LENGTH_LONG).show();
                    finish();
                }

                FirebaseUser user = mAuth.getCurrentUser();
                AuthCredential credential = EmailAuthProvider
                        .getCredential(emailCU, passwordCU);
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            changePasswordDialog.show();
                            checkUserDialog.dismiss();
                        } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            checkUserDialog.dismiss();
                            passwordChangeFailed();
                            mAuth.signOut();
                            startActivity(new Intent(AccountDetails.this, LoginPage.class));
                            Toast.makeText(AccountDetails.this, "Wrong password, signed out", Toast.LENGTH_LONG).show();

                        } else if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                            checkUserDialog.dismiss();
                            passwordChangeFailed();
                            mAuth.signOut();
                            startActivity(new Intent(AccountDetails.this, LoginPage.class));
                            Toast.makeText(AccountDetails.this, "Invalid user, signed out", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                });
            }
        });

        final ImageView close, showPassword, showPassword2;
        final Button update;
        final EditText etPassword, etPassword2;
        changePasswordDialog.setContentView(R.layout.pop_up_change_password);
        changePasswordDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        close = changePasswordDialog.findViewById(R.id.img_close_passwordPU);
        showPassword = changePasswordDialog.findViewById(R.id.img_showPassword_passwordPU);
        showPassword2 = changePasswordDialog.findViewById(R.id.img_showPassword2_passwordPU);
        update = changePasswordDialog.findViewById(R.id.bt_update_passwordPU);
        etPassword = changePasswordDialog.findViewById(R.id.et_password_passwordPU);
        etPassword2 = changePasswordDialog.findViewById(R.id.et_password2_passwordPU);
        changePasswordDialog.setCanceledOnTouchOutside(false);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AccountDetails.this, "Password not changed", Toast.LENGTH_LONG).show();
                changePasswordDialog.dismiss();
            }
        });

        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!showPasswordPPU) {
                    showPassword.setImageResource(R.drawable.hide_password_icon);
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etPassword.setSelection(etPassword.getText().length());
                    showPasswordPPU = true;
                } else {
                    showPassword.setImageResource(R.drawable.show_password_icon);
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etPassword.setSelection(etPassword.getText().length());
                    showPasswordPPU = false;
                }
            }
        });

        showPassword2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!showPassword2PPU) {
                    showPassword2.setImageResource(R.drawable.hide_password_icon);
                    etPassword2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etPassword2.setSelection(etPassword2.getText().length());
                    showPassword2PPU = true;
                } else {
                    showPassword2.setImageResource(R.drawable.show_password_icon);
                    etPassword2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etPassword2.setSelection(etPassword2.getText().length());
                    showPassword2PPU = false;
                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String password = etPassword.getText().toString().trim();
                String password2 = etPassword2.getText().toString().trim();

                if (password.isEmpty()) {
                    etPassword.setError("Password cannot be empty");
                    etPassword.requestFocus();
                    return;
                }

                if (password.length() < 6) {
                    etPassword.setError("Password need to be more than 6 character");
                    etPassword.requestFocus();
                    return;
                }

                if (!password2.equals(password)) {
                    etPassword2.setError("Password doesn't matches");
                    etPassword2.requestFocus();
                    return;
                }
                final FirebaseUser user2 = mAuth.getCurrentUser();
                AuthCredential credential2 = EmailAuthProvider
                        .getCredential(emailCU, passwordCU);
                user2.reauthenticate(credential2).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user2.updatePassword(password).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    passwordChange();
                                    Toast.makeText(AccountDetails.this, "Password update successfully", Toast.LENGTH_LONG).show();
                                    changePasswordDialog.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AccountDetails.this, "Error, failed to update password", Toast.LENGTH_LONG).show();
                                    changePasswordDialog.dismiss();

                                }
                            });
                        } else {
                            Toast.makeText(AccountDetails.this, "Error, failed to update password", Toast.LENGTH_LONG).show();
                            changePasswordDialog.dismiss();

                        }

                    }
                });


            }
        });

    }

    public void back(View view) {
        super.onBackPressed();
    }

    public String capitalize(String s) {
        if ((s == null) || (s.trim().length() == 0)) {
            return s;
        }
        s = s.toLowerCase();
        char[] cArr = s.trim().toCharArray();
        cArr[0] = Character.toUpperCase(cArr[0]);
        for (int i = 0; i < cArr.length; i++) {
            if (cArr[i] == ' ' && (i + 1) < cArr.length) {
                cArr[i + 1] = Character.toUpperCase(cArr[i + 1]);
            }
            if (cArr[i] == '-' && (i + 1) < cArr.length) {
                cArr[i + 1] = Character.toUpperCase(cArr[i + 1]);
            }
            if (cArr[i] == '\'' && (i + 1) < cArr.length) {
                cArr[i + 1] = Character.toUpperCase(cArr[i + 1]);
            }
        }
        return new String(cArr);
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void passwordChange() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss ");
        Date dt = new Date();
        String message;

        message = "Dear " + mFName.getText().toString().trim() + ",\n"
                + "\tYour password has successfully being changed at " + formatter.format(dt) + ". If this is not an action that you recognize, please login "
                + "to your account and change your password immediately.\n\n"
                + "\tPlease report to the admin if there's any mistake. Thank you and have a nice day.\n\n"
                + "Regards,\n"
                + "Ashita";
        JavaMailAPI javaMailAPI = new JavaMailAPI(this, mEmail.getText().toString().trim(),
                "Password Changed", message);

        javaMailAPI.execute();
    }

    public void passwordChangeFailed() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss ");
        Date dt = new Date();
        String message;

        message = "Dear " + mFName.getText().toString().trim() + ",\n"
                + "\tSomeone attempted to change your password at" + formatter.format(dt) + ". If this is not an action that you recognize, please login "
                + "to your account and change your password immediately.\n\n"
                + "\tPlease report to the admin if there's any mistake. Thank you and have a nice day.\n\n"
                + "Regards,\n"
                + "Ashita";

        JavaMailAPI javaMailAPI = new JavaMailAPI(this, mEmail.getText().toString().trim(),
                "Attempt To Change Password", message);

        javaMailAPI.execute();
    }

    public void positionChange(String email) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss ");
        Date dt = new Date();
        String message;

        message = mFName.getText().toString().trim() + " " + mLName.getText().toString().trim() + " changed their position at" + formatter.format(dt) + ".\n\n"
                + "\tFrom " + oldPosition + " -> " + mPosition.getText().toString().trim() + "\n\n"
                + "Staff position can be updated directly from the admin pages.";

        JavaMailAPI javaMailAPI = new JavaMailAPI(this, email,
                "Staff Position Changed", message);

        javaMailAPI.execute();
    }


    private void branchChange(String email, String original, String changed) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss ");
        Date dt = new Date();
        String message;

        message = mFName.getText().toString().trim() + " " + mLName.getText().toString().trim() + " changed their branch at " + formatter.format(dt) + ".\n\n"
                + "\tFrom " + original + " -> " + changed + "\n\n"
                + "Staff branch can be updated directly from the admin pages.";

        JavaMailAPI javaMailAPI = new JavaMailAPI(this, email,
                "Staff Branch Changed", message);

        javaMailAPI.execute();
    }

    public void forgetPassword(View view) {
        Intent intent = new Intent(AccountDetails.this, ForgetPassword.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (staffDetailsStatic.isProfilePic()) {
            StorageReference profileRef = storageReference.child("staff/" + mAuth.getCurrentUser().getUid() + "/profileImage.jpg");
            profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(pi);
                }
            });
        }
    }
}
