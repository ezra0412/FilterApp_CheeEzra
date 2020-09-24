package com.example.filterapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddCustomer extends AppCompatActivity {
    EditText mFName, mLName, mEmail, mMobile, mNote;
    TextView mAddress;
    ImageView addSign;
    static boolean addAddress = false;
    static Address sAddress = new Address();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CustomerDetails customerDetails;
    String documentCollection, documentID;
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


        if (!addAddress){
            Toast.makeText(AddCustomer.this,"Please add customer address",Toast.LENGTH_SHORT).show();
            return;
        }

        if (note.isEmpty()) {
            note = "No note provided";
        }

        DocumentReference checkCustomer = db.collection("customerDetails").document("sorted").collection(fName.substring(0, 1).toLowerCase()).document(fName.substring(0, 1).toLowerCase() + mobile);
        checkCustomer.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        mMobile.setText("Customer registered before");
                        mMobile.requestFocus();
                        return;
                    }
                }
            }
        });

        customerDetails = new CustomerDetails(capitalize(fName), capitalize(lName), email, mobile, note);

        documentCollection = fName.substring(0, 1).toLowerCase();
        documentID = documentCollection + mobile;

        DocumentReference customerAddress = db.collection("customerDetails").document("sorted").collection(documentCollection).document(documentID).collection("address").document("address");
        customerAddress.set(sAddress).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                DocumentReference customer = db.collection("customerDetails").document("sorted").collection(documentCollection).document(documentID);
                customer.set(customerDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddCustomer.this, "Customer added", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddCustomer.this, "Error please try again later", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
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
            String houseNum, block, level, building, streetName, garden,
                    area, city, postCode, state;

            houseNum = sAddress.getHouseNumber();
            block = sAddress.getBlock();
            level = sAddress.getLevel();
            building = sAddress.getBuilding();
            streetName = sAddress.getStreetName();
            garden = sAddress.getGarden();
            area = sAddress.getArea();
            city = sAddress.getCity();
            postCode = sAddress.getPostCode();
            state = sAddress.getState();

            String formatedAddress = "";

            if (!block.equalsIgnoreCase("")) {
                block = "Block: " + block + " , ";
            }
            if (!level.equalsIgnoreCase("")) {
                level = "Level: " + level + " , ";
            }
            if (!building.equalsIgnoreCase("")) {
                building = "Condo / Building: " + building + " , ";
            }
            if (!city.equalsIgnoreCase("")) {
                city = city + " , ";
            }
            if (!garden.equalsIgnoreCase("")) {
                garden = garden + " , ";
            }
            formatedAddress = block + level + building + " House number " + houseNum + " , " + streetName + " , " + garden + area + city + postCode + " , " + state;
            addSign.setVisibility(View.INVISIBLE);
            mAddress.setText(formatedAddress);
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
        startActivity(intent);
    }

    public void back(View view) {
        super.onBackPressed();
        finish();
    }
}
