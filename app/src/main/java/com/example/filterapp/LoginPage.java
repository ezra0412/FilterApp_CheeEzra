package com.example.filterapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginPage extends AppCompatActivity {
    EditText mEmail, mPassword;
    ImageView showPasswordIcon;
    Boolean showPassword = false;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userID = "", sBranch = "", sPosition = "", sMobile = "", sEmail = "";
    boolean signUpAllow = false;
    boolean verifiedAdmin = false;
    boolean showPasswordPU = false;
    Dialog googleDialog, branchDialog, positionDialog, loadingDialog, adminDialog;
    GoogleSignInClient mGoogleSignInClient;
    final static int RC_SIGN_IN = 234;
    StaffDetails staffDetails;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        mEmail = findViewById(R.id.et_email_login);
        mPassword = findViewById(R.id.et_password_login);
        showPasswordIcon = findViewById(R.id.img_passwordIcon_login);
        googleDialog = new Dialog(this);
        branchDialog = new Dialog(this);
        positionDialog = new Dialog(this);
        loadingDialog = new Dialog(this);
        adminDialog = new Dialog(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        findViewById(R.id.icon_google_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }
/*
    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            finish();
        }
    }
    */

    public void login(View view) {
        String email, password;
        email = mEmail.getText().toString().trim();
        password = mPassword.getText().toString().trim();


        if (email.isEmpty()) {
            mEmail.setError("Email cannot be empty");
            mEmail.requestFocus();
            return;
        }

        //Check if the email format is correct
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("Wrong email format");
            mEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            mPassword.setError("Password cannot be empty");
            mPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            mPassword.setError("Password need to be more than 6 character");
            mPassword.requestFocus();
            return;
        }

        closeKeyboard();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginPage.this, "Logged in", Toast.LENGTH_SHORT).show();
                    loadingScreen();

                } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                    mPassword.setError("Wrong password");
                    mPassword.requestFocus();
                    return;

                } else if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                    mEmail.setError("Invalid user");
                    mEmail.requestFocus();
                    return;
                }

            }
        });
    }

    public void showPassword(View view) {
        if (!showPassword) {
            showPasswordIcon.setImageResource(R.drawable.hide_password_icon);
            mPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            mPassword.setSelection(mPassword.getText().length());
            showPassword = true;
        } else {
            showPasswordIcon.setImageResource(R.drawable.show_password_icon);
            mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            mPassword.setSelection(mPassword.getText().length());
            showPassword = false;
        }

    }

    public void forgetPassword(View view) {
        Intent intent = new Intent(LoginPage.this, ForgetPassword.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void sendMailToAdmin(String adminEmail, String staffEmail) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss ");
        Date dt = new Date();
        String message;

        message = "A new staff had signed up at " + formatter.format(dt) + ". Here are all the details about this staff:\n\n"
                + "First Name: " + staffDetails.getfName() + "\n"
                + "Last Name: " + staffDetails.getlName() + "\n"
                + "Email: " + staffEmail + "\n"
                + "Mobile: " + staffDetails.getMobile() + "\n"
                + "Branch: " + staffDetails.getBranch() + "\n"
                + "Position: " + staffDetails.getPosition() + "\n\n"
                + "If not an staff that you recognize please remove it through the admin account.";


        JavaMailAPI javaMailAPI = new JavaMailAPI(this, adminEmail,
                "New staff signed up (" + staffDetails.getlName() + " " + staffDetails.getfName() + ")", message);

        javaMailAPI.execute();
    }

    /**
     * Send to email to the staff indicating that they had signed up
     *
     * @param email the email of staff to email to
     */
    public void sendMailToStaff(String email) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss ");
        Date dt = new Date();
        String message;

        message = "Dear " + staffDetails.getfName() + ",\n"
                + "You had sign up as a XXX's staff at during " + formatter.format(dt) + ". Here are all the details about your sign up:\n\n"
                + "First Name: " + staffDetails.getfName() + "\n"
                + "Last Name: " + staffDetails.getlName() + "\n"
                + "Email: " + email + "\n"
                + "Mobile: " + staffDetails.getMobile() + "\n"
                + "Branch: " + staffDetails.getBranch() + "\n"
                + "Position: " + staffDetails.getPosition() + "\n\n"
                + "Please report to the admin if there's any mistake. Thank you and have a nice day.\n\n"
                + "Regards,\n"
                + "XXX ";


        JavaMailAPI javaMailAPI = new JavaMailAPI(this, email,
                "Sign Up Details", message);

        javaMailAPI.execute();
    }

    /**
     * Display loading screen
     */
    public void loadingScreen() {
        ProgressBar progressBar;
        Sprite style = new Wave();
        loadingDialog.setContentView(R.layout.pop_up_loading_screen);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressBar = loadingDialog.findViewById(R.id.sk_loadingPU);
        progressBar.setIndeterminateDrawable(style);

        //Make the loading dialog not dismissible
        loadingDialog.setCanceledOnTouchOutside(false);

        loadingDialog.show();

        //Let the loading dialog run for 3 sec to make sure everything is prepared
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismiss();
            }
        }, 3000);

        loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Intent intent = new Intent(LoginPage.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode == RC_SIGN_IN) {

            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                //authenticating with firebase
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(LoginPage.this, "Error please try again later", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Create user's account/ sign in
     *
     * @param acct user google details
     */
    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        //Now using firebase we are signing in the user here
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userID = mAuth.getCurrentUser().getUid();
                            FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                            DocumentReference docIdRef = rootRef.collection("staffDetails").document(userID);
                            docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        //Login the user if the user signed up before
                                        if (document.exists()) {
                                            Toast.makeText(LoginPage.this, "Sign in successfully", Toast.LENGTH_SHORT).show();
                                            loadingScreen();
                                        } else {
                                            //Let the user enter some additional details
                                            staffDetails = new StaffDetails();

                                            sEmail = acct.getEmail();

                                            staffDetails.setfName(acct.getGivenName());

                                            staffDetails.setlName(acct.getFamilyName());

                                            popUpGoogle();
                                        }
                                    }
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginPage.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    /**
     * Let the user enter additional details if the user uses google to sign up
     */
    public void popUpGoogle() {
        final ImageView mainClose;
        final EditText mobile;
        final TextView branch, position, verifyMessage, tncMessage;
        final CheckBox tNcGoogle;
        Button done;
        googleDialog.setContentView(R.layout.pop_up_google);
        googleDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mobile = googleDialog.findViewById(R.id.et_mobile_googlePU);

        mainClose = googleDialog.findViewById(R.id.img_close_googlePU);

        branch = googleDialog.findViewById(R.id.tv_branch_googlePU);

        position = googleDialog.findViewById(R.id.tv_position_googlePU);

        verifyMessage = googleDialog.findViewById(R.id.tv_verifiedMessage_googlePU);

        tNcGoogle = googleDialog.findViewById(R.id.cb_tnc_googlePU);

        done = googleDialog.findViewById(R.id.bt_done_googlePU);

        tncMessage = googleDialog.findViewById(R.id.tv_tnc_googlePU);

        //Pop up branch
        ImageView branchClose;
        Button branchA, branchB, branchC;
        branchDialog.setContentView(R.layout.pop_up_branch);
        branchDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        branchClose = branchDialog.findViewById(R.id.img_close_branchPU);

        branchA = branchDialog.findViewById(R.id.bt_a_branchPU);

        branchB = branchDialog.findViewById(R.id.bt_b_branchPU);

        branchC = branchDialog.findViewById(R.id.bt_c_branchPU);


        //Pop up position
        final ImageView positionClose;
        final Button sales, technician, admin;
        positionDialog.setContentView(R.layout.pop_up_position);
        positionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        positionClose = positionDialog.findViewById(R.id.img_close_positionPU);

        sales = positionDialog.findViewById(R.id.bt_sales_positionPU);

        technician = positionDialog.findViewById(R.id.bt_technician_positionPU);

        admin = positionDialog.findViewById(R.id.bt_admin_positionPU);

        mainClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleDialog.dismiss();
            }
        });
        googleDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                closeKeyboard();
                //User enter all the details correctly and is allowed to sign up
                if (signUpAllow) {
                    closeKeyboard();
                    DocumentReference staffDetailsDB = db.collection("staffDetails").document(mAuth.getCurrentUser().getUid());
                    staffDetailsDB.set(staffDetails)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    DocumentReference getEmail = db.collection("adminDetails").document("loginDetails");

                                    getEmail.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            sendMailToAdmin(documentSnapshot.getString("adminEmail"), sEmail);
                                        }
                                    });

                                    sendMailToStaff(sEmail);
                                    Toast.makeText(LoginPage.this, "Signed up successfully", Toast.LENGTH_SHORT).show();
                                    loadingScreen();
                                }
                            });

                } else {
                    //Delete the user account of the user don't want to enter additional details
                    FirebaseUser user = mAuth.getCurrentUser();
                    mGoogleSignInClient.revokeAccess();
                    mAuth.signOut();
                    user.delete();
                    Toast.makeText(LoginPage.this, "Sign up failed", Toast.LENGTH_SHORT).show();

                }
            }
        });

        branch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                branch.setBackgroundResource(R.drawable.spinner_open);
                branchDialog.show();
            }
        });

        position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position.setBackgroundResource(R.drawable.spinner_open);
                positionDialog.show();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sMobile = mobile.getText().toString().trim();
                if (sMobile.isEmpty()) {
                    mobile.setError("Mobile cannot be empty");
                    mobile.requestFocus();
                    return;
                }

                if (!Patterns.PHONE.matcher(sMobile).matches()) {
                    mobile.setError("Invalid mobile format");
                    mobile.requestFocus();
                    return;
                }

                if (sBranch.isEmpty()) {
                    Toast.makeText(LoginPage.this, "Please select your branch", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (sPosition.isEmpty()) {
                    Toast.makeText(LoginPage.this, "Please select your position", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!tNcGoogle.isChecked()) {
                    Toast.makeText(LoginPage.this, "Please check the text box", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!verifiedAdmin && sPosition.equals("Admin")) {
                    Toast.makeText(LoginPage.this, "Admin verification failed", Toast.LENGTH_SHORT).show();
                    return;
                }

                signUpAllow = true;

                staffDetails.setMobile(sMobile);

                staffDetails.setBranch(sBranch);

                staffDetails.setPosition(sPosition);

                staffDetails.setGoogle(true);

                googleDialog.dismiss();

            }
        });

        googleDialog.show();


        branchClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                branchDialog.dismiss();
            }
        });


        branchA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sBranch = "A";
                branchDialog.dismiss();
            }
        });


        branchB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sBranch = "B";
                branchDialog.dismiss();
            }
        });

        branchC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sBranch = "C";
                branchDialog.dismiss();
            }
        });

        branchDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (!sBranch.isEmpty()) {
                    branch.setText("  " + sBranch);
                }
                branch.setBackgroundResource(R.drawable.spinner_close);
            }
        });

        positionClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                positionDialog.dismiss();
            }
        });

        sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sPosition = "Sales";
                verifyMessage.setVisibility(View.INVISIBLE);
                positionDialog.dismiss();

            }
        });

        technician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sPosition = "Technician";
                verifyMessage.setVisibility(View.INVISIBLE);
                positionDialog.dismiss();

            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sPosition = "Admin";
                verifyMessage.setVisibility(View.VISIBLE);
                positionDialog.dismiss();
                adminDialog.show();

            }
        });

        positionDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (!sPosition.isEmpty()) {
                    position.setText("  " + sPosition);
                    position.setBackgroundResource(R.drawable.spinner_close);
                }
            }
        });

        //Verify admin pop up
        final ImageView verifiyClose, showPassword;
        Button verifyDone;
        final EditText etPassword;
        adminDialog.setContentView(R.layout.pop_up_admin_password_check);
        adminDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        verifiyClose = adminDialog.findViewById(R.id.img_close_adminPU);

        showPassword = adminDialog.findViewById(R.id.img_showPassword_adminPU);

        etPassword = adminDialog.findViewById(R.id.et_enterCode_adminPU);

        verifyDone = adminDialog.findViewById(R.id.bt_done_adminPU);


        verifiyClose.setOnClickListener(new View.OnClickListener() {
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

        verifyDone.setOnClickListener(new View.OnClickListener() {
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
                            verifyMessage.setText("(Verified Successfully)");
                            //Change the text color
                            verifyMessage.setTextColor(getResources().getColor(R.color.green));
                            Toast.makeText(LoginPage.this, "Admin verified successfully", Toast.LENGTH_SHORT).show();
                            adminDialog.dismiss();
                        } else {
                            verifiedAdmin = false;
                            //Change the text message
                            verifyMessage.setText("(Verification Failed)");
                            Toast.makeText(LoginPage.this, "Admin verification failed", Toast.LENGTH_SHORT).show();
                            adminDialog.dismiss();
                        }
                    }
                });
            }
        });

        adminDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                closeKeyboard();
            }
        });

        tncMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginPage.this, TermNCondition.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    //this method is called on click
    private void signIn() {
        //getting the google signin intent
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void signUp(View view) {
        Intent intent = new Intent(LoginPage.this, SignUp.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}