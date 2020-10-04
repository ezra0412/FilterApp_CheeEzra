package com.example.filterapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class ForgetPassword extends AppCompatActivity {
    EditText mEmail;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Dialog loadingDialog, emailDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        mEmail = findViewById(R.id.et_email_forgetPassword);
        loadingDialog = new Dialog(this);
        emailDialog = new Dialog(this);
    }

    public void sendEmail(View view){
        String email = mEmail.getText().toString().trim();

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

        closeKeyboard();

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            loadingScreen();

                        }else if (task.getException()  instanceof FirebaseAuthInvalidUserException){
                            mEmail.setError("Invalid user");
                            mEmail.requestFocus();
                            return;
                        }
                    }
                });

    }

    /**
     * Display loading screen
     */
    public void loadingScreen() {
        ProgressBar progressBar;
        Sprite style = new Wave();
        loadingDialog.setContentView(R.layout.pop_up_loading_screen);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressBar = loadingDialog.findViewById(R.id.sk_loadingPU);
        progressBar.setIndeterminateDrawable(style);

        //Make the loading dialog not dismissible
        loadingDialog.setCanceledOnTouchOutside(false);

        loadingDialog.show();

        //Let the loading dialog run for 3 sec to make sure everything is prepared
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismiss();
                sendEmailNotic();

            }
        }, 3000);


    }

    public void sendEmailNotic() {

        emailDialog.setContentView(R.layout.pop_up_email_sent);
        emailDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //Make the loading dialog not dismissible
        emailDialog.setCanceledOnTouchOutside(false);

        emailDialog.show();

        //Let the loading dialog run for 4 sec to make sure everything is prepared
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                emailDialog.dismiss();
            }
        }, 4000);

        emailDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                ForgetPassword.super.onBackPressed();
                finish();
            }
        });
    }

    public void back(View view){
        super.onBackPressed();
        finish();
    }

    public void FQA (View view){
        Intent intent = new Intent(this, FAQ.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     * hide keyboard
     */
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
