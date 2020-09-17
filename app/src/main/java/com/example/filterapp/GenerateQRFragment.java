package com.example.filterapp;

import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.LinkedList;


/**
 * A simple {@link Fragment} subclass.
 */
public class GenerateQRFragment extends Fragment {

    public GenerateQRFragment() {
        // Required empty public constructor
    }

    EditText mInvoiceNum, mMobile, mFModel, mCommission, mPrice, mNote;
    TextView mFilter1, mFilter2, mFilter3, mFilter4, mFilter5;
    LinkedList<String> filters = new LinkedList<>();
    int selectLocation;
    Dialog filterDialog, filterCDialog, filterPDialog, filterMDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_generate_q_r, container, false);

        mInvoiceNum = v.findViewById(R.id.et_invNum_generateQR);

        mMobile = v.findViewById(R.id.et_mobile_generateQR);

        mFModel = v.findViewById(R.id.et_filterModel_generateQR);

        mCommission = v.findViewById(R.id.et_comission_generateQR);

        mPrice = v.findViewById(R.id.et_salesPrice_generateQR);

        mNote = v.findViewById(R.id.et_note_generateQR);

        mFilter1 = v.findViewById(R.id.tv_filter1_generateQR);

        mFilter2 = v.findViewById(R.id.tv_filter2_generateQR);

        mFilter3 = v.findViewById(R.id.tv_filter3_generateQR);

        mFilter4 = v.findViewById(R.id.tv_filter4_generateQR);

        mFilter5 = v.findViewById(R.id.tv_filter5_generateQR);

        filterDialog = new Dialog(getActivity());

        filterCDialog = new Dialog(getActivity());

        filterMDialog = new Dialog(getActivity());

        filterPDialog = new Dialog(getActivity());

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
                if (filters.size() >= 1) {
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

    public void generate(View view) {
        String invoiceNum, mobile, fModel, commission, price, note;
        invoiceNum = mInvoiceNum.getText().toString().trim();
        mobile = mInvoiceNum.getText().toString().trim();
        fModel = mInvoiceNum.getText().toString().trim();
        commission = mCommission.getText().toString().trim();
        price = mCommission.getText().toString().trim();
        note = mCommission.getText().toString().trim();

        if (invoiceNum.isEmpty()) {
            mInvoiceNum.setError("Invoice number cannot be empty");
            mInvoiceNum.requestFocus();
        }

        if (mobile.isEmpty()) {
            mMobile.setError("Mobile cannot be empty");
            mMobile.requestFocus();
        }

        if (Patterns.PHONE.matcher(mobile).matches()) {
            mMobile.setError("Invalid mobile format");
            mMobile.requestFocus();
        }

        if (fModel.isEmpty()) {
            mFModel.setError("Filter model cannot be empty");
            mFModel.requestFocus();
        }

        if (commission.isEmpty()) {
            mCommission.setError("Commission cannot be empty");
            mCommission.requestFocus();
        }

        if (price.isEmpty()) {
            mPrice.setError("Price cannot be empty");
            mPrice.requestFocus();
        }

        if (note.isEmpty()) {
            note = "No note provided";
        }


    }


}
