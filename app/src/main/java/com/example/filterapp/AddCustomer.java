package com.example.filterapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
    Dialog loadingDialog;


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
            DocumentReference userDetails = db.collection("customerDetails").document("sorted")
                    .collection(customerID.substring(0, 1)).document(customerID);
            userDetails.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    title.setText("Customer Details");
                    btAdd.setText("Update Customer Details");
                    CustomerDetails customerDetails = documentSnapshot.toObject(CustomerDetails.class);
                    mFName.setText(customerDetails.getfName());
                    mLName.setText(customerDetails.getlName());
                    mEmail.setText(customerDetails.getEmail());
                    mMobile.setText(customerDetails.getMobile());
                    mNote.setText(customerDetails.getNote());
                    mFName.setEnabled(false);
                    mLName.setEnabled(false);
                    mMobile.setEnabled(false);
                }
            });

            DocumentReference userAddress = db.collection("customerDetails").document("sorted")
                    .collection(customerID.substring(0, 1)).document(customerID)
                    .collection("address").document("address");
            userAddress.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    addSign.setVisibility(View.INVISIBLE);
                    Address address = documentSnapshot.toObject(Address.class);
                    storeAddress(address);
                    mAddress.setText(address.formatedAddress());
                }
            });
        }
    }

    private void storeAddress(Address address) {
        sAddress = address;
        addAddress = true;
    }

    public void addCustomer(View view) {
        String fName, lName, email, mobile, note;
        fName = mFName.getText().toString().trim();
        lName = mLName.getText().toString().trim();
        email = mEmail.getText().toString().trim();
        mobile = mMobile.getText().toString().trim();
        note = mNote.getText().toString();

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

        if (email.isEmpty()) {
            mEmail.setError("Email cannot be empty");
            mEmail.requestFocus();
            return;
        }

        //Check if the email format is correct
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("Wrong email format");
            mEmail.requestFocus();
            return;
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

        //Make the loading dialog not dismissible
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

        customerDetails = new CustomerDetails(capitalize(fName), capitalize(lName), email, mobile, note, false);

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
                        intent.putExtra("customerID", documentID);
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
        super.onBackPressed();
        finish();
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
