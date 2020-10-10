package com.example.filterapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.filterapp.classes.FilterDetails;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class GenerateQRFragment extends Fragment {

    public GenerateQRFragment() {
        // Required empty public constructor
    }

    EditText mInvoiceNum, mMobile, mFName, mFModel, mCommission, mPrice, mNote;
    TextView mFilter1, mFilter2, mFilter3, mFilter4, mFilter5;
    LinkedList<String> filters = new LinkedList<>();
    int selectLocation;
    Dialog filterDialog, filterCDialog, filterPDialog, filterMDialog;
    Button generate;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String documentID;
    String invoiceNum, mobile, fName, fModel, commission, price, note;
    Dialog loadingDialog;
    String date, time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_generate_q_r, container, false);

        mInvoiceNum = v.findViewById(R.id.et_invNum_generateQR);

        mMobile = v.findViewById(R.id.et_mobile_generateQR);

        mFModel = v.findViewById(R.id.et_filterModel_generateQR);

        mFName = v.findViewById(R.id.et_fName_generateQR);

        mCommission = v.findViewById(R.id.et_comission_generateQR);

        mPrice = v.findViewById(R.id.et_salesPrice_generateQR);

        mNote = v.findViewById(R.id.et_note_generateQR);

        mFilter1 = v.findViewById(R.id.tv_filter1_generateQR);

        mFilter2 = v.findViewById(R.id.tv_filter2_generateQR);

        mFilter3 = v.findViewById(R.id.tv_filter3_generateQR);

        mFilter4 = v.findViewById(R.id.tv_filter4_generateQR);

        mFilter5 = v.findViewById(R.id.tv_filter5_generateQR);

        loadingDialog = new Dialog(getActivity());

        filterDialog = new Dialog(getActivity());

        filterCDialog = new Dialog(getActivity());

        filterMDialog = new Dialog(getActivity());

        filterPDialog = new Dialog(getActivity());

        generate = v.findViewById(R.id.bt_generate_fQR);

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generate();
            }
        });

        mFilter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFilter1.setBackgroundResource(R.drawable.spinner_open);
                selectLocation = 1;
                choseFilter();
            }
        });

        mFilter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFilter2.setBackgroundResource(R.drawable.spinner_open);
                selectLocation = 2;
                choseFilter();
            }
        });

        mFilter3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFilter3.setBackgroundResource(R.drawable.spinner_open);
                selectLocation = 3;
                choseFilter();
            }
        });

        mFilter4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFilter4.setBackgroundResource(R.drawable.spinner_open);
                selectLocation = 4;
                choseFilter();
            }
        });

        mFilter5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFilter5.setBackgroundResource(R.drawable.spinner_open);
                selectLocation = 5;
                choseFilter();
            }
        });
        return v;
    }

    public void choseFilter() {
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
                if (filters.size() >= selectLocation) {
                    switch (selectLocation) {
                        case 1: {
                            filters.remove(0);
                            Toast.makeText(getActivity(), "Filter removed", Toast.LENGTH_SHORT).show();
                            filterDialog.dismiss();
                            break;
                        }

                        case 2: {
                            filters.remove(1);
                            Toast.makeText(getActivity(), "Filter removed", Toast.LENGTH_SHORT).show();
                            filterDialog.dismiss();
                            break;
                        }

                        case 3: {
                            filters.remove(2);
                            Toast.makeText(getActivity(), "Filter removed", Toast.LENGTH_SHORT).show();
                            filterDialog.dismiss();
                            break;
                        }

                        case 4: {
                            filters.remove(3);
                            Toast.makeText(getActivity(), "Filter removed", Toast.LENGTH_SHORT).show();
                            filterDialog.dismiss();
                            break;
                        }

                        case 5: {
                            filters.remove(4);
                            Toast.makeText(getActivity(), "Filter removed", Toast.LENGTH_SHORT).show();
                            filterDialog.dismiss();
                            break;
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "Nothing to remove", Toast.LENGTH_SHORT).show();
                }

            }
        });

        filterDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                switch (selectLocation) {
                    case 1: {
                        mFilter1.setBackgroundResource(R.drawable.spinner_close);
                        break;
                    }

                    case 2: {
                        mFilter2.setBackgroundResource(R.drawable.spinner_close);
                        break;
                    }

                    case 3: {
                        mFilter3.setBackgroundResource(R.drawable.spinner_close);
                        break;
                    }

                    case 4: {
                        mFilter4.setBackgroundResource(R.drawable.spinner_close);
                        break;
                    }

                    case 5: {
                        mFilter5.setBackgroundResource(R.drawable.spinner_close);
                        break;
                    }

                }

                if (filters.size() == 0) {
                    mFilter1.setText(" Please Select filter 1");
                    mFilter2.setText(" Please Select filter 2");
                    mFilter3.setText(" Please Select filter 3");
                    mFilter4.setText(" Please Select filter 4");
                    mFilter5.setText(" Please Select filter 5");
                } else {
                    for (int i = 0; i < filters.size(); i++) {
                        switch (i) {
                            case 0: {
                                mFilter1.setText("  " + filters.get(0));
                                mFilter2.setText(" Please Select filter 2");
                                mFilter3.setText(" Please Select filter 3");
                                mFilter4.setText(" Please Select filter 4");
                                mFilter5.setText(" Please Select filter 5");

                                break;
                            }

                            case 1: {
                                mFilter2.setText("  " + filters.get(1));
                                mFilter3.setText(" Please Select filter 3");
                                mFilter4.setText(" Please Select filter 4");
                                mFilter5.setText(" Please Select filter 5");
                                break;
                            }

                            case 2: {
                                mFilter3.setText("  " + filters.get(2));
                                mFilter4.setText(" Please Select filter 4");
                                mFilter5.setText(" Please Select filter 5");
                                break;
                            }

                            case 3: {
                                mFilter4.setText("  " + filters.get(3));
                                mFilter5.setText(" Please Select filter 5");
                                break;
                            }

                            case 4: {
                                mFilter5.setText("  " + filters.get(4));
                                break;
                            }
                        }
                    }
                }
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
                filters.add("CC-N");
                filterCDialog.dismiss();
            }
        });

        cc_nd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filters.add("CC-ND");
                filterCDialog.dismiss();
            }
        });

        cc_sd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filters.add("CC-SD");
                filterCDialog.dismiss();
            }
        });

        cto_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filters.add("CTO-S");
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
                filters.add("MC");
                filterMDialog.dismiss();
            }
        });

        MC_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filters.add("MC-A");
                filterMDialog.dismiss();
            }
        });

        MT_C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filters.add("MT-C");
                filterMDialog.dismiss();
            }
        });

        MT_CA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filters.add("MT-CA");
                filterMDialog.dismiss();
            }
        });

        MT_H.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filters.add("MT-H");
                filterMDialog.dismiss();
            }
        });

        MT_S.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filters.add("MT-S");
                filterMDialog.dismiss();
            }
        });

        MT_SA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filters.add("MT-SA");
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
                filters.add("PP-1");
                filterPDialog.dismiss();
            }
        });

        pp_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filters.add("PP-5");
                filterPDialog.dismiss();
            }
        });

    }

    public void generate() {
        invoiceNum = mInvoiceNum.getText().toString().trim();
        mobile = mMobile.getText().toString().trim();
        fName = mFName.getText().toString().trim();
        fModel = mFModel.getText().toString().trim();
        commission = mCommission.getText().toString().trim();
        price = mPrice.getText().toString().trim();
        note = mNote.getText().toString().trim();
        if (invoiceNum.isEmpty()) {
            mInvoiceNum.setError("Invoice number cannot be empty");
            mInvoiceNum.requestFocus();
            return;
        }

        if (mobile.isEmpty()) {
            mMobile.setError("Customer mobile cannot be empty");
            mMobile.requestFocus();
            return;
        }

        if (!Patterns.PHONE.matcher(mobile).matches()) {
            mMobile.setError("Invalid mobile format");
            mMobile.requestFocus();
            return;
        }


        if (fName.isEmpty()) {
            mFName.setError("Customer first name cannot be empty");
            mFName.requestFocus();
            return;
        }



        if (fModel.isEmpty()) {
            mFModel.setError("Filter model cannot be empty");
            mFModel.requestFocus();
            return;
        }

        if (commission.isEmpty()) {
            commission = "0";
        }

        if (price.isEmpty()) {
            mPrice.setError("Price cannot be empty");
            mPrice.requestFocus();
            return;
        }

        if (filters.size() < 1) {
            Toast.makeText(getActivity(), "Need to have at least one filter.", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressBar progressBar;
        final Sprite style = new Wave();
        loadingDialog.setContentView(R.layout.pop_up_loading_screen);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressBar = loadingDialog.findViewById(R.id.sk_loadingPU);
        progressBar.setIndeterminateDrawable(style);

        //Make the loading dialog not dismissible
        loadingDialog.setCanceledOnTouchOutside(false);

        loadingDialog.show();

        DocumentReference checkCustomer = db.collection("customerDetails").document("sorted").
                collection(fName.substring(0, 1).toLowerCase()).document(fName.substring(0, 1).toLowerCase() + mobile);
        checkCustomer.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (!documentSnapshot.exists()) {
                        loadingDialog.dismiss();
                        mFName.setError("Customer not yet registered");
                        mFName.requestFocus();
                        return;
                    } else {

                        FilterDetails filterDetails = new FilterDetails();
                        documentID = createID();

                        String year = documentID.substring(0, 4);
                        String month = documentID.substring(4, 7);

                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDateTime now = LocalDateTime.now();
                        String date = dtf.format(now);

                        filterDetails.setInvoiceNumber(invoiceNum.toUpperCase());
                        filterDetails.setMobile(mobile);
                        filterDetails.setfName(fName);
                        filterDetails.setfModel(fModel.toUpperCase());
                        filterDetails.setSalesID(mAuth.getCurrentUser().getUid());

                        if (note.isEmpty()) {
                            note = "No note provided";
                        }

                        switch (filters.size()) {
                            case 1: {
                                filterDetails.setFilter1(filters.get(0));

                                filterDetails.setFilter1LC(date);
                                break;
                            }

                            case 2: {
                                filterDetails.setFilter1(filters.get(0));
                                filterDetails.setFilter2(filters.get(1));

                                filterDetails.setFilter1LC(date);
                                filterDetails.setFilter2LC(date);
                                break;
                            }

                            case 3: {
                                filterDetails.setFilter1(filters.get(0));
                                filterDetails.setFilter2(filters.get(1));
                                filterDetails.setFilter3(filters.get(2));

                                filterDetails.setFilter1LC(date);
                                filterDetails.setFilter2LC(date);
                                filterDetails.setFilter3LC(date);
                                break;
                            }

                            case 4: {
                                filterDetails.setFilter1(filters.get(0));
                                filterDetails.setFilter2(filters.get(1));
                                filterDetails.setFilter3(filters.get(2));
                                filterDetails.setFilter4(filters.get(3));

                                filterDetails.setFilter1LC(date);
                                filterDetails.setFilter2LC(date);
                                filterDetails.setFilter3LC(date);
                                filterDetails.setFilter4LC(date);
                                break;
                            }

                            case 5: {
                                filterDetails.setFilter1(filters.get(0));
                                filterDetails.setFilter2(filters.get(1));
                                filterDetails.setFilter3(filters.get(2));
                                filterDetails.setFilter4(filters.get(3));
                                filterDetails.setFilter5(filters.get(4));

                                filterDetails.setFilter1LC(date);
                                filterDetails.setFilter2LC(date);
                                filterDetails.setFilter3LC(date);
                                filterDetails.setFilter4LC(date);
                                filterDetails.setFilter5LC(date);
                                break;
                            }
                        }

                        filterDetails.setPrice(price);
                        filterDetails.setNote(note);

                        filterDetails.setDayBrought(date);
                        filterDetails.setTimeBrought(time);

                        Map<String, String> dummyData = new HashMap<>();
                        dummyData.put("placeHolder", "dummyData");
                        final DocumentReference staffDB = db.collection("staffDetails").document(mAuth.getCurrentUser().getUid()).
                                collection("sales").document(year);
                        staffDB.set(dummyData);
                        DocumentReference staffDB2 = db.collection("staffDetails").document(mAuth.getCurrentUser().getUid()).
                                collection("sales").document(year).collection(month).document(documentID);
                        staffDB2.set(dummyData);

                        staffDB.update("placeHolder", FieldValue.delete());

                        DocumentReference customerDB = db.collection("customerDetails").document("sorted")
                                .collection(fName.substring(0, 1)).document(fName.substring(0, 1) + mobile)
                                .collection("purchaseHistory").document(year);
                        customerDB.set(dummyData);

                        Map<String, Object> data = new HashMap<>();
                        data.put("commission", commission);
                        DocumentReference customerDB2 = db.collection("customerDetails").document("sorted")
                                .collection(fName.substring(0, 1).toLowerCase()).document(fName.substring(0, 1).toLowerCase() + mobile)
                                .collection("purchaseHistory").document(year).collection(year).document(documentID);
                        customerDB2.set(data);

                        customerDB.update("placeHolder", FieldValue.delete());

                        final DocumentReference filterDetailsDB = db.collection("sales").document(year);
                        filterDetailsDB.set(dummyData);

                        mCommission.setText("");
                        mFModel.setText("");
                        mFilter1.setText("");
                        mFilter2.setText("");
                        mFilter3.setText("");
                        mFilter4.setText("");
                        mFilter5.setText("");
                        mCommission.setText("");
                        mPrice.setText("");
                        mNote.setText("");


                        DocumentReference filterDetailsDB2 = db.collection("sales").document(year).collection(month).document(documentID);
                        filterDetailsDB2.set(filterDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                filterDetailsDB.update("placeHolder", FieldValue.delete());
                                filters = new LinkedList<>();
                                loadingDialog.dismiss();
                                Intent intent = new Intent(getActivity(), QRcode.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("documentID", documentID);
                                startActivity(intent);
                            }
                        });
                    }
                }
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

        date = day + "/" + month + "/" + year;
        time = hour + ":" + minute;

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
