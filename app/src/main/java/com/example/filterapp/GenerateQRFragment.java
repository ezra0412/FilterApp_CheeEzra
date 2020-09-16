package com.example.filterapp;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class GenerateQRFragment extends Fragment {

    public GenerateQRFragment() {
        // Required empty public constructor
    }

    EditText mInvoiceNum, mMobile, mFModel, mCommission, mPrice, mNote;
    TextView mFilter1, mFilter2, mFilter3, mFilter4, mFilter5;
    String filter1, filter2, filter3, filter4, filter5;

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

        return v;
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
