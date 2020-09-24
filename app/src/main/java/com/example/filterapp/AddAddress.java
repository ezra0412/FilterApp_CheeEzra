package com.example.filterapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.filterapp.AddCustomer.isAddAddress;
import static com.example.filterapp.AddCustomer.sAddress;
import static com.example.filterapp.AddCustomer.setAddAddress;
import static com.example.filterapp.AddCustomer.setsAddress;

public class AddAddress extends AppCompatActivity {
    Dialog stateDialog;
    TextView title, mState;
    EditText mHouseNum, mBlock, mLevel, mBuilding, mStreetName, mGarden,
            mArea, mCity, mPostCode;
    Button mDone;
    boolean stateSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        stateDialog = new Dialog(this);

        title = findViewById(R.id.tv_title_address);
        mState = findViewById(R.id.tv_state_address);
        mHouseNum = findViewById(R.id.et_houseNum_address);
        mBlock = findViewById(R.id.et_block_address);
        mLevel = findViewById(R.id.et_level_address);
        mBuilding = findViewById(R.id.et_building_address);
        mStreetName = findViewById(R.id.et_streetName_addAddress);
        mGarden = findViewById(R.id.et_garden_addAddress);
        mArea = findViewById(R.id.et_area_address);
        mCity = findViewById(R.id.et_city_address);
        mPostCode = findViewById(R.id.et_postCode_address);
        mDone = findViewById(R.id.bt_done_addAddress);

