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

public class NotificationFragment extends Fragment {

    Switch signUpEmail, branchEmail, positionEmail, deletedEmail, startNavigationEmail, newFilterEmail,
            serviceDoneEmail, companyEmailEmail, adminVerificationEmail,
            identityVerificationEmail, moneyWithdrawEmail;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    EmailNotification emailNotification = new EmailNotification();

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
        moneyWithdrawEmail = v.findViewById(R.id.s_moneyWithDrawEmail_Notification);

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
                if (b)
                    emailNotification.setStaffSignUp(true);
                else
                    emailNotification.setStaffSignUp(false);
            }
        });

        branchEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    emailNotification.setChangeBranch(true);
                else
                    emailNotification.setChangeBranch(false);
            }
        });

        positionEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    emailNotification.setChangePosition(true);
                else
                    emailNotification.setChangePosition(false);
            }
        });


        return v;
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

        if (tempo.isMoneyWithdraw())
            moneyWithdrawEmail.setChecked(true);
        else
            moneyWithdrawEmail.setChecked(false);
    }

    private void storePreference(EmailNotification emailNotification) {
        this.emailNotification = emailNotification;
    }


}
