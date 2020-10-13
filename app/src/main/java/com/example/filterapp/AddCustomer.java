package com.example.filterapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.filterapp.classes.Address;
import com.example.filterapp.classes.CustomerDetails;
import com.example.filterapp.geocoding.GeocodingLocation;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddCustomer extends AppCompatActivity {
    EditText mFName, mLName, mEmail, mMobile, mNote;
    TextView mAddress, title;
    Button btAdd;
    ImageView addSign;
    static boolean addAddress = false;
    static boolean madeChanges = false;
    static Address sAddress = new Address();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CustomerDetails customerDetails;
    String documentCollection, documentID;
    String customerID = "";
    Address passedAddress = new Address();
    CustomerDetails passedCustomerDetails = new CustomerDetails();
    Dialog loadingDialog;
    boolean changedDetails = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        mFName = findViewById(R.id.et_fName_addCustomer);
        mLName = findViewById(R.id.et_lName_addCustomer);
        mEmail = findViewById(R.id.et_email_addCustomer);
        mMobile = findViewById(R.id.et_mobile_addCustomer);
        mNote = findViewById(R.id.et_note_addCustomer);
        mAddress = findViewById(R.id.tv_address_addCustomer);
        addSign = findViewById(R.id.img_addSign_addCustomer);
        title = findViewById(R.id.tv_title_addCustomer);
        btAdd = findViewById(R.id.bt_add_AddCustomer);
        loadingDialog = new Dialog(this);

        customerID = getIntent().getStringExtra("customerID");

        if (!customerID.equalsIgnoreCase("non")) {
            passedAddress = getIntent().getParcelableExtra("address");
            passedCustomerDetails = getIntent().getParcelableExtra("customerDetails");

                    title.setText("Customer Details");
                    btAdd.setText("Update Customer Details");
                    mFName.setText(passedCustomerDetails.getfName());
                    mLName.setText(passedCustomerDetails.getlName());
                    mEmail.setText(passedCustomerDetails.getEmail());
                    mMobile.setText(passedCustomerDetails.getMobile());
                    mNote.setText(passedCustomerDetails.getNote());
                    mFName.setEnabled(false);
                    mLName.setEnabled(false);
                    mMobile.setEnabled(false);

                    sAddress = passedAddress;

                    mAddress.setText(passedAddress.formatedAddress());

        }

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                changedDetails = true;
            }
        };
        mFName.addTextChangedListener(textWatcher);
        mLName.addTextChangedListener(textWatcher);
        mEmail.addTextChangedListener(textWatcher);
        mMobile.addTextChangedListener(textWatcher);
        mNote.addTextChangedListener(textWatcher);

    }


    public void addCustomer(View view) {
        String fName, lName, email, mobile, note;
        fName = mFName.getText().toString().trim();
        lName = mLName.getText().toString().trim();
        email = mEmail.getText().toString().trim();
        mobile = mMobile.getText().toString().trim();
        note = mNote.getText().toString();


        if (!changedDetails&&!madeChanges){
            Toast.makeText(AddCustomer.this,"Nothing Changed",Toast.LENGTH_SHORT).show();
            return;
        }

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

        if (email.isEmpty()||email.equalsIgnoreCase("-")) {
            email ="-";

        }else{
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                mEmail.setError("Wrong email format");
                mEmail.requestFocus();
                return;
            }
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


        if (!addAddress) {
            Toast.makeText(AddCustomer.this, "Please add customer address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (note.isEmpty()) {
            note = "No note provided";
        }

        ProgressBar progressBar;
        final Sprite style = new Wave();
        loadingDialog.setContentView(R.layout.pop_up_loading_screen);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressBar = loadingDialog.findViewById(R.id.sk_loadingPU);
        progressBar.setIndeterminateDrawable(style);

        loadingDialog.setCanceledOnTouchOutside(false);

        loadingDialog.show();

        DocumentReference checkCustomer = db.collection("customerDetails").document("sorted").collection(fName.substring(0, 1).toLowerCase()).document(fName.substring(0, 1).toLowerCase() + mobile);
        checkCustomer.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        loadingDialog.dismiss();
                        mMobile.setText("Customer registered before");
                        mMobile.requestFocus();
                        return;
                    }
                }
            }
        });

        customerDetails = new CustomerDetails(capitalize(fName), capitalize(lName), mobile, email, note, false);

        documentCollection = fName.substring(0, 1).toLowerCase();
        documentID = documentCollection + mobile;

        if (isMadeChanges()) {
            GeocodingLocation geocodingLocation = new GeocodingLocation();
            geocodingLocation.getAddressFromLocation(sAddress, getApplicationContext());
            sAddress.setLan(geocodingLocation.getAl());
            sAddress.setLon(geocodingLocation.getLon());
        }

        if (sAddress.getLan() == 0.0 || sAddress.getLon() == 0.0) {
            Toast.makeText(AddCustomer.this, "Error please try again later", Toast.LENGTH_LONG).show();
            return;
        }

        DocumentReference customerAddress = db.collection("customerDetails").document("sorted").collection(documentCollection).document(documentID).collection("address").document("address");
        customerAddress.set(sAddress).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                DocumentReference customer = db.collection("customerDetails").document("sorted").collection(documentCollection).document(documentID);
                customer.set(customerDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loadingDialog.dismiss();
                        if (!customerID.equalsIgnoreCase("non"))
                            Toast.makeText(AddCustomer.this, "Customer detail updated", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(AddCustomer.this, "Customer added", Toast.LENGTH_SHORT).show();


                        Intent intent = new Intent(AddCustomer.this, CustomerDetail.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("customerDetails", customerDetails);
                        startActivity(intent);
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingDialog.dismiss();
                        Toast.makeText(AddCustomer.this, "Error please try again later", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingDialog.dismiss();
                Toast.makeText(AddCustomer.this, "Error please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void setAddAddress(boolean addAddress) {
        AddCustomer.addAddress = addAddress;
    }

    public static void setsAddress(Address sAddress) {
        AddCustomer.sAddress = sAddress;
    }


    public static boolean isAddAddress() {
        return addAddress;
    }

    public static Address getsAddress() {
        return sAddress;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (addAddress) {
            addSign.setVisibility(View.INVISIBLE);
            mAddress.setText(sAddress.formatedAddress());
        } else
            addSign.setVisibility(View.VISIBLE);

        if (!customerID.equalsIgnoreCase("non")) {
            addSign.setVisibility(View.INVISIBLE);
            addAddress = true;
        }


    }

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

    public void openAddress(View view) {
        Intent intent = new Intent(AddCustomer.this, AddAddress.class);
        intent.putExtra("customerID", customerID);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("customerID", "non");
        startActivity(intent);
    }

    public void back(View view) {
        if (!changedDetails&&!madeChanges) {
            super.onBackPressed();
            finish();
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final AlertDialog dialog = builder.setMessage("Are you sure you wanna go back? Any changes made wouldn't be saved.")
                    .setTitle(Html.fromHtml("<font color='#ff0f0f'>BACK CONFIRMATION</font>"))
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            AddCustomer.super.onBackPressed();
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    })

                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                        }
                    })
                    .create();


            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {

                    dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.red));
                    dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.cancel_grey));

                    dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
                    dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);

                    dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextSize(17);
                    dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setTextSize(17);

                }
            });

            dialog.show();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        sAddress = new Address();
        addAddress = false;
        madeChanges = false;
    }

    public static boolean isMadeChanges() {
        return madeChanges;
    }

    public static void setMadeChanges(boolean madeChanges) {
        AddCustomer.madeChanges = madeChanges;
    }
}
