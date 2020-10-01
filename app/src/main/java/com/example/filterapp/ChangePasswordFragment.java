package com.example.filterapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.filterapp.classes.JavaMailAPI;
import com.example.filterapp.classes.UserDetails;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.filterapp.MainActivity.getStaffDetailsStatic;

public class ChangePasswordFragment extends Fragment {
    EditText mEmail;
    String emailPassword, identityPassword, adminPassword;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String changedEmail, changedEmailPassword, changedIdentity, changedAdmin;

    boolean emailPasswordChangedBool = false;
    boolean identityPasswordChangedBool = false;
    boolean adminPasswordChangedBool = false;
    boolean emailChangedBool = false;

    Dialog changePasswordDialog, adminDialog, identityDialog, emailDialog;
    TextView mAdmin, mPassword, mIdentity;
    Button update;

    boolean showPasswordPIC = false, showPasswordPIC2 = false;
    boolean adminShowPassword = false, emailShowPassword = false, identityShowPassword = false;

    Map<String, Object> newDetails = new HashMap<>();

    RequestQueue requestQueue1;
    RequestQueue requestQueue2;
    RequestQueue requestQueue3;
    RequestQueue requestQueue4;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_password, container, false);
        mEmail = v.findViewById(R.id.et_email_changePassword);

        changePasswordDialog = new Dialog(getContext());
        adminDialog = new Dialog(getContext());
        identityDialog = new Dialog(getContext());
        emailDialog = new Dialog(getContext());
        mAdmin = v.findViewById(R.id.tv_admin_changePassword);
        mPassword = v.findViewById(R.id.tv_password_changePassword);
        mIdentity = v.findViewById(R.id.tv_verification_changePassword);
        update = v.findViewById(R.id.bt_update_changePassword);

        requestQueue1 = Volley.newRequestQueue(getContext());
        requestQueue2 = Volley.newRequestQueue(getContext());
        requestQueue3 = Volley.newRequestQueue(getContext());
        requestQueue4 = Volley.newRequestQueue(getContext());

        DocumentReference getDetails = db.collection("adminDetails").document("loginDetails");
        getDetails.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                mEmail.setText(documentSnapshot.getString("email"));
                String password = documentSnapshot.getString("password");
                String changeDetailsPassword = documentSnapshot.getString("changeDetailsPassword");
                String verificationPassword = documentSnapshot.getString("verificationPassword");
                storePassword(password, changeDetailsPassword, verificationPassword);
            }
        });

        mPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpPassword();
            }
        });

        mIdentity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                identityPopUp();
            }
        });

        mAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpAdmin();
            }
        });
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                emailChangedBool = true;
                changedEmail = mEmail.getText().toString().trim();
            }
        };

        mEmail.addTextChangedListener(textWatcher);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDetails();
            }
        });

        return v;
    }

    public void popUpPassword() {
        emailPasswordChangedBool = false;
        TextView messagae;
        final EditText password;
        Button done;
        final ImageView closeBt, showPassword;
        emailDialog.setContentView(R.layout.pop_up_admin_password_check);
        emailDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        messagae = emailDialog.findViewById(R.id.tv_message_adminPU);
        password = emailDialog.findViewById(R.id.et_enterCode_adminPU);
        done = emailDialog.findViewById(R.id.bt_done_adminPU);
        closeBt = emailDialog.findViewById(R.id.img_close_adminPU);
        showPassword = emailDialog.findViewById(R.id.img_showPassword_adminPU);
        emailDialog.show();

        closeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailDialog.dismiss();
            }
        });

        emailDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (!emailPasswordChangedBool) {
                    Toast.makeText(getActivity(), "Fail to update password", Toast.LENGTH_SHORT).show();
                    mPassword.setText("  ******");
                }
            }
        });

        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!emailShowPassword) {
                    showPassword.setImageResource(R.drawable.hide_password_icon);
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    password.setSelection(password.getText().length());
                    emailShowPassword = true;
                } else {
                    showPassword.setImageResource(R.drawable.show_password_icon);
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    password.setSelection(password.getText().length());
                    emailShowPassword = false;
                }
            }
        });


        messagae.setText("Please enter old email password to proceed.");
        password.setHint("  Enter old email password");
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getText().toString().equals(emailPassword)) {
                    emailPasswordChangedBool = true;
                    emailDialog.dismiss();
                    popUpChangePassword(0);
                } else
                    emailDialog.dismiss();
            }
        });
    }

    public void identityPopUp() {
        identityPasswordChangedBool = false;
        TextView messagae;
        final EditText password;
        Button done;
        final ImageView closeBt, showPassword;
        identityDialog.setContentView(R.layout.pop_up_admin_password_check);
        identityDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        messagae = identityDialog.findViewById(R.id.tv_message_adminPU);
        password = identityDialog.findViewById(R.id.et_enterCode_adminPU);
        done = identityDialog.findViewById(R.id.bt_done_adminPU);
        closeBt = identityDialog.findViewById(R.id.img_close_adminPU);
        showPassword = identityDialog.findViewById(R.id.img_showPassword_adminPU);
        identityDialog.show();

        closeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                identityDialog.dismiss();
            }
        });

        identityDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (!identityPasswordChangedBool) {
                    Toast.makeText(getActivity(), "Fail to update password", Toast.LENGTH_SHORT).show();
                    mIdentity.setText("  ******");
                }
            }
        });

        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!identityShowPassword) {
                    showPassword.setImageResource(R.drawable.hide_password_icon);
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    password.setSelection(password.getText().length());
                    identityShowPassword = true;
                } else {
                    showPassword.setImageResource(R.drawable.show_password_icon);
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    password.setSelection(password.getText().length());
                    identityShowPassword = false;
                }
            }
        });


        messagae.setText("Please enter old security code to proceed.");
        password.setHint("  Enter old security code");
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getText().toString().equals(identityPassword)) {
                    identityPasswordChangedBool = true;
                    identityDialog.dismiss();
                    popUpChangePassword(1);
                } else
                    identityDialog.dismiss();
            }
        });
    }

    public void popUpAdmin() {
        adminPasswordChangedBool = false;
        TextView messagae;
        final EditText password;
        Button done;
        final ImageView closeBt, showPassword;
        adminDialog.setContentView(R.layout.pop_up_admin_password_check);
        adminDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        messagae = adminDialog.findViewById(R.id.tv_message_adminPU);
        password = adminDialog.findViewById(R.id.et_enterCode_adminPU);
        done = adminDialog.findViewById(R.id.bt_done_adminPU);
        closeBt = adminDialog.findViewById(R.id.img_close_adminPU);
        showPassword = adminDialog.findViewById(R.id.img_showPassword_adminPU);
        adminDialog.show();

        closeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminDialog.dismiss();
            }
        });

        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!adminShowPassword) {
                    showPassword.setImageResource(R.drawable.hide_password_icon);
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    password.setSelection(password.getText().length());
                    adminShowPassword = true;
                } else {
                    showPassword.setImageResource(R.drawable.show_password_icon);
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    password.setSelection(password.getText().length());
                    adminShowPassword = false;
                }
            }
        });

        adminDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (!adminPasswordChangedBool) {
                    Toast.makeText(getActivity(), "Fail to update password", Toast.LENGTH_SHORT).show();
                    mAdmin.setText("  ******");
                }
            }
        });

        messagae.setText("Please enter old admin code to proceed.");
        password.setHint("  Enter old admin code");
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getText().toString().equals(adminPassword)) {
                    adminPasswordChangedBool = true;
                    adminDialog.dismiss();
                    popUpChangePassword(2);
                } else
                    adminDialog.dismiss();
            }
        });
    }

    private void popUpChangePassword(final int choice) {
        final ImageView closeBt, showPassword, showPassword2;
        final EditText mPassword1, mPassword2;
        Button updateBt;
        final TextView title;

        changePasswordDialog.setContentView(R.layout.pop_up_change_password);
        changePasswordDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        closeBt = changePasswordDialog.findViewById(R.id.img_close_passwordPU);
        showPassword = changePasswordDialog.findViewById(R.id.img_showPassword_passwordPU);
        showPassword2 = changePasswordDialog.findViewById(R.id.img_showPassword2_passwordPU);
        mPassword1 = changePasswordDialog.findViewById(R.id.et_password_passwordPU);
        mPassword2 = changePasswordDialog.findViewById(R.id.et_password2_passwordPU);
        updateBt = changePasswordDialog.findViewById(R.id.bt_update_passwordPU);
        title = changePasswordDialog.findViewById(R.id.tv_title_passwordPU);

        switch (choice) {
            case 0:
                title.setText("Update Email Password");
                break;

            case 1:
                title.setText("Update Identity Password");
                break;

            case 2:
                title.setText("Update Admin Password");
                break;
        }

        changePasswordDialog.show();

        closeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePasswordDialog.dismiss();
            }
        });

        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!showPasswordPIC) {
                    showPassword.setImageResource(R.drawable.hide_password_icon);
                    mPassword1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mPassword1.setSelection(mPassword1.getText().length());
                    showPasswordPIC = true;
                } else {
                    showPassword.setImageResource(R.drawable.show_password_icon);
                    mPassword1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mPassword1.setSelection(mPassword1.getText().length());
                    showPasswordPIC = false;
                }
            }
        });

        showPassword2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!showPasswordPIC2) {
                    showPassword2.setImageResource(R.drawable.hide_password_icon);
                    mPassword2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mPassword2.setSelection(mPassword2.getText().length());
                    showPasswordPIC2 = true;
                } else {
                    showPassword2.setImageResource(R.drawable.show_password_icon);
                    mPassword2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mPassword2.setSelection(mPassword2.getText().length());
                    showPasswordPIC2 = false;
                }
            }
        });

        updateBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password1, password2;

                password1 = mPassword1.getText().toString().trim();
                password2 = mPassword2.getText().toString().trim();
                if (password1.isEmpty()) {
                    mPassword1.setError("Password cannot be empty");
                    mPassword1.requestFocus();
                    return;
                }

                if (password1.length() < 6) {
                    mPassword1.setError("Password need to be more than 6 character");
                    mPassword1.requestFocus();
                    return;
                }

                if (!password2.equals(password1)) {
                    mPassword2.setError("Password doesn't matches");
                    mPassword2.requestFocus();
                    return;
                }

                switch (choice) {

                    case 0:
                        mPassword.setText("  " + password1);
                        changedEmailPassword = password1;
                        break;

                    case 1:
                        mIdentity.setText("  " + password1);
                        changedIdentity = password1;
                        break;

                    case 2:
                        mAdmin.setText("  " + password1);
                        changedAdmin = password1;
                        break;
                }

                changePasswordDialog.dismiss();

            }
        });


    }

    private void storePassword(String password, String changeDetailsPassword, String verificationPassword) {
        emailPassword = password;
        identityPassword = changeDetailsPassword;
        adminPassword = verificationPassword;
    }

    public void updateDetails() {
        if (!adminPasswordChangedBool && !emailChangedBool
                && !emailPasswordChangedBool && !identityPasswordChangedBool) {
            Toast.makeText(getActivity(), "Nothing Changed", Toast.LENGTH_LONG).show();
        } else {

            CollectionReference adminEmails = db.collection("adminDetails").document("adminList").collection("notificationPreference");
            adminEmails.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        if (documentSnapshot.getBoolean("positionChangeEmail")) {
                            if (adminPasswordChangedBool)
                                adminPasswordChangedEmail(documentSnapshot.getString("email"));

                            if (emailChangedBool)
                                emailChangedEmail(documentSnapshot.getString("email"));

                            if (emailPasswordChangedBool)
                                emailPasswordChangeEmail(documentSnapshot.getString("email"));

                            if (identityPasswordChangedBool)
                                identityPasswordChangedEmail(documentSnapshot.getString("email"));
                        }
                    }
                }
            });
            if (adminPasswordChangedBool) {
                newDetails.put("verificationPassword", changedAdmin);
                mAdmin.setText("  ******");
                String title = "Admin Password Changed";
                String body = getStaffDetailsStatic().fullName() + " changed admin verification password";
                UserDetails.AppNotification sendAppNotification = new UserDetails.AppNotification();
                requestQueue1.add(sendAppNotification.sendNotification("adminVerificationPassChange", title, body));
            }

            if (emailChangedBool) {
                newDetails.put("email", changedEmail);
                mEmail.setText("  " + changedEmail);

                String title = "Company Email Changed";
                String body = getStaffDetailsStatic().fullName() + " changed company email";
                UserDetails.AppNotification sendAppNotification = new UserDetails.AppNotification();
                requestQueue2.add(sendAppNotification.sendNotification("companyEmailDetailsChanged", title, body));
            }

            if (emailPasswordChangedBool) {
                newDetails.put("password", changedEmailPassword);
                mPassword.setText("  ******");

                String title = "Company Email's Password Changed";
                String body = getStaffDetailsStatic().fullName() + " changed company email's password";
                UserDetails.AppNotification sendAppNotification = new UserDetails.AppNotification();
                requestQueue1.add(sendAppNotification.sendNotification("companyEmailDetailsChanged", title, body));
            }
            if (identityPasswordChangedBool) {
                newDetails.put("changeDetailsPassword", changedIdentity);
                mIdentity.setText("  ******");

                String title = "Identity verification Password Changed";
                String body = getStaffDetailsStatic().fullName() + " changed identity verification password";
                UserDetails.AppNotification sendAppNotification = new UserDetails.AppNotification();
                requestQueue1.add(sendAppNotification.sendNotification("identityVerificationPassChange", title, body));
            }

        }
        DocumentReference getDetails = db.collection("adminDetails").document("loginDetails");
        getDetails.update(newDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(), "Nothing Changed", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error, fail to update details", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void identityPasswordChangedEmail(String email) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss ");
        Date dt = new Date();
        String message;

        message = getStaffDetailsStatic().fullName() + " changed the identity verification password at" + formatter.format(dt) + ".\n"
                + "Identity verification password can be updated directly from the admin pages.";

        JavaMailAPI javaMailAPI = new JavaMailAPI(getContext(), email,
                "Identity verification Password Changed", message);

        javaMailAPI.execute();
    }

    private void emailPasswordChangeEmail(String email) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss ");
        Date dt = new Date();
        String message;

        message = getStaffDetailsStatic().fullName() + " changed the company email's password at" + formatter.format(dt) + ".\n"
                + "Identity verification password can be updated directly from the admin pages.";

        JavaMailAPI javaMailAPI = new JavaMailAPI(getContext(), email,
                "Company Email's Password Changed", message);

        javaMailAPI.execute();
    }

    private void adminPasswordChangedEmail(String email) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss ");
        Date dt = new Date();
        String message;

        message = getStaffDetailsStatic().fullName() + " changed the admin verification password at" + formatter.format(dt) + ".\n"
                + "Identity verification password can be updated directly from the admin pages.";

        JavaMailAPI javaMailAPI = new JavaMailAPI(getContext(), email,
                "Admin Password Changed", message);

        javaMailAPI.execute();
    }

    private void emailChangedEmail(String email) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss ");
        Date dt = new Date();
        String message;

        message = getStaffDetailsStatic().fullName() + " changed company's email at" + formatter.format(dt) + ".\n"
                + "Identity verification password can be updated directly from the admin pages.";


        JavaMailAPI javaMailAPI = new JavaMailAPI(getContext(), email,
                "Company Email Changed", message);

        javaMailAPI.execute();
    }


}