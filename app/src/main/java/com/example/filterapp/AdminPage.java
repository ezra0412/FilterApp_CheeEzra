package com.example.filterapp;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filterapp.menu.DrawerAdapter;
import com.example.filterapp.menu.DrawerItem;
import com.example.filterapp.menu.SimpleItem;
import com.example.filterapp.menu.SpaceItem;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;

import static com.example.filterapp.MainActivity.setVerfiedAdmin;

public class AdminPage extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {
    private String[] screenTitles;
    private Drawable[] screenIcons;

    private static final int notification = 0;
    private static final int changePassword = 1;
    private static final int manageStaff = 2;
    private static final int manageCustomer = 3;
    private static final int back = 5;


    private SlidingRootNav slidingRootNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVerfiedAdmin(true);

        //At here
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_adminPage,
                new NotificationFragment()).commit();

        setContentView(R.layout.activity_admin_page);
        Toolbar toolbar = findViewById(R.id.tb_adminPage);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingRootNav.closeMenu();
            }
        });

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(true)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_admin)
                .withDragDistance(160)
                .withRootViewScale(0.8f)
                .withRootViewElevation(10)
                .withRootViewYTranslation(4)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(notification).setChecked(true),
                createItemFor(changePassword),
                createItemFor(manageStaff),
                createItemFor(manageCustomer),
                new SpaceItem(48),
                createItemFor(back)));
        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.list_admin);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(notification);

    }

    @Override
    public void onItemSelected(int position) {
        switch (position) {
            case notification:
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_adminPage,
                        new NotificationFragment()).commit();
                break;

            case changePassword:
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_adminPage,
                        new ChangePasswordFragment()).commit();
                break;


            case manageStaff:
                Intent staff = new Intent(AdminPage.this, StaffPositionChoice.class);
                staff.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(staff);

                break;

            case manageCustomer:
                Intent manageCus = new Intent(AdminPage.this, Abcd.class);
                manageCus.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(manageCus);

                break;

            case back:
                setVerfiedAdmin(false);
                Intent backIntent = new Intent(AdminPage.this, MainActivity.class);
                backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(backIntent);
                finish();
        }
        slidingRootNav.closeMenu();

    }

    @SuppressWarnings("rawtypes")
    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.button_purple))
                .withTextTint(color(R.color.black))
                .withSelectedIconTint(color(R.color.button_purple))
                .withSelectedTextTint(color(R.color.black));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.adminMenu);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.adminMenuIcons);
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

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }
}
