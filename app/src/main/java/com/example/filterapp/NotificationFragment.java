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

    Switch signUp, branch, position, deleted, startNavigation, newFilter,
            serviceDone, companyEmail, adminVerification,
            identityVerification, moneyWithdraw;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    EmailNotification emailNotification = new EmailNotification();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notification, container, false);
        signUp = v.findViewById(R.id.s_signUp_Notification);
        branch = v.findViewById(R.id.s_branch_Notification);
        position = v.findViewById(R.id.s_position_Notification);
        deleted = v.findViewById(R.id.s_deleted_Notification);
        startNavigation = v.findViewById(R.id.s_navigation_Notification);
        newFilter = v.findViewById(R.id.s_newFilter_Notification);
        serviceDone = v.findViewById(R.id.s_serviceDone_Notification);
        companyEmail = v.findViewById(R.id.s_companyEmail_Notification);
        adminVerification = v.findViewById(R.id.s_adminVerification_Notification);
        identityVerification = v.findViewById(R.id.s_identityVerification_Notification);
        moneyWithdraw = v.findViewById(R.id.s_moneyWithDraw_Notification);

        DocumentReference getPreference = db.collection("adminDetails").document("adminList").collection("notificationPreference")
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

        signUp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    emailNotification.setStaffSignUp(true);
                else
                    emailNotification.setStaffSignUp(false);
            }
        });

        branch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    emailNotification.setChangeBranch(true);
                else
                    emailNotification.setChangeBranch(false);
            }
        });

        position.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
            signUp.setChecked(true);
        else
            signUp.setChecked(false);

        if (tempo.isChangeBranch())
            branch.setChecked(true);
        else
            branch.setChecked(false);

        if (tempo.isChangePosition())
            position.setChecked(true);
        else
            position.setChecked(false);

        if (tempo.isCustomerDeleted())
            deleted.setChecked(true);
        else
            deleted.setChecked(false);

        if (tempo.isStaffStartNavigation())
            startNavigation.setChecked(true);
        else
            startNavigation.setChecked(false);

        if (tempo.isSoldNewFilter())
            newFilter.setChecked(true);
        else
            newFilter.setChecked(false);

        if (tempo.isServiceDone())
            serviceDone.setChecked(true);
        else
            serviceDone.setChecked(false);

        if (tempo.isCompanyEmailDetailsChanged())
            companyEmail.setChecked(true);
        else
            companyEmail.setChecked(false);

        if (tempo.isAdminVerificationPassChange())
            adminVerification.setChecked(true);
        else
            adminVerification.setChecked(false);

        if (tempo.isIdentityVerificationPassChange())
            identityVerification.setChecked(true);
        else
            identityVerification.setChecked(false);

        if (tempo.isMoneyWithdraw())
            moneyWithdraw.setChecked(true);
        else
            moneyWithdraw.setChecked(false);
    }

    private void storePreference(EmailNotification emailNotification) {
        this.emailNotification = emailNotification;
    }


}
