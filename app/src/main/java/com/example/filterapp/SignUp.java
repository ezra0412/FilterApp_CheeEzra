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
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.filterapp.classes.EmailNotification;
import com.example.filterapp.classes.JavaMailAPI;
import com.example.filterapp.classes.StaffDetails;
import com.example.filterapp.fcm.AppNotification;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    boolean showPasswordB = false;
    boolean showPassword2B = false;
    boolean verifiedAdmin = false;
    boolean showPasswordPU = false;
    ImageView showPasswordIcon, showPasswordIcon2;
    TextView mPosition, mBranch, verify;
    EditText mFName, mLName, mEmail, mPassword, mPassword2, mMobile;
    Dialog positionDialog, branchDialog, adminDialog, loadingDialog, googleDialog;
    CheckBox tNc;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    StaffDetails staffDetails;
    GoogleSignInClient mGoogleSignInClient;
    final static int RC_SIGN_IN = 234;
    String userID = "", sBranch = "", sPosition = "", sMobile = "", sEmail = "";
    boolean signUpAllow = false;
    String token;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        showPasswordIcon = findViewById(R.id.ic_password_signUp);
        showPasswordIcon2 = findViewById(R.id.ic_password2_signUp);
        mPosition = findViewById(R.id.tv_position_signUp);
        mBranch = findViewById(R.id.tv_branch_signUp);
        mFName = findViewById(R.id.et_fName_signUp);
        mLName = findViewById(R.id.et_lName_signUp);
        mEmail = findViewById(R.id.et_email_signUp);
        mPassword = findViewById(R.id.et_password_signUp);
        mPassword2 = findViewById(R.id.et_password2_signUp);
        mMobile = findViewById(R.id.et_mobile_signUp);
        verify = findViewById(R.id.tv_verified_signUp);
        tNc = findViewById(R.id.cb_tNc_signUp);
        positionDialog = new Dialog(this);
        branchDialog = new Dialog(this);
        adminDialog = new Dialog(this);
        loadingDialog = new Dialog(this);
        googleDialog = new Dialog(this);
        requestQueue = Volley.newRequestQueue(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        findViewById(R.id.icon_google_signUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }


    /**
     * Show staff password for the first password
     *
     * @param view
     */
    public void showPassword(View view) {
        if (!showPasswordB) {
            //Change the password icon
            showPasswordIcon.setImageResource(R.drawable.hide_password_icon);
            //Show the password
            mPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            //Move the cursor to the last letter of the password
            mPassword.setSelection(mPassword.getText().length());
            showPasswordB = true;
        } else {
            showPasswordIcon.setImageResource(R.drawable.show_password_icon);
            //Hide the password
            mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            mPassword.setSelection(mPassword.getText().length());
            showPasswordB = false;
        }

    }

    /**
     * Show staff password for the re enter password
     *
     * @param view
     */
    public void showPassword2(View view) {
        if (!showPassword2B) {
            showPasswordIcon2.setImageResource(R.drawable.hide_password_icon);
            mPassword2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            mPassword2.setSelection(mPassword2.getText().length());
            showPassword2B = true;
        } else {
            showPasswordIcon2.setImageResource(R.drawable.show_password_icon);
            mPassword2.setTransformationMethod(PasswordTransformationMethod.getInstance());
            mPassword2.setSelection(mPassword2.getText().length());
            showPassword2B = false;
        }

    }

    /**
     * Call up the branch pop up for the stuff to chose which branch they work at
     *
     * @param view
     */
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
                mPosition.setText("  Sales");
                //Set the text (Not yet verified) to be invisible as only admin need to be verified first only can be signed up
                verify.setVisibility(View.INVISIBLE);
                positionDialog.dismiss();
            }
        });


        technician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPosition.setText("  Technician");
                verify.setVisibility(View.INVISIBLE);
                positionDialog.dismiss();
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                            Toast.makeText(SignUp.this, "Admin verified successfully", Toast.LENGTH_SHORT).show();
                            adminDialog.dismiss();
                        } else {
                            verifiedAdmin = false;
                            //Change the text message
                            verify.setText("(Verification Failed)");
                            Toast.makeText(SignUp.this, "Admin verification failed", Toast.LENGTH_SHORT).show();
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

    /**
     * Check the info the staff entered, create account for the stuff and also store their data into db
     *
     * @param view
     */
    public void signUp(View view) {

        String fName, lName, password, password2, mobile, branch, position;
        final String email;
        fName = mFName.getText().toString().trim();
        lName = mLName.getText().toString().trim();
        email = mEmail.getText().toString().trim();
        password = mPassword.getText().toString();
        password2 = mPassword2.getText().toString();
        mobile = mMobile.getText().toString().trim();
        branch = mBranch.getText().toString().trim();
        position = mPosition.getText().toString().trim();

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

        if (email.isEmpty()) {
            mEmail.setError("Email cannot be empty");
            mEmail.requestFocus();
            return;
        }

        //Check if the email format is correct
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("Invalid email format");
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

        if (!password2.equals(password)) {
            mPassword2.setError("Password doesn't matches");
            mPassword2.requestFocus();
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
            Toast.makeText(SignUp.this, "Please select your branch", Toast.LENGTH_SHORT).show();
            return;
        }

        if (position.equalsIgnoreCase("No position selected")) {
            Toast.makeText(SignUp.this, "Please select your position", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!tNc.isChecked()) {
            Toast.makeText(SignUp.this, "Please check the text box", Toast.LENGTH_SHORT).show();
            return;
        }

        //Check if the admin is being verified or not if the position chose is admin
        if (!verifiedAdmin && position.equals("Admin")) {
            Toast.makeText(SignUp.this, "Admin verification failed", Toast.LENGTH_SHORT).show();
            return;
        }

        closeKeyboard();

        if (branch.equalsIgnoreCase("Branch LG"))
            branch = "LG";

        else if (branch.equalsIgnoreCase("Branch PB"))
            branch = "PB";

        else
            branch = "PT";

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            token = task.getResult().getToken();
                        }
                    }
                });

        staffDetails = new StaffDetails(capitalize(fName), capitalize(lName), mobile, position, branch, email, false, false, token);
        //Create the staff account
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Map<String, String> dummyData = new HashMap<>();
                            dummyData.put("placeHolder", "dummyData");

                            EmailNotification emailNotification = new EmailNotification();
                            emailNotification.setEmail(email);
                            AppNotification appNotification = new AppNotification();

                            //Get the admin email and then send the notice email to the admin
                            CollectionReference adminEmails = db.collection("adminDetails").document("adminList").collection("emailNotificationPreference");
                            adminEmails.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        if (documentSnapshot.getBoolean("staffSignUp")) {
                                            sendMailToAdmin(documentSnapshot.getString("email"), email);
                                        }
                                    }
                                }
                            });


                            if (staffDetails.getPosition().equalsIgnoreCase("admin")) {

                                DocumentReference upDatePosition = db.collection("adminDetails").document("adminList")
                                        .collection("emailNotificationPreference").document(mAuth.getCurrentUser().getUid());
                                upDatePosition.set(emailNotification);

                                DocumentReference upDatePositionApp = db.collection("adminDetails").document("adminList")
                                        .collection("appNotificationPreference").document(mAuth.getCurrentUser().getUid());
                                upDatePositionApp.set(appNotification);

                            } else if (staffDetails.getPosition().equalsIgnoreCase("sales")) {
                                DocumentReference upDatePosition = db.collection("staffDetails").document("sales")
                                        .collection("sales").document(mAuth.getCurrentUser().getUid());
                                upDatePosition.set(dummyData);
                            } else {
                                DocumentReference upDatePosition = db.collection("staffDetails").document("technician")
                                        .collection("technician").document(mAuth.getCurrentUser().getUid());
                                upDatePosition.set(dummyData);
                            }


                            //Send the notice email to the staff
                            sendMailToStaff(email);

                            //Store the staff data into database
                            DocumentReference staffDetailsDB = db.collection("staffDetails").document(mAuth.getCurrentUser().getUid());
                            staffDetailsDB.set(staffDetails)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            String title = "New Staff Sign Up";
                                            String body = staffDetails.fullName() + " had signed up as " + staffDetails.getPosition() + ".";
                                            AppNotification appNotificationSend = new AppNotification();
                                            requestQueue.add(appNotificationSend.sendNotification("staffSignUp", title, body));

                                            if (staffDetails.getPosition().equalsIgnoreCase("admin")) {

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
                                            }

                                            loadingScreen();
                                            Toast.makeText(SignUp.this, "Signed up successfully", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                        } else {
                            //Show error message if sign up fail
                            Toast.makeText(SignUp.this, "Error" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * Send email to the admin to notify about the new staff sign up
     *
     * @param adminEmail the email of admin to email to
     * @param staffEmail the email of staff to email to
     */
    public void sendMailToAdmin(String adminEmail, String staffEmail) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss ");
        Date dt = new Date();
        String message;

        message = "A new staff had signed up at " + formatter.format(dt) + ". Here are all the details about this staff:\n\n"
                + "\tFirst Name: " + staffDetails.getfName() + "\n"
                + "\tLast Name: " + staffDetails.getlName() + "\n"
                + "\tEmail: " + staffEmail + "\n"
                + "\tMobile: " + staffDetails.getMobile() + "\n"
                + "\tBranch: " + staffDetails.getBranch() + "\n"
                + "\tPosition: " + staffDetails.getPosition() + "\n\n"
                + "If it's not an staff that you recognize please remove it through the admin page.";


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

        message = "Dear " + staffDetails.getfName() + ",\n\n"
                + "\tYou had sign up as a Ashita's staff at during " + formatter.format(dt) + ". Here are all the details about your sign up:\n\n"
                + "\t\tFirst Name: " + staffDetails.getfName() + "\n"
                + "\t\tLast Name: " + staffDetails.getlName() + "\n"
                + "\t\tEmail: " + email + "\n"
                + "\t\tMobile: " + staffDetails.getMobile() + "\n"
                + "\t\tBranch: " + staffDetails.getBranch() + "\n"
                + "\t\tPosition: " + staffDetails.getPosition() + "\n\n"
                + "\tPlease report to the admin if there's any mistake. Thank you and have a nice day.\n\n"
                + "Regards,\n"
                + "Ashita ";


        JavaMailAPI javaMailAPI = new JavaMailAPI(this, email,
                "Sign Up Details", message);

        javaMailAPI.execute();
    }

    /**
     * Display loading screen
     */
    public void loadingScreen() {
        ProgressBar progressBar;
        final Sprite style = new Wave();
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
                Intent intent = new Intent(SignUp.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
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
                Toast.makeText(SignUp.this, "Error please try again later", Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(SignUp.this, "Sign in successfully", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(SignUp.this, "Authentication failed.",
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
        Button branchLG, branchPB, branchPT;
        branchDialog.setContentView(R.layout.pop_up_branch);
        branchDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        branchClose = branchDialog.findViewById(R.id.img_close_branchPU);

        branchLG = branchDialog.findViewById(R.id.bt_lg_branchPU);

        branchPB = branchDialog.findViewById(R.id.bt_pb_branchPU);

        branchPT = branchDialog.findViewById(R.id.bt_pt_branchPU);


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

                    String title = "New Staff Sign Up";
                    String body = staffDetails.fullName() + " had signed up as " + staffDetails.getPosition() + ".";
                    AppNotification appNotificationSend = new AppNotification();
                    requestQueue.add(appNotificationSend.sendNotification("staffSignUp", title, body));

                    CollectionReference adminEmails = db.collection("adminDetails").document("adminList").
                            collection("emailNotificationPreference");
                    adminEmails.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                if (documentSnapshot.getBoolean("staffSignUp")) {
                                    sendMailToAdmin(documentSnapshot.getString("email"), sEmail);
                                }
                            }
                            Map<String, String> dummyData = new HashMap<>();
                            dummyData.put("placeHolder", "dummyData");

                            EmailNotification emailNotification = new EmailNotification();
                            emailNotification.setEmail(sEmail);

                            AppNotification appNotification = new AppNotification();

                            if (staffDetails.getPosition().equalsIgnoreCase("admin")) {

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

                            } else if (staffDetails.getPosition().equalsIgnoreCase("sales")) {
                                DocumentReference upDatePosition = db.collection("staffDetails").document("sales")
                                        .collection("sales").document(mAuth.getCurrentUser().getUid());
                                upDatePosition.set(dummyData);
                            } else {
                                DocumentReference upDatePosition = db.collection("staffDetails").document("technician")
                                        .collection("technician").document(mAuth.getCurrentUser().getUid());
                                upDatePosition.set(dummyData);
                            }
                        }
                    });

                    FirebaseInstanceId.getInstance().getInstanceId()
                            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    if (task.isSuccessful()) {
                                        token = task.getResult().getToken();
                                    }
                                }
                            });

                    staffDetails.setToken(token);
                    staffDetails.setEmail(sEmail);

                    DocumentReference staffDetailsDB = db.collection("staffDetails").document(mAuth.getCurrentUser().getUid());
                    staffDetailsDB.set(staffDetails)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {


                                    //Get the admin email and then send the notice email to the admin
                                    sendMailToStaff(sEmail);
                                    Toast.makeText(SignUp.this, "Signed up successfully", Toast.LENGTH_SHORT).show();
                                    loadingScreen();
                                }
                            });

                } else {

                    //Delete the user account of the user don't want to enter additional details
                    FirebaseUser user = mAuth.getCurrentUser();
                    mGoogleSignInClient.revokeAccess();
                    mAuth.signOut();
                    user.delete();
                    Toast.makeText(SignUp.this, "Sign up failed", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(SignUp.this, "Please select your branch", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (sPosition.isEmpty()) {
                    Toast.makeText(SignUp.this, "Please select your position", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!tNcGoogle.isChecked()) {
                    Toast.makeText(SignUp.this, "Please check the text box", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!verifiedAdmin && sPosition.equals("Admin")) {
                    Toast.makeText(SignUp.this, "Admin verification failed", Toast.LENGTH_SHORT).show();
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


        branchLG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sBranch = "LG";
                branchDialog.dismiss();
            }
        });


        branchPB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sBranch = "PB";
                branchDialog.dismiss();
            }
        });

        branchPT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sBranch = "LG";
                branchDialog.dismiss();
            }
        });

        branchDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (!sBranch.isEmpty()) {
                    branch.setText("  Branch " + sBranch);
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
                            Toast.makeText(SignUp.this, "Admin verified successfully", Toast.LENGTH_SHORT).show();
                            adminDialog.dismiss();
                        } else {
                            verifiedAdmin = false;
                            //Change the text message
                            verifyMessage.setText("(Verification Failed)");
                            Toast.makeText(SignUp.this, "Admin verification failed", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(SignUp.this, TermNCondition.class);
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


    /**
     * Capitalize the first letter of the staff name then returns it
     *
     * @param s the name of the staff
     * @return
     */
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

    /**
     * Open term and condition
     *
     * @param view
     */
    public void openTnC(View view) {
        Intent intent = new Intent(SignUp.this, TermNCondition.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     * Back button
     *
     * @param view
     */
    public void back(View view) {
        super.onBackPressed();
        finish();
    }

    /**
     * Redirect the user back to sign in page
     *
     * @param view
     */
    public void signIn(View view) {
        //Prevent repeating activities
        Intent intent = new Intent(SignUp.this, LoginPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    /**
     * hide keyboard
     */
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
