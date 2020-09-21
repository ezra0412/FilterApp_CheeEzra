package com.example.filterapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class AccountDetails extends AppCompatActivity {
    boolean showPasswordPU = false;
    boolean verifiedAdmin = false;
    int selected = 0;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseFirestore fs = FirebaseFirestore.getInstance();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    TextView mEmail, mBranch, mPosition, verify;
    EditText mFName, mLName, mMobile;
    ImageView pi;
    Dialog loadingDialog, branchDialog, positionDialog, adminDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);
        pi = findViewById(R.id.img_pi_myAccount);
        loadingDialog = new Dialog(this);
        branchDialog = new Dialog(this);
        positionDialog = new Dialog(this);
        adminDialog = new Dialog(this);
        mFName = findViewById(R.id.et_fName_accountDetails);
        mLName = findViewById(R.id.et_lName_accountDetails);
        mEmail = findViewById(R.id.tv_email_accountDetails);
        mMobile = findViewById(R.id.et_mobile_accountDetails);
        mBranch = findViewById(R.id.tv_branch_accountDetails);
        mPosition = findViewById(R.id.tv_position_accountDetails);
        verify = findViewById(R.id.tv_verify_accountDetails);

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

        DocumentReference userDetails = db.collection("staffDetails").document(mAuth.getCurrentUser().getUid());
        userDetails.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                StaffDetails staffDetails = new StaffDetails();
                staffDetails = value.toObject(StaffDetails.class);
                mFName.setText("  " + staffDetails.getfName());
                mLName.setText("  " + staffDetails.getlName());
                mEmail.setText("  " + mAuth.getCurrentUser().getEmail());
                mMobile.setText("  " + staffDetails.getMobile());
                if (value.getString("branch").equalsIgnoreCase("LG")) {
                    mBranch.setText("  Branch LG");
                } else if (value.getString("branch").equalsIgnoreCase("PB")) {
                    mBranch.setText("  Branch PB");
                } else {
                    mBranch.setText("  Branch PT");
                }
                mPosition.setText("  " + staffDetails.getPosition());
                if (staffDetails.getPosition().equalsIgnoreCase("admin")) {
                    verifiedAdmin = true;
                }
            }
        });

        StorageReference profileRef = storageReference.child("staff/" + mAuth.getCurrentUser().getUid() + "/profileImage.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(pi);
            }
        });
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
        final StorageReference imageRefence = storageReference.child("staff/" + mAuth.getCurrentUser().getUid() + "/profileImage.jpg");
        imageRefence.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRefence.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(pi);
                        loadingDialog.dismiss();
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
                Toast.makeText(AccountDetails.this, "Profile picture removed", Toast.LENGTH_LONG).show();
                pi.setImageDrawable(getDrawable(R.drawable.profile_picture));
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
}
