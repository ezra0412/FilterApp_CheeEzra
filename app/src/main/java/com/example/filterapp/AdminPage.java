package com.example.filterapp;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
    private static final int cash = 2;
    private static final int manageStaff = 3;
    private static final int manageCustomer = 4;
    private static final int back = 6;


    private SlidingRootNav slidingRootNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVerfiedAdmin(true);

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
                createItemFor(cash),
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
                Toast.makeText(this, "notification", Toast.LENGTH_LONG).show();
                break;

            case changePassword:
                Toast.makeText(this, "changePassword", Toast.LENGTH_LONG).show();
                break;

            case cash:
                Toast.makeText(this, "cash", Toast.LENGTH_LONG).show();
                break;

            case manageStaff:
                Toast.makeText(this, "Manage stuff", Toast.LENGTH_LONG).show();
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
                .withIconTint(color(R.color.light_green))
                .withTextTint(color(R.color.light_green))
                .withSelectedIconTint(color(R.color.light_green))
                .withSelectedTextTint(color(R.color.light_green));
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
