package com.example.filterapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.filterapp.adapter.FAQAdapter;
import com.example.filterapp.classes.FAQitem;

import java.util.ArrayList;
import java.util.List;

public class FAQ extends AppCompatActivity {
    RecyclerView faq;
    RecyclerView.LayoutManager layoutManager;
    List<FAQitem> faqItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_a_q);
        faq = findViewById(R.id.rv_faq);
        faq.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        faq.setLayoutManager(layoutManager);

        FAQitem item = new FAQitem("1. Forget password", "You can reset it by pressing the forget password text at the login page which then you can enter your email to receive a secure link to reset your password.", true);
        faqItems.add(item);

        FAQitem item2 = new FAQitem("1. Forget password", "You can reset it by pressing the forget password text at the login page which then you can enter your email to receive a secure link to reset your password.", true);
        faqItems.add(item2);

        FAQAdapter adapter = new FAQAdapter(faqItems);
        faq.setAdapter(adapter);
    }

    public void back(View view) {
        super.onBackPressed();
    }
}
