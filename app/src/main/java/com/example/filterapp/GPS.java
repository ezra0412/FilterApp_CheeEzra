package com.example.filterapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.filterapp.classes.Address;
import com.example.filterapp.classes.CustomerDetails;
import com.example.filterapp.classes.JavaMailAPI;
import com.example.filterapp.fcm.AppNotification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.filterapp.MainActivity.getStaffDetailsStatic;

public class GPS extends AppCompatActivity {
    String name, mobile, address;
    Double lan, lon;
    String fName;
    String customerID;
    CardView CVcustomerDetails;
    TextView mName, mPhone, mAddress, navigateBy, mFName, mMobile;
    EditText etFName, etMobile;
    Button mGoogle, mWaze, mSearch;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g_p_s);
        lan = getIntent().getDoubleExtra("lan", 0);
        lon = getIntent().getDoubleExtra("lon", 0);
        name = getIntent().getStringExtra("name");
        mobile = getIntent().getStringExtra("mobile");
        address = getIntent().getStringExtra("address");
        requestQueue = Volley.newRequestQueue(this);

        CVcustomerDetails = findViewById(R.id.cv_userDetails_gps);
        mName = findViewById(R.id.tv_name_gps);
        mPhone = findViewById(R.id.tv_phone_gps);
        mAddress = findViewById(R.id.tv_address_gps);
        navigateBy = findViewById(R.id.tv_navigateBy_gps);
        mGoogle = findViewById(R.id.bt_googleMap_gps);
        mWaze = findViewById(R.id.bt_waze_gps);
        mFName = findViewById(R.id.tv_fName_gps);
        mMobile = findViewById(R.id.tv_mobile_gps);
        etFName = findViewById(R.id.et_fName_gps);
        etMobile = findViewById(R.id.et_mobile_gps);
        mSearch = findViewById(R.id.bt_search_gps);

        if (lan == (0) && lon == (0) &&
                name.equalsIgnoreCase("non") && mobile.equalsIgnoreCase("non")
                && address.equalsIgnoreCase("non")) {
            mFName.setVisibility(View.VISIBLE);
            mMobile.setVisibility(View.VISIBLE);
            etFName.setVisibility(View.VISIBLE);
            etMobile.setVisibility(View.VISIBLE);
            mSearch.setVisibility(View.VISIBLE);

        } else {
            customerID = getIntent().getStringExtra("customerID");
            CVcustomerDetails.setVisibility(View.VISIBLE);
            navigateBy.setVisibility(View.VISIBLE);
            mGoogle.setVisibility(View.VISIBLE);
            mWaze.setVisibility(View.VISIBLE);
            mName.setText(name);
            mPhone.setText(mobile);
            mAddress.setText(address);
        }
    }

    public void back(View view) {
        super.onBackPressed();
    }

    public void search(View view) {

        closeKeyboard();

        fName = etFName.getText().toString().trim();
        mobile = etMobile.getText().toString().trim();

        if (fName.isEmpty()) {
            etFName.setError("Customer first name cannot be empty");
            etFName.requestFocus();
            return;
        }

        if (mobile.isEmpty()) {
            etMobile.setError("Customer mobile cannot be empty");
            etMobile.requestFocus();
            return;

        }

        if (!Patterns.PHONE.matcher(mobile).matches()) {
            etMobile.setError("Invalid mobile format");
            etMobile.requestFocus();
            return;
        }

        customerID = fName.substring(0, 1).toLowerCase() + mobile;

        DocumentReference checkCustomer = db.collection("customerDetails").document("sorted").collection(fName.substring(0, 1).toLowerCase()).document(fName.substring(0, 1).toLowerCase() + mobile);
        checkCustomer.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        setAddressDB();
                        CustomerDetails customerDetails = documentSnapshot.toObject(CustomerDetails.class);

                        setData(customerDetails);

                        mFName.setVisibility(View.GONE);
                        mMobile.setVisibility(View.GONE);
                        etFName.setVisibility(View.GONE);
                        etMobile.setVisibility(View.GONE);
                        mSearch.setVisibility(View.GONE);

                        CVcustomerDetails.setVisibility(View.VISIBLE);
                        navigateBy.setVisibility(View.VISIBLE);
                        mGoogle.setVisibility(View.VISIBLE);
                        mWaze.setVisibility(View.VISIBLE);
                    } else {
                        etFName.setError("Customer details not found");
                        etFName.requestFocus();
                    }
                } else {
                    Toast.makeText(GPS.this, "Error, please try again later", Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    public void setAddressDB() {
        DocumentReference address = db.collection("customerDetails").document("sorted")
                .collection(fName.substring(0, 1).toLowerCase()).document(fName.substring(0, 1).toLowerCase() + mobile)
                .collection("address").document("address");
        address.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Address addressDB = documentSnapshot.toObject(Address.class);
                setAddress(addressDB);
            }
        });
    }

    private void setAddress(Address address) {
        mAddress.setText(address.formatedAddress());
        lan = address.getLan();
        lon = address.getLon();
    }

    private void setData(CustomerDetails customerDetails) {
        mName.setText(customerDetails.fullName());
        mPhone.setText(customerDetails.getMobile());

    }

    public void google(View view) {
        String title = "Staff Start Navigation";
        String body = getStaffDetailsStatic().fullName() + " started navigation to " + mName.getText().toString().trim() + " using Google Map.";
        AppNotification sendAppNotification = new AppNotification();
        requestQueue.add(sendAppNotification.sendNotification("staffStartNavigation", title, body,"5","",customerID,"",""));

        getEmail("Google map");
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + lan + "," + lon + "&mode=d"));
        i.setPackage("com.google.android.apps.maps");
        startActivity(i);
    }


    public void waze(View view) {

        String title = "Staff Started Navigation";
        String body = getStaffDetailsStatic().fullName() + " started navigation to " + mName.getText().toString().trim() + " using Waze.";
        AppNotification sendAppNotification = new AppNotification();
        requestQueue.add(sendAppNotification.sendNotification("staffStartNavigation", title, body,"5","",customerID,"",""));

        getEmail("Waze");
        String url = "https://waze.com/ul?q=" + lan + "," + lon + "&navigate=yes";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void getEmail(final String option) {
        CollectionReference adminEmails = db.collection("adminDetails").document("adminList").collection("emailNotificationPreference");
        adminEmails.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.getBoolean("staffStartNavigation")) {
                        sendNotice(documentSnapshot.getString("email"), option);
                    }
                }
            }
        });
    }

    public void sendNotice(String email, String option) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss ");
        Date dt = new Date();
        String message;

        message = getStaffDetailsStatic().fullName() + " started navigating to " + mName.getText().toString().trim() + " at " + formatter.format(dt) + " using " + option + ".\n\n"
                + "\t------Customer Details------\n"
                + "\tName: " + mName.getText().toString().trim() + "\n"
                + "\tMobile: " + mMobile.getText().toString().trim() + "\n"
                + "\tAddress: \n"
                + "\t" + mAddress.getText().toString().trim() + "\n";


        JavaMailAPI javaMailAPI = new JavaMailAPI(this, email,
                "Start drive", message);

        javaMailAPI.execute();
    }

    public void call(View view){
        String phonenNumber = mMobile.getText().toString().trim();
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phonenNumber));
        startActivity(intent);
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