        if (isAddAddress()) {
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

            mHouseNum.setText(houseNum);
            mStreetName.setText(streetName);
            mArea.setText(area);
            mPostCode.setText(postCode);
            mState.setText(state);
            if (!block.equalsIgnoreCase("")) {
                mBlock.setText(block);
            }
            if (!level.equalsIgnoreCase("")) {
                mLevel.setText(level);
            }
            if (!building.equalsIgnoreCase("")) {
                mBuilding.setText(building);
            }
            if (!city.equalsIgnoreCase("")) {
                mCity.setText(city);
            }
            if (!garden.equalsIgnoreCase("")) {
                mGarden.setText(garden);
            }
        }

    }

    public void done(View view) {
        String houseNum, block, level, building, streetName, garden, area,
                city, postCode, state;

        houseNum = mHouseNum.getText().toString().trim();
        block = mBlock.getText().toString().trim();
        level = mLevel.getText().toString().trim();
        building = mBuilding.getText().toString().trim();
        streetName = mStreetName.getText().toString().trim();
        garden = mGarden.getText().toString().trim();
        area = mArea.getText().toString().trim();
        city = mCity.getText().toString().trim();
        state = mState.getText().toString().trim();
        postCode = mPostCode.getText().toString().trim();

        if (houseNum.isEmpty()) {
            mHouseNum.setError("House number cannot be empty");
            mHouseNum.requestFocus();
            return;
        }

        if (streetName.isEmpty()) {
            mStreetName.setError("Street name cannot be empty");
            mStreetName.requestFocus();
            return;
        }

        if (area.isEmpty()) {
            mArea.setError("Area cannot be empty");
            mArea.requestFocus();
            return;
        }

        if (city.isEmpty()) {
            mCity.setError("City cannot be empty");
            mCity.requestFocus();
            return;
        }

        if (postCode.length() < 5) {
            mPostCode.setError("Post code cannot be less than 5 number");
            mPostCode.requestFocus();
            return;
        }

        if (postCode.length() > 5) {
            mPostCode.setError("Post code cannot be more than 5 number");
            mPostCode.requestFocus();
            return;
        }

        if (!stateSelected) {
            Toast.makeText(AddAddress.this, " No state chose", Toast.LENGTH_LONG).show();
            return;
        }

        Address address = new Address();

        address.setHouseNumber(capitalize(houseNum));
        address.setStreetName(capitalize(streetName));
        address.setArea(capitalize(area));
        address.setCity(capitalize(city));
        address.setPostCode(postCode);
        address.setState(state);

        if (!block.isEmpty()) {
            address.setBlock(capitalize(block));
        }

        if (!level.isEmpty()) {
            address.setLevel(level.toUpperCase());
        }

        if (!building.isEmpty()) {
            address.setBuilding(capitalize(building));
        }

        if (!garden.isEmpty()) {
            address.setGarden(capitalize(garden));
        }

        setsAddress(address);
        setAddAddress(true);
        super.onBackPressed();

    }

    public void back(View view) {
        super.onBackPressed();
    }

    public void choseState(View view) {
        Button perlis, kedah, penang, perak, kelantan, terengganu, pahang, selangor,
                nSembilan, melaka, johor, sabah, sarawak;
        ImageView close;

        mState.setBackground(getDrawable(R.drawable.spinner_open_short));

        stateDialog.setContentView(R.layout.pop_up_state);
        stateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        stateDialog.show();

        perlis = stateDialog.findViewById(R.id.bt_perlis_statePU);
        kedah = stateDialog.findViewById(R.id.bt_kedah_statePU);
        penang = stateDialog.findViewById(R.id.bt_penang_statePU);
        perak = stateDialog.findViewById(R.id.bt_perak_statePU);
        kelantan = stateDialog.findViewById(R.id.bt_kelantan_statePU);
        terengganu = stateDialog.findViewById(R.id.bt_terengganu_statePU);
        pahang = stateDialog.findViewById(R.id.bt_pahang_statePU);
        selangor = stateDialog.findViewById(R.id.bt_selangor_statePU);
        nSembilan = stateDialog.findViewById(R.id.bt_nSembilan_statePU);
        melaka = stateDialog.findViewById(R.id.bt_melaka_statePU);
        johor = stateDialog.findViewById(R.id.bt_johor_statePU);
        sabah = stateDialog.findViewById(R.id.bt_sabah_statePU);
        sarawak = stateDialog.findViewById(R.id.bt_sarawak_statePU);
        close = stateDialog.findViewById(R.id.img_close_statePU);

        stateDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (stateSelected)
                    mState.setBackground(getDrawable(R.drawable.spinner_close_short));

                else {
                    Toast.makeText(AddAddress.this, " No State Chose", Toast.LENGTH_LONG).show();
                    mState.setBackground(getDrawable(R.drawable.spinner_close_short));
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stateDialog.dismiss();
            }
        });

        perlis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mState.setText("  Perlis");
                stateSelected = true;
                stateDialog.dismiss();
            }
        });

        kedah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mState.setText("  Kedah");
                stateSelected = true;
                stateDialog.dismiss();
            }
        });

        penang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mState.setText("  Penang");
                stateSelected = true;
                stateDialog.dismiss();
            }
        });

        perak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mState.setText("  Perak");
                stateDialog.dismiss();
            }
        });

        kelantan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mState.setText("  Kelantan");
                stateSelected = true;
                stateDialog.dismiss();
            }
        });

        terengganu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mState.setText("  Terengganu");
                stateSelected = true;
                stateDialog.dismiss();
            }
        });

        pahang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mState.setText("  Pahang");
                stateSelected = true;
                stateDialog.dismiss();
            }
        });

        selangor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mState.setText("  Selangor");
                stateSelected = true;
                stateDialog.dismiss();
            }
        });

        nSembilan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mState.setText("  N.Sembilan");
                stateSelected = true;
                stateDialog.dismiss();
            }
        });

        melaka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mState.setText("  Melaka");
                stateSelected = true;
                stateDialog.dismiss();
            }
        });

        johor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mState.setText("  Johor");
                stateSelected = true;
                stateDialog.dismiss();
            }
        });

        sabah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mState.setText("  Sabah");
                stateSelected = true;
                stateDialog.dismiss();
            }
        });

        sarawak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mState.setText("  Sarawak");
                stateSelected = true;
                stateDialog.dismiss();
            }
        });
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
}
