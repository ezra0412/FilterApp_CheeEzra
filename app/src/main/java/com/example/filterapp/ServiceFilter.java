package com.example.filterapp;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import com.example.filterapp.classes.FilterDetails;
import com.example.filterapp.classes.ServiceDetails;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.example.filterapp.FilterDetail.imageBit;
import static com.example.filterapp.FilterDetail.serviced;


public class ServiceFilter extends AppCompatActivity {
    public static final int SELECT_PICTURE = 1;
    String currentPhotoPath;
    ImageView filterImg;
    TextView mFilter1, mFilter2, mFilter3, mFilter4, mFilter5;
    TextView message;
    EditText mWT, mWTS, mFCD, mServicePrice, mNote;
    boolean changedFilter = true, changeSparePart = true, uploadPicture = false;
    String documentID;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FilterDetails filterDetails = new FilterDetails();
    boolean filter1Bool = false, filter2Bool = false, filter3Bool = false, filter4Bool = false, filter5Bool = false;
    Dialog filterDialog, filterCDialog, filterMDialog, filterPDialog;
    String chosenFilter = "";
    int counter = 5;
    ConstraintLayout constrainLayout;
    Bitmap bitmap;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_filter);
        loadingDialog = new Dialog(this);
        filterImg = findViewById(R.id.img_filter_serviceFilter);
        mFilter1 = findViewById(R.id.tv_changedFilter1_serviceFIlter);
        mFilter2 = findViewById(R.id.tv_changedFilter2_serviceFIlter);
        mFilter3 = findViewById(R.id.tv_changedFilter3_serviceFIlter);
        mFilter4 = findViewById(R.id.tv_changedFilter4_serviceFIlter);
        mFilter5 = findViewById(R.id.tv_changedFilter5_serviceFIlter);

        message = findViewById(R.id.tv_message_serviceFilter);

        mWT = findViewById(R.id.et_wt_filterService);
        mWTS = findViewById(R.id.et_wts_filterService);
        mFCD = findViewById(R.id.et_fcd_filterService);
        mServicePrice = findViewById(R.id.et_servicePrice_servicePrice);
        mNote = findViewById(R.id.et_note_serviceFilter);

        filterDialog = new Dialog(this);
        filterCDialog = new Dialog(this);
        filterMDialog = new Dialog(this);
        filterPDialog = new Dialog(this);
        documentID = getIntent().getStringExtra("documentID");
        String year = documentID.substring(0, 4);
        String month = documentID.substring(4, 7);
        serviced = false;
        constrainLayout = findViewById(R.id.cl_serviceFilter);

        DocumentReference getFilterDetails = db.collection("sales").document(year).collection(month).document(documentID);
        getFilterDetails.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                storeFilterDetails(documentSnapshot.toObject(FilterDetails.class));

                FilterDetails filterDetail2 = documentSnapshot.toObject(FilterDetails.class);

                if (filterDetail2.getFilter1().equalsIgnoreCase("-"))
                    counter--;

                if (filterDetail2.getFilter2().equalsIgnoreCase("-"))
                    counter--;

                if (filterDetail2.getFilter3().equalsIgnoreCase("-"))
                    counter--;

                if (filterDetail2.getFilter4().equalsIgnoreCase("-"))
                    counter--;

                if (filterDetail2.getFilter5().equalsIgnoreCase("-"))
                    counter--;
            }
        });
    }

    private void storeFilterDetails(FilterDetails filterDetails) {
        this.filterDetails = filterDetails;
    }

    public void filter1(View view) {
        mFilter1.setBackground(getDrawable(R.drawable.spinner_open));
        choseFilters(1);
    }

    public void filter2(View view) {
        if (filterDetails.getFilter2().equalsIgnoreCase("-"))
            Toast.makeText(ServiceFilter.this, "This model only have " + counter + " filter", Toast.LENGTH_SHORT).show();
        else {
            mFilter2.setBackground(getDrawable(R.drawable.spinner_open));
            choseFilters(2);
        }
    }

    public void filter3(View view) {
        if (filterDetails.getFilter3().equalsIgnoreCase("-"))
            Toast.makeText(ServiceFilter.this, "This model only have " + counter + " filters", Toast.LENGTH_SHORT).show();
        else {
            mFilter3.setBackground(getDrawable(R.drawable.spinner_open));
            choseFilters(3);
        }
    }

    public void filter4(View view) {
        if (filterDetails.getFilter3().equalsIgnoreCase("-"))
            Toast.makeText(ServiceFilter.this, "This model only have " + counter + " filters", Toast.LENGTH_SHORT).show();
        else {
            mFilter4.setBackground(getDrawable(R.drawable.spinner_open));
            choseFilters(4);
        }

    }

    public void filter5(View view) {
        if (filterDetails.getFilter3().equalsIgnoreCase("-"))
            Toast.makeText(ServiceFilter.this, "This model only have " + counter + " filters", Toast.LENGTH_SHORT).show();
        else {
            mFilter5.setBackground(getDrawable(R.drawable.spinner_open));
            choseFilters(5);
        }


    }

    public void back(View view) {
        if (!filter1Bool && !filter2Bool && !filter3Bool && !filter4Bool && !filter5Bool) {
            changedFilter = false;
        }

        if (mWT.getText().toString().trim().isEmpty() && mWTS.getText().toString().isEmpty() && mFCD.getText().toString().trim().isEmpty()) {
            changeSparePart = false;
        }

        if (changedFilter || changeSparePart || uploadPicture) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final AlertDialog dialog = builder.setMessage("Details will not be saved, are you sure you want to go back?")
                    .setTitle("Confirmation")
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ServiceFilter.super.onBackPressed();
                            finish();
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).setIcon(getDrawable(R.drawable.ic_warning))
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
        } else
            super.onBackPressed();
    }

    public void choseFilters(final int selectLocation) {
        filterDialog.setContentView(R.layout.pop_up_filter);
        filterDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final ImageView filterClose;
        Button typeC, typeM, typeP, remove;
        filterClose = filterDialog.findViewById(R.id.img_close_popUpFilter);
        typeC = filterDialog.findViewById(R.id.bt_c_filter);
        typeM = filterDialog.findViewById(R.id.bt_m_filter);
        typeP = filterDialog.findViewById(R.id.bt_p_filter);
        remove = filterDialog.findViewById(R.id.bt_remove_filter);
        filterClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterDialog.dismiss();
            }
        });

        typeC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterCDialog.show();
            }
        });

        typeM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterMDialog.show();
            }
        });

        typeP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterPDialog.show();
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (selectLocation) {
                    case 1:
                        if (mFilter1.getText().toString().trim().isEmpty())
                            Toast.makeText(ServiceFilter.this, "Nothing to be removed", Toast.LENGTH_SHORT).show();
                        else {
                            filter1Bool = false;
                            mFilter1.setText("");
                            Toast.makeText(ServiceFilter.this, "Filter Removed", Toast.LENGTH_SHORT).show();
                            filterDialog.dismiss();
                        }
                        break;

                    case 2:
                        if (mFilter2.getText().toString().trim().isEmpty())
                            Toast.makeText(ServiceFilter.this, "Nothing to be removed", Toast.LENGTH_SHORT).show();
                        else {
                            filter2Bool = false;
                            mFilter2.setText("");
                            Toast.makeText(ServiceFilter.this, "Filter Removed", Toast.LENGTH_SHORT).show();
                            filterDialog.dismiss();
                        }
                        break;

                    case 3:
                        if (mFilter3.getText().toString().trim().isEmpty())
                            Toast.makeText(ServiceFilter.this, "Nothing to be removed", Toast.LENGTH_SHORT).show();
                        else {
                            filter3Bool = false;
                            mFilter3.setText("");
                            Toast.makeText(ServiceFilter.this, "Filter Removed", Toast.LENGTH_SHORT).show();
                            filterDialog.dismiss();
                        }
                        break;

                    case 4:
                        if (mFilter4.getText().toString().trim().isEmpty())
                            Toast.makeText(ServiceFilter.this, "Nothing to be removed", Toast.LENGTH_SHORT).show();
                        else {
                            filter4Bool = false;
                            mFilter4.setText("");
                            Toast.makeText(ServiceFilter.this, "Filter Removed", Toast.LENGTH_SHORT).show();
                            filterDialog.dismiss();
                        }
                        break;

                    case 5:
                        if (mFilter5.getText().toString().trim().isEmpty())
                            Toast.makeText(ServiceFilter.this, "Nothing to be removed", Toast.LENGTH_SHORT).show();
                        else {
                            filter5Bool = false;
                            mFilter5.setText("");
                            Toast.makeText(ServiceFilter.this, "Filter Removed", Toast.LENGTH_SHORT).show();
                            filterDialog.dismiss();
                        }
                        break;

                }


            }
        });

        filterDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                switch (selectLocation) {
                    case 1: {
                        if (!chosenFilter.isEmpty()) {
                            mFilter1.setText("  " + chosenFilter);
                            filter1Bool = true;
                        }

                        mFilter1.setBackgroundResource(R.drawable.spinner_close);
                        break;
                    }

                    case 2: {
                        if (!chosenFilter.isEmpty()) {
                            mFilter2.setText("  " + chosenFilter);
                            filter2Bool = true;
                        }

                        mFilter2.setBackgroundResource(R.drawable.spinner_close);
                        break;
                    }

                    case 3: {
                        if (!chosenFilter.isEmpty()) {
                            mFilter3.setText("  " + chosenFilter);
                            filter3Bool = true;
                        }
                        mFilter3.setBackgroundResource(R.drawable.spinner_close);
                        break;
                    }

                    case 4: {
                        if (!chosenFilter.isEmpty()) {
                            mFilter4.setText("  " + chosenFilter);
                            filter4Bool = true;
                        }

                        mFilter4.setBackgroundResource(R.drawable.spinner_close);
                        break;
                    }

                    case 5: {
                        if (!chosenFilter.isEmpty()) {
                            mFilter4.setText("  " + chosenFilter);
                            filter5Bool = true;
                        }
                        mFilter5.setBackgroundResource(R.drawable.spinner_close);
                        break;
                    }

                }

                chosenFilter = "";
            }
        });

        filterDialog.show();

        //Filter C
        filterCDialog.setContentView(R.layout.pop_up_filter_c);
        filterCDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView closeC;
        final Button cc_n, cc_nd, cc_sd, cto_s;
        closeC = filterCDialog.findViewById(R.id.img_close_popUpC);
        cc_n = filterCDialog.findViewById(R.id.bt_cc_n_popUpC);
        cc_nd = filterCDialog.findViewById(R.id.bt_cc_nd_popUpC);
        cc_sd = filterCDialog.findViewById(R.id.bt_cc_sd_popUpC);
        cto_s = filterCDialog.findViewById(R.id.bt_cto_s_popUpC);

        closeC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterCDialog.dismiss();
            }
        });

        cc_n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenFilter = "CC-N";
                filterCDialog.dismiss();
            }
        });

        cc_nd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenFilter = "CC-ND";
                filterCDialog.dismiss();
            }
        });

        cc_sd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenFilter = "CC-SD";
                filterCDialog.dismiss();
            }
        });

        cto_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenFilter = "CTO-S";
                filterCDialog.dismiss();
            }
        });

        filterCDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                filterDialog.dismiss();
            }
        });

        //Filter M
        filterMDialog.setContentView(R.layout.pop_up_filter_m);
        filterMDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView closeM;
        Button MC, MC_A, MT_C, MT_CA, MT_H, MT_S, MT_SA;

        closeM = filterMDialog.findViewById(R.id.img_close_popUpM);

        MC = filterMDialog.findViewById(R.id.bt_mc_popUpM);

        MC_A = filterMDialog.findViewById(R.id.bt_mc_a_popUpM);

        MT_C = filterMDialog.findViewById(R.id.bt_mt_c_popUpM);

        MT_CA = filterMDialog.findViewById(R.id.bt_mt_ca_popUpM);

        MT_H = filterMDialog.findViewById(R.id.bt_mt_h_popUpM);

        MT_S = filterMDialog.findViewById(R.id.bt_mt_s_popUpM);

        MT_SA = filterMDialog.findViewById(R.id.bt_mt_sa_popUpM);

        filterMDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                filterDialog.dismiss();
            }
        });

        closeM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterMDialog.dismiss();
            }
        });

        MC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenFilter = "MC";
                filterMDialog.dismiss();
            }
        });

        MC_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenFilter = "MC-A";
                filterMDialog.dismiss();
            }
        });

        MT_C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenFilter = "MT-C";
                filterMDialog.dismiss();
            }
        });

        MT_CA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenFilter = "MT-CA";
                filterMDialog.dismiss();
            }
        });

        MT_H.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenFilter = "MT-H";
                filterMDialog.dismiss();
            }
        });

        MT_S.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenFilter = "MT-S";
                filterMDialog.dismiss();
            }
        });

        MT_SA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenFilter = "MT-SA";
                filterMDialog.dismiss();
            }
        });


        //Filter P
        filterPDialog.setContentView(R.layout.pop_up_filter_p);
        filterPDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView closeP;
        Button pp_1, pp_5;
        closeP = filterPDialog.findViewById(R.id.img_close_popUpP);
        pp_1 = filterPDialog.findViewById(R.id.bt_pp1_popUpP);
        pp_5 = filterPDialog.findViewById(R.id.bt_pp5_popUpP);

        filterPDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                filterDialog.dismiss();
            }
        });

        closeP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterPDialog.dismiss();
            }
        });

        pp_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenFilter = "PP-1";
                filterPDialog.dismiss();
            }
        });

        pp_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosenFilter = "PP-5";
                filterPDialog.dismiss();
            }
        });

    }

    public void service(View view) {
        if (!filter1Bool && !filter2Bool && !filter3Bool && !filter4Bool && !filter5Bool) {
            changedFilter = false;
        }

        if (mWT.getText().toString().trim().isEmpty() && mWTS.getText().toString().isEmpty() && mFCD.getText().toString().trim().isEmpty()) {
            changeSparePart = false;
        }

        if (!changedFilter && !changeSparePart) {
            Toast.makeText(ServiceFilter.this, "No changes made", Toast.LENGTH_SHORT).show();
            return;
        }
        ProgressBar progressBar;
        final Sprite style = new Wave();
        loadingDialog.setContentView(R.layout.pop_up_loading_screen);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressBar = loadingDialog.findViewById(R.id.sk_loadingPU);
        progressBar.setIndeterminateDrawable(style);

        loadingDialog.setCanceledOnTouchOutside(false);

        loadingDialog.show();


        final Map<String, Object> serviceDetailsMap = new HashMap<>();
        String servicePrice = mServicePrice.getText().toString().trim();
        String note = mNote.getText().toString().trim();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        LocalDateTime timeNow = LocalDateTime.now();
        int hour = timeNow.getHour();
        int minute = timeNow.getMinute();

        ServiceDetails serviceDetails = new ServiceDetails();

        if (!uploadPicture) {
            loadingDialog.dismiss();
            Toast.makeText(ServiceFilter.this, "No image uploaded yet", Toast.LENGTH_SHORT).show();
            return;
        }

        if (servicePrice.isEmpty()) {
            loadingDialog.dismiss();
            mServicePrice.setError("Service price cannot be empty");
            mServicePrice.requestFocus();
            return;
        }

        if (note.isEmpty()) {
            note = "No note provided";
        }


        serviceDetails.setServiceID(createID());
        serviceDetails.setTechnicianID(mAuth.getCurrentUser().getUid());
        serviceDetails.setServicePrice(String.format("%.02f", (Double.parseDouble(servicePrice))));
        serviceDetails.setServiceDate(date);
        serviceDetails.setServiceTime(hour + ":" + minute);
        serviceDetails.setNote(note);
        serviceDetailsMap.put("lastServiced", date);
        serviceDetailsMap.put("note", note);
        serviceDetailsMap.put("imageLocation", serviceDetails.getServiceID());

        if (filter1Bool) {
            serviceDetails.setChangedFilter1(mFilter1.getText().toString().trim());
            serviceDetailsMap.put("filter1", mFilter1.getText().toString().trim());
            serviceDetailsMap.put("filter1LC", date);
        }

        if (filter2Bool) {
            serviceDetails.setChangedFilter2(mFilter2.getText().toString().trim());
            serviceDetailsMap.put("filter2", mFilter2.getText().toString().trim());
            serviceDetailsMap.put("filter2LC", date);
        }

        if (filter3Bool) {
            serviceDetails.setChangedFilter3(mFilter3.getText().toString().trim());
            serviceDetailsMap.put("filter3", mFilter3.getText().toString().trim());
            serviceDetailsMap.put("filter3LC", date);
        }

        if (filter4Bool) {
            serviceDetails.setChangedFilter4(mFilter4.getText().toString().trim());
            serviceDetailsMap.put("filter4", mFilter4.getText().toString().trim());
            serviceDetailsMap.put("filter4LC", date);
        }

        if (filter5Bool) {
            serviceDetails.setChangedFilter5(mFilter5.getText().toString().trim());
            serviceDetailsMap.put("filter5", mFilter5.getText().toString().trim());
            serviceDetailsMap.put("filter5LC", date);
        }

        if (!mWT.getText().toString().trim().isEmpty()) {
            serviceDetails.setChangedWt(mWT.getText().toString().trim());
            serviceDetailsMap.put("wt", mWT.getText().toString().trim());
            serviceDetailsMap.put("wtLC", date);
        }

        if (!mWTS.getText().toString().trim().isEmpty()) {
            serviceDetails.setChangedWt_s(mWTS.getText().toString().trim());
            serviceDetailsMap.put("wt_s", mWT.getText().toString().trim());
            serviceDetailsMap.put("wt_sLC", date);
        }

        if (!mFCD.getText().toString().trim().isEmpty()) {
            serviceDetails.setChangedFC_D(mFCD.getText().toString().trim());
            serviceDetailsMap.put("fc_D", mFCD.getText().toString().trim());
            serviceDetailsMap.put("fc_DLC", date);
        }


        final StorageReference imageReference = storageReference.child("filterPictures/" + documentID + "/" + serviceDetails.getServiceID() + "/filterPictures.jpg");
        imageReference.putFile(getImageUri(this, bitmap));
        String year = serviceDetails.getServiceID().substring(0, 4);
        final String month = serviceDetails.getServiceID().substring(4, 7);



        final DocumentReference updateFilterDetails = db.collection("sales").document(year).collection(month).document(documentID);

        Map<String, Object> placeHolder = new HashMap<>();
        placeHolder.put("dummyData", "dummyData");

        Map<String, Object> data = new HashMap<>();
        data.put("commission", String.format("%.02f", (Double.parseDouble(servicePrice) * 0.02)));
        data.put("filterID", documentID);

        final DocumentReference staffHistory = db.collection("staffDetails").document(mAuth.getCurrentUser().getUid())
                .collection("service").document(year);
        staffHistory.set(placeHolder);

        DocumentReference staffHistory2 = db.collection("staffDetails").document(mAuth.getCurrentUser().getUid())
                .collection("service").document(year).collection(month).document(serviceDetails.getServiceID());
        staffHistory2.set(data);

        final DocumentReference filterDetails = db.collection("sales").document(year).collection(month).document(documentID).collection("serviceDetails").document(year);
        filterDetails.set(placeHolder);
        serviced = true;
        DocumentReference filterDetails2 = db.collection("sales").document(year).collection(month)
                .document(documentID).collection("serviceDetails").document(year).collection(year).document(serviceDetails.getServiceID());

        filterDetails2.set(serviceDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                updateFilterDetails.update(serviceDetailsMap);
                filterDetails.update("dummyData", FieldValue.delete());
                staffHistory.update("dummyData", FieldValue.delete());
                loadingDialog.dismiss();
                Toast.makeText(ServiceFilter.this, "Service done, thank you", Toast.LENGTH_SHORT).show();
                ServiceFilter.super.onBackPressed();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingDialog.dismiss();
                Toast.makeText(ServiceFilter.this, "Error happened, please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String createID() {
        LocalDateTime timeNow = LocalDateTime.now();
        int year = timeNow.getYear();
        int month = timeNow.getMonthValue();
        int day = timeNow.getDayOfMonth();
        int hour = timeNow.getHour();
        int minute = timeNow.getMinute();
        int second = timeNow.getSecond();
        int millis = timeNow.get(ChronoField.MILLI_OF_SECOND);

        switch (month) {
            case 1:
                return year + "Jan" + day + "Hour" + hour + "Min" + minute + "Sec" + second + "Mil" + millis;

            case 2:
                return year + "Feb" + day + "Hour" + hour + "Min" + minute + "Sec" + second + "Mil" + millis;

            case 3:
                return year + "Mar" + day + "Hour" + hour + "Min" + minute + "Sec" + second + "Mil" + millis;

            case 4:
                return year + "Apr" + day + "Hour" + hour + "Min" + minute + "Sec" + second + "Mil" + millis;

            case 5:
                return year + "May" + day + "Hour" + hour + "Min" + minute + "Sec" + second + "Mil" + millis;

            case 6:
                return year + "Jun" + day + "Hour" + hour + "Min" + minute + "Sec" + second + "Mil" + millis;

            case 7:
                return year + "Jul" + day + "Hour" + hour + "Min" + minute + "Sec" + second + "Mil" + millis;

            case 8:
                return year + "Aug" + day + "Hour" + hour + "Min" + minute + "Sec" + second + "Mil" + millis;

            case 9:
                return year + "Sep" + day + "Hour" + hour + "Min" + minute + "Sec" + second + "Mil" + millis;

            case 10:
                return year + "Oct" + day + "Hour" + hour + "Min" + minute + "Sec" + second + "Mil" + millis;

            case 11:
                return year + "Nov" + day + "Hour" + hour + "Min" + minute + "Sec" + second + "Mil" + millis;

            case 12:
                return year + "Dec" + day + "Hour" + hour + "Min" + minute + "Sec" + second + "Mil" + millis;

            default:
                return "";
        }

    }

    public void takePictures(View view) {
        permissionRequest();
    }

    public void permissionRequest() {
        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                message.setText("(Upload Image)");
                message.setTextColor(getColor(R.color.green));
                message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        takePicturesPermissionGet();
                    }
                });
                filterImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        takePicturesPermissionGet();
                    }
                });
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                message.setText("Camera permission not granted");
                message.setTextColor(getColor(R.color.red));
                message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                        permissionRequest();
                    }
                });

                final Snackbar snackbar = Snackbar.make(constrainLayout, "Camera permission is needed in order to take pictures", Snackbar.LENGTH_LONG);
                snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                snackbar.setDuration(10000);
                snackbar.setActionTextColor(getResources().getColor(R.color.red));
                snackbar.setAction("Setting", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                        permissionRequest();
                    }
                });
                snackbar.show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    private void takePicturesPermissionGet() {
        String fileName = "photo";
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            File imageFiles = File.createTempFile(fileName, ".jpg", storageDirectory);
            currentPhotoPath = imageFiles.getAbsolutePath();
            Uri imageUri = FileProvider.getUriForFile(ServiceFilter.this, "com.example.filterapp.fileprovider", imageFiles);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, SELECT_PICTURE);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE) {
            if (currentPhotoPath == null) {
                message.setVisibility(View.VISIBLE);
                filterImg.setImageDrawable(getDrawable(R.drawable.white_image));
                uploadPicture = false;
            } else {
                bitmap = BitmapFactory.decodeFile(currentPhotoPath);
                filterImg.setImageBitmap(bitmap);
                imageBit = bitmap;
                message.setVisibility(View.GONE);
                uploadPicture = true;
            }
        }
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "filterImage" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }

    @Override
    protected void onStart() {
        super.onStart();

        permissionRequest();
    }

}
