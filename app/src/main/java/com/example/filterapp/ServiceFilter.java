package com.example.filterapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.filterapp.classes.FilterDetails;
import com.example.filterapp.classes.ServiceDetails;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.Map;

public class ServiceFilter extends AppCompatActivity {

    TextView mFilter1, mFilter2, mFilter3, mFilter4, mFilter5;
    EditText mWT, mWTS, mFCD, mServicePrice, mNote;
    boolean changedFilter = true, changeSparePart = true;
    String documentID;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FilterDetails filterDetails = new FilterDetails();
    boolean filter1Bool = false, filter2Bool = false, filter3Bool = false, filter4Bool = false, filter5Bool = false;
    Dialog filterDialog, filterCDialog, filterMDialog, filterPDialog;
    String chosenFilter = "";
    int counter = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_filter);

        mFilter1 = findViewById(R.id.tv_changedFilter1_serviceFIlter);
        mFilter2 = findViewById(R.id.tv_changedFilter2_serviceFIlter);
        mFilter3 = findViewById(R.id.tv_changedFilter3_serviceFIlter);
        mFilter4 = findViewById(R.id.tv_changedFilter4_serviceFIlter);
        mFilter5 = findViewById(R.id.tv_changedFilter5_serviceFIlter);

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


        if (changedFilter || changeSparePart) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final AlertDialog dialog = builder.setMessage("Details will not be saved, are you sure you want to go back?")
                    .setTitle("Confirmation")
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ServiceFilter.super.onBackPressed();
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
        Map<String, Object> serviceDetailsMap = new HashMap<>();
        String servicePrice = mServicePrice.getText().toString().trim();
        String note = mNote.getText().toString().trim();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        LocalDateTime timeNow = LocalDateTime.now();
        int hour = timeNow.getHour();
        int minute = timeNow.getMinute();

        ServiceDetails serviceDetails = new ServiceDetails();
        if (servicePrice.isEmpty()) {
            mServicePrice.setError("Service price cannot be empty");
            mServicePrice.requestFocus();
        }

        if (note.isEmpty()) {
            note = "No note provided";
        }

        serviceDetails.setServiceID(createID());
        serviceDetails.setTechnicianID(mAuth.getCurrentUser().getUid());
        serviceDetails.setCommission(String.format("%.02f", (Double.parseDouble(servicePrice) * 0.01)));
        serviceDetails.setServicePrice(String.format("%.02f", servicePrice));
        serviceDetails.setServiceDate(date);
        serviceDetails.setServiceTime(hour + ":" + minute);
        serviceDetails.setNote(note);

        serviceDetailsMap.put("note", note);

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

        if (mWT.getText().toString().trim().isEmpty()) {
            serviceDetails.setChangedWt(mWT.getText().toString().trim());
            serviceDetailsMap.put("wt", mWT.getText().toString().trim());
            serviceDetailsMap.put("wtLC", date);
        }

        if (mWTS.getText().toString().trim().isEmpty()) {
            serviceDetails.setChangedWt_s(mWTS.getText().toString().trim());
            serviceDetailsMap.put("wt_s", mWT.getText().toString().trim());
            serviceDetailsMap.put("wt_sLC", date);
        }

        if (mFCD.getText().toString().trim().isEmpty()) {
            serviceDetails.setChangedFC_D(mFCD.getText().toString().trim());
            serviceDetailsMap.put("FC_D", mFCD.getText().toString().trim());
            serviceDetailsMap.put("FC_DLC", date);
        }

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
}
