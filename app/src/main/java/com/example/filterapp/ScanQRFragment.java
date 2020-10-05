package com.example.filterapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScanQRFragment extends Fragment {

    public ScanQRFragment() {
        // Required empty public constructor
    }

    CodeScanner codeScanner;
    CodeScannerView codeScannerView;
    Activity activity;
    TextView message;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         v = inflater.inflate(R.layout.fragment_scan_q_r, container, false);
         activity = getActivity();

        codeScannerView = v.findViewById(R.id.scannerCam);
        message = v.findViewById(R.id.tv_message_scanQR);

        codeScanner = new CodeScanner(getActivity(), codeScannerView);
        requestForCamera();

        return v;
    }

    @Override
    public void onPause() {
        codeScanner.releaseResources();
        super.onPause();    }

    private void requestForCamera(){

        Dexter.withContext(activity).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                message.setVisibility(View.INVISIBLE);
                codeScanner.startPreview();
                startCamera();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                message.setVisibility(View.VISIBLE);
                message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Context context = getContext();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                        intent.setData(uri);
                        context.startActivity(intent);
                        requestForCamera();
                    }
                });
                final Snackbar snackbar = Snackbar.make(v,"Camera permission is needed in order to scan QR code", Snackbar.LENGTH_LONG);
                snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                snackbar.setDuration(10000);
                snackbar.setActionTextColor(getResources().getColor(R.color.red));
                snackbar.setAction("Setting", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                        Context context = getContext();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                        intent.setData(uri);
                        context.startActivity(intent);
                        requestForCamera();
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

    public void startCamera(){
        message.setVisibility(View.INVISIBLE);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final String scanResult = result.getText();
                        String year = scanResult.substring(0, 4);
                        String month = scanResult.substring(4, 7);

                        DocumentReference filterDetailsDB = db.collection("sales").document(year).collection(month).document(scanResult);
                        filterDetailsDB.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if (documentSnapshot.exists()) {
                                        message.setVisibility(View.INVISIBLE);
                                        Intent intent = new Intent(getActivity(), FilterDetail.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("documentID", scanResult);
                                        startActivity(intent);
                                    } else {
                                        message.setText("No result found");
                                        message.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        });
                    }
                });
            }
        });

        codeScannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message.setVisibility(View.INVISIBLE);
                codeScanner.startPreview();
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        Dexter.withContext(activity).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                message.setVisibility(View.INVISIBLE);
                codeScanner.startPreview();
                startCamera();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                message.setVisibility(View.VISIBLE);
                message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Context context = getContext();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                        intent.setData(uri);
                        context.startActivity(intent);
                        requestForCamera();
                    }
                });
                final Snackbar snackbar = Snackbar.make(v, "Camera permission is needed in order to scan QR code", Snackbar.LENGTH_LONG);
                snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                snackbar.setDuration(10000);
                snackbar.setActionTextColor(getResources().getColor(R.color.red));
                snackbar.setAction("Setting", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                        Context context = getContext();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                        intent.setData(uri);
                        context.startActivity(intent);
                        requestForCamera();
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
}
