package com.example.filterapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    TabItem generateQR, scanQR;
    ViewPager viewPager;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Toolbar tb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tl_main);
        generateQR = findViewById(R.id.tab_item_generateQR_main);
        scanQR = findViewById(R.id.tab_item_scanQR_main);
        viewPager = findViewById(R.id.vp_main);

        //tb = findViewById(R.id.tb_main);
        //  getSupportActionBar(tb);

        MovableFloatingActionButton fab = findViewById(R.id.fab_main);
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        fab.setCoordinatorLayout(lp);

        MainTabAdapter mainTabAdapter = new
                MainTabAdapter(getSupportFragmentManager(),
                tabLayout.getTabCount());


        viewPager.setAdapter(mainTabAdapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void addCustomer(View view) {
        Intent intent = new Intent(MainActivity.this, AddCustomer.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void logout(View view) {
        mAuth.signOut();
        startActivity(new Intent(MainActivity.this, LoginPage.class));
        finish();
    }

}
