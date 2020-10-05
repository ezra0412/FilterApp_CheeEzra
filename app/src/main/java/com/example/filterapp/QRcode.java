package com.example.filterapp;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRcode extends AppCompatActivity {
    ImageView qr;
    Bitmap qrBits;
    FileOutputStream outputStream;
    String documentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_rcode);
        documentID = getIntent().getStringExtra("documentID");
        qr = findViewById(R.id.img_qr_qrCode);

        QRGEncoder qrgEncoder = new QRGEncoder(documentID, null, QRGContents.Type.TEXT, 1000);
        qrBits = qrgEncoder.getBitmap();
        qr.setImageBitmap(qrBits);

    }

    public void back(View view) {
        super.onBackPressed();
    }

    public void saveImage(View view) {
        Dexter.withContext(this).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                ContextWrapper dir = new ContextWrapper(getApplicationContext());
                File directory = dir.getDir("Ashita_QrCodes", Context.MODE_PRIVATE);

                File file = new File(directory, documentID + ".jpg");

                try {
                    directory.mkdirs();
                    outputStream = new FileOutputStream(file);

                    qrBits.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.close();

                }

                catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(QRcode.this, "Qr code saved!", Toast.LENGTH_SHORT).show();
                Intent shareImage = new Intent(Intent.ACTION_SEND);
                shareImage.setType("image/jpeg");
                ByteArrayOutputStream byts = new ByteArrayOutputStream();
                qrBits.compress(Bitmap.CompressFormat.JPEG, 100, byts);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                        qrBits, documentID, documentID);
                Uri imageUri = Uri.parse(path);
                shareImage.putExtra(Intent.EXTRA_STREAM, imageUri);
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                View view = findViewById(R.id.activity_qrCode);
                final Snackbar snackbar = Snackbar.make(view, "File writing permission is needed in order to save QR code", Snackbar.LENGTH_LONG);
                snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                snackbar.setDuration(10000);
                snackbar.setActionTextColor(getResources().getColor(R.color.red));
                snackbar.setAction("Setting", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                        Context context = getApplicationContext();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                        intent.setData(uri);
                        context.startActivity(intent);
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

    public void shareImageBT(View view) {
        Intent shareImage = new Intent(Intent.ACTION_SEND);
        shareImage.setType("image/jpeg");
        ByteArrayOutputStream byts = new ByteArrayOutputStream();
        qrBits.compress(Bitmap.CompressFormat.JPEG, 100, byts);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                qrBits, documentID, documentID);
        Uri imageUri = Uri.parse(path);
        shareImage.putExtra(Intent.EXTRA_STREAM, imageUri);
        startActivity(Intent.createChooser(shareImage, "Share QR code via"));
    }
}


