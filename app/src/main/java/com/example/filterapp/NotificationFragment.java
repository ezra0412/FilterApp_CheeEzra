package com.example.filterapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class NotificationFragment extends Fragment {

    Switch signUpEmail, branchEmail, positionEmail, deletedEmail, startNavigationEmail, newFilterEmail,
            serviceDoneEmail, companyEmailEmail, adminVerificationEmail,
            identityVerificationEmail, staffDeletedEmail;

    Switch signUpApp, branchApp, positionApp, deletedApp, startNavigationApp, newFilterApp,
            serviceDoneApp, companyEmailApp, adminVerificationApp,
            identityVerificationApp, staffDeletedApp;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    EmailNotification emailNotification = new EmailNotification();
    AppNotification appNotification = new AppNotification();

    FirebaseMessaging fm = FirebaseMessaging.getInstance();

    boolean updateEmail = false;
    boolean updateApp = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notification, container, false);
        signUpEmail = v.findViewById(R.id.s_signUpEmail_Notification);
        branchEmail = v.findViewById(R.id.s_branchEmail_Notification);
        positionEmail = v.findViewById(R.id.s_positionEmail_Notification);
        deletedEmail = v.findViewById(R.id.s_deletedEmail_Notification);
        startNavigationEmail = v.findViewById(R.id.s_navigationEmail_Notification);
        newFilterEmail = v.findViewById(R.id.s_newFilterEmail_Notification);
        serviceDoneEmail = v.findViewById(R.id.s_serviceDoneEmail_Notification);
        companyEmailEmail = v.findViewById(R.id.s_companyEmailEmail_Notification);
        adminVerificationEmail = v.findViewById(R.id.s_adminVerificationEmail_Notification);
        identityVerificationEmail = v.findViewById(R.id.s_identityVerificationEmail_Notification);
        staffDeletedEmail = v.findViewById(R.id.s_staffDeleteEmail_Notification);

        signUpApp = v.findViewById(R.id.s_signUpApp_Notification);
        branchApp = v.findViewById(R.id.s_branchApp_notification);
        positionApp = v.findViewById(R.id.s_positionApp_notification);
        deletedApp = v.findViewById(R.id.s_deleteApp_notification);
        startNavigationApp = v.findViewById(R.id.s_navigationApp_notification);
        newFilterApp = v.findViewById(R.id.s_newFilterApp_notification);
        serviceDoneApp = v.findViewById(R.id.s_serviceDoneApp_notification);
        companyEmailApp = v.findViewById(R.id.s_companyEmailApp_notification);
        adminVerificationApp = v.findViewById(R.id.s_adminVerificationApp_notification);
        identityVerificationApp = v.findViewById(R.id.s_identitiyVerificationApp_notification);
        staffDeletedApp = v.findViewById(R.id.s_staffDeletedApp_notification);

        DocumentReference getPreference = db.collection("adminDetails").document("adminList").collection("emailNotificationPreference")
                .document(mAuth.getCurrentUser().getUid());
        getPreference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                EmailNotification tempo;
                tempo = documentSnapshot.toObject(EmailNotification.class);
                storePreference(tempo);
                setPreference(tempo);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        signUpEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateEmail = true;

                if (b)
                    emailNotification.setStaffSignUp(true);
                else
                    emailNotification.setStaffSignUp(false);
            }
        });

        branchEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateEmail = true;

                if (b)
                    emailNotification.setChangeBranch(true);
                else
                    emailNotification.setChangeBranch(false);
            }
        });

        positionEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateEmail = true;

                if (b)
                    emailNotification.setChangePosition(true);
                else
                    emailNotification.setChangePosition(false);
            }
        });

        deletedEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateEmail = true;

                if (b)
                    emailNotification.setCustomerDeleted(true);
                else
                    emailNotification.setCustomerDeleted(false);
            }
        });

        startNavigationEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateEmail = true;

                if (b)
                    emailNotification.setStaffStartNavigation(true);
                else
                    emailNotification.setStaffStartNavigation(false);
            }
        });

        newFilterEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateEmail = true;

                if (b)
                    emailNotification.setSoldNewFilter(true);
                else
                    emailNotification.setSoldNewFilter(false);
            }
        });

        serviceDoneEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateEmail = true;

                if (b)
                    emailNotification.setServiceDone(true);
                else
                    emailNotification.setServiceDone(false);
            }
        });

        companyEmailEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateEmail = true;

                if (b)
                    emailNotification.setCompanyEmailDetailsChanged(true);
                else
                    emailNotification.setCompanyEmailDetailsChanged(false);
            }
        });

        adminVerificationEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateEmail = true;

                if (b)
                    emailNotification.setAdminVerificationPassChange(true);
                else
                    emailNotification.setAdminVerificationPassChange(false);
            }
        });

        identityVerificationEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateEmail = true;

                if (b)
                    emailNotification.setIdentityVerificationPassChange(true);
                else
                    emailNotification.setIdentityVerificationPassChange(false);
            }
        });

        staffDeletedEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateEmail = true;

                if (b)
                    emailNotification.setStaffDeleted(true);
                else
                    emailNotification.setStaffDeleted(false);
            }
        });

        signUpApp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateApp = true;

                if (b) {
                    appNotification.setStaffSignUp(true);
                    fm.subscribeToTopic("staffSignUp");
                } else {
                    appNotification.setStaffSignUp(false);
                    fm.unsubscribeFromTopic("staffSignUp");
                }
            }
        });

        branchApp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateApp = true;

                if (b) {
                    appNotification.setChangeBranch(true);
                    fm.subscribeToTopic("changeBranch");
                } else {
                    appNotification.setChangeBranch(false);
                    fm.unsubscribeFromTopic("changeBranch");
                }
            }
        });

        positionApp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateApp = true;

                if (b) {
                    appNotification.setChangePosition(true);
                    fm.subscribeToTopic("changePosition");

                } else {
                    appNotification.setChangePosition(false);
                    fm.unsubscribeFromTopic("changePosition");
                }
            }
        });

        deletedApp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateApp = true;

                if (b) {
                    appNotification.setCustomerDeleted(true);
                    fm.subscribeToTopic("customerDeleted");
                } else {
                    appNotification.setCustomerDeleted(false);
                    fm.unsubscribeFromTopic("customerDeleted");
                }
            }
        });

        startNavigationApp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateApp = true;

                if (b) {
                    appNotification.setStaffStartNavigation(true);
                    fm.subscribeToTopic("staffStartNavigation");
                } else {
                    appNotification.setStaffStartNavigation(false);
                    fm.unsubscribeFromTopic("staffStartNavigation");
                }
            }
        });

        newFilterApp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateApp = true;

                if (b) {
                    appNotification.setSoldNewFilter(true);
                    fm.subscribeToTopic("soldNewFilter");
                } else {
                    appNotification.setSoldNewFilter(false);
                    fm.unsubscribeFromTopic("soldNewFilter");
                }
            }
        });

        serviceDoneApp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateApp = true;

                if (b) {
                    appNotification.setServiceDone(true);
                    fm.subscribeToTopic("serviceDone");
                } else {
                    appNotification.setServiceDone(false);
                    fm.unsubscribeFromTopic("serviceDone");
                }
            }
        });

        companyEmailApp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateApp = true;

                if (b) {
                    appNotification.setCompanyEmailDetailsChanged(true);
                    fm.subscribeToTopic("companyEmailDetailsChanged");
                } else {
                    appNotification.setCompanyEmailDetailsChanged(false);
                    fm.unsubscribeFromTopic("companyEmailDetailsChanged");
                }
            }
        });

        adminVerificationApp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateApp = true;

                if (b) {
                    appNotification.setAdminVerificationPassChange(true);
                    fm.subscribeToTopic("adminVerificationPassChange");

                } else {
                    appNotification.setAdminVerificationPassChange(false);
                    fm.unsubscribeFromTopic("adminVerificationPassChange");
                }
            }
        });

        identityVerificationApp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateApp = true;

                if (b) {
                    appNotification.setIdentityVerificationPassChange(true);
                    fm.subscribeToTopic("identityVerificationPassChange");
                } else {
                    appNotification.setIdentityVerificationPassChange(false);
                    fm.unsubscribeFromTopic("identityVerificationPassChange");
                }
            }
        });

        staffDeletedApp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateApp = true;

                if (b) {
                    appNotification.setStaffDeleted(true);
                    fm.subscribeToTopic("staffDeleted");
                } else {
                    appNotification.setStaffDeleted(false);
                    fm.unsubscribeFromTopic("staffDeleted");
                }
            }
        });


        DocumentReference getPreferenceApp = db.collection("adminDetails").document("adminList").collection("appNotificationPreference")
                .document(mAuth.getCurrentUser().getUid());
        getPreferenceApp.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                AppNotification tempo;
                tempo = documentSnapshot.toObject(AppNotification.class);
                setPreferenceApp(tempo);
                storePreferenceApp(tempo);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


        return v;
    }

    private void storePreferenceApp(AppNotification tempo) {
        appNotification = tempo;
    }


    private void setPreferenceApp(AppNotification tempo) {
        if (tempo.isStaffSignUp())
            signUpApp.setChecked(true);
        else
            signUpApp.setChecked(false);

        if (tempo.isChangeBranch())
            branchApp.setChecked(true);
        else
            branchApp.setChecked(false);

        if (tempo.isChangePosition())
            positionApp.setChecked(true);
        else
            positionApp.setChecked(false);

        if (tempo.isCustomerDeleted())
            deletedApp.setChecked(true);
        else
            deletedApp.setChecked(false);

        if (tempo.isStaffStartNavigation())
            startNavigationApp.setChecked(true);
        else
            startNavigationApp.setChecked(false);

        if (tempo.isSoldNewFilter())
            newFilterApp.setChecked(true);
        else
            newFilterApp.setChecked(false);

        if (tempo.isServiceDone())
            serviceDoneApp.setChecked(true);
        else
            serviceDoneApp.setChecked(false);

        if (tempo.isCompanyEmailDetailsChanged())
            companyEmailApp.setChecked(true);
        else
            companyEmailApp.setChecked(false);

        if (tempo.isAdminVerificationPassChange())
            adminVerificationApp.setChecked(true);
        else
            adminVerificationApp.setChecked(false);

        if (tempo.isIdentityVerificationPassChange())
            identityVerificationApp.setChecked(true);
        else
            identityVerificationApp.setChecked(false);

        if (tempo.isStaffDeleted())
            staffDeletedApp.setChecked(true);
        else
            staffDeletedApp.setChecked(false);
    }

    private void setPreference(EmailNotification tempo) {
        if (tempo.isStaffSignUp())
            signUpEmail.setChecked(true);
        else
            signUpEmail.setChecked(false);

        if (tempo.isChangeBranch())
            branchEmail.setChecked(true);
        else
            branchEmail.setChecked(false);

        if (tempo.isChangePosition())
            positionEmail.setChecked(true);
        else
            positionEmail.setChecked(false);

        if (tempo.isCustomerDeleted())
            deletedEmail.setChecked(true);
        else
            deletedEmail.setChecked(false);

        if (tempo.isStaffStartNavigation())
            startNavigationEmail.setChecked(true);
        else
            startNavigationEmail.setChecked(false);

        if (tempo.isSoldNewFilter())
            newFilterEmail.setChecked(true);
        else
            newFilterEmail.setChecked(false);

        if (tempo.isServiceDone())
            serviceDoneEmail.setChecked(true);
        else
            serviceDoneEmail.setChecked(false);

        if (tempo.isCompanyEmailDetailsChanged())
            companyEmailEmail.setChecked(true);
        else
            companyEmailEmail.setChecked(false);

        if (tempo.isAdminVerificationPassChange())
            adminVerificationEmail.setChecked(true);
        else
            adminVerificationEmail.setChecked(false);

        if (tempo.isIdentityVerificationPassChange())
            identityVerificationEmail.setChecked(true);
        else
            identityVerificationEmail.setChecked(false);

        if (tempo.isStaffDeleted())
            staffDeletedEmail.setChecked(true);
        else
            staffDeletedEmail.setChecked(false);
    }

    private void storePreference(EmailNotification emailNotification) {
        this.emailNotification = emailNotification;
    }

    @Override
    public void onPause() {
        super.onPause();

        if (updateEmail) {
            DocumentReference setPreference = db.collection("adminDetails").document("adminList").collection("emailNotificationPreference")
                    .document(mAuth.getCurrentUser().getUid());

            setPreference.set(emailNotification);
        }

        if (updateApp) {
            DocumentReference setPreferenceApp = db.collection("adminDetails").document("adminList").collection("appNotificationPreference")
                    .document(mAuth.getCurrentUser().getUid());

            setPreferenceApp.set(appNotification);
        }

    }

    @Override
    public void onStop() {
        super.onStop();

        if (updateEmail) {
            DocumentReference setPreference = db.collection("adminDetails").document("adminList").collection("emailNotificationPreference")
                    .document(mAuth.getCurrentUser().getUid());

            setPreference.set(emailNotification);
        }

        if (updateApp) {
            DocumentReference setPrefernceApp = db.collection("adminDetails").document("adminList").collection("appNotificationPreference")
                    .document(mAuth.getCurrentUser().getUid());

            setPrefernceApp.set(appNotification);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (updateEmail) {
            DocumentReference setPreference = db.collection("adminDetails").document("adminList").collection("emailNotificationPreference")
                    .document(mAuth.getCurrentUser().getUid());

            setPreference.set(emailNotification);
        }

        if (updateApp) {
            DocumentReference setPreferenceApp = db.collection("adminDetails").document("adminList").collection("appNotificationPreference")
                    .document(mAuth.getCurrentUser().getUid());

            setPreferenceApp.set(appNotification);
        }
    }
}
