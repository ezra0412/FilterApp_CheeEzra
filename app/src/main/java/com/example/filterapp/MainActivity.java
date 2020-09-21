package com.example.filterapp;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.filterapp.menu.DrawerAdapter;
import com.example.filterapp.menu.DrawerItem;
import com.example.filterapp.menu.SimpleItem;
import com.example.filterapp.menu.SpaceItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {
    TabLayout tabLayout;
    TabItem generateQR, scanQR;
    ViewPager viewPager;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private SlidingRootNav slidingRootNav;
    private String[] screenTitles;
    private Drawable[] screenIcons;

    private static final int account = 0;
    private static final int faq = 1;
    private static final int logout = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tl_main);
        generateQR = findViewById(R.id.tab_item_generateQR_main);
        scanQR = findViewById(R.id.tab_item_scanQR_main);
        viewPager = findViewById(R.id.vp_main);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.tb_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(true)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .withDragDistance(150)
                .withRootViewScale(0.8f)
                .withRootViewElevation(10)
                .withRootViewYTranslation(4)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();
        SlidingRootNav changeName = slidingRootNav.getLayout();
        //TextView ChangeName = changeName.findViewById*

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(account).setChecked(true),
                createItemFor(faq),
                new SpaceItem(48),
                createItemFor(logout)));
        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

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

    @Override
    public void onItemSelected(int position) {
        if (position == logout) {
            logout();
        } else if (position == account) {
            Toast.makeText(MainActivity.this, "My account", Toast.LENGTH_LONG).show();
        }
        slidingRootNav.closeMenu();

    }

    @SuppressWarnings("rawtypes")
    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.light_green))
                .withTextTint(color(R.color.light_green))
                .withSelectedIconTint(color(R.color.light_green))
                .withSelectedTextTint(color(R.color.light_green));
    }


    public void addCustomer(View view) {
        Intent intent = new Intent(MainActivity.this, AddCustomer.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.menuScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.menuScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }


    public void logout() {
        mAuth.signOut();
        startActivity(new Intent(MainActivity.this, LoginPage.class));
        finish();
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

}
