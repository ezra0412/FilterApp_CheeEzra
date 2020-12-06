package com.example.filterapp.geocoding;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeocodingLocation {
    private static final String TAG = "GeocodingLocation";
    private double al;
    private double lon;

    public void getAddressFromLocation(com.example.filterapp.classes.Address locationAddress, final Context context) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        String formattedAddress = "";

        if (!locationAddress.getBuilding().isEmpty()) {
            formattedAddress += locationAddress.getBuilding();
        } else {
            formattedAddress += locationAddress.getHouseNumber();
        }

        formattedAddress += locationAddress.getStreetName();
        formattedAddress += locationAddress.getPostCode();
        formattedAddress += locationAddress.getArea();
        formattedAddress += locationAddress.getState();

        try {
            List<Address> addressList = geocoder.getFromLocationName(formattedAddress + "&key= Your key", 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                setAl(address.getLatitude());
                setLon(address.getLongitude());
            }
        } catch (IOException e) {
            Log.e(TAG, "Unable to connect to Geocoder", e);
        }
    }

    public void setAl(double al) {
        this.al = al;
    }

    public double getAl() {
        return al;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
