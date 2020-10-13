package com.example.filterapp.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Address implements Parcelable {
    String houseNumber;
    String level;
    String block;
    String building;
    String streetName;
    String area;
    String postCode;
    String state;
    double lan;
    double lon;

    public Address() {
        this("", "", "", "", "", "", "", "", 0.0, 0.0);
    }

    public Address(String houseNumber, String level, String block, String building, String streetName, String area, String postCode, String state, double lan, double lon) {
        this.houseNumber = houseNumber;
        this.level = level;
        this.block = block;
        this.building = building;
        this.streetName = streetName;
        this.area = area;
        this.postCode = postCode;
        this.state = state;
        this.lan = lan;
        this.lon = lon;
    }

    protected Address(Parcel in) {
        houseNumber = in.readString();
        level = in.readString();
        block = in.readString();
        building = in.readString();
        streetName = in.readString();
        area = in.readString();
        postCode = in.readString();
        state = in.readString();
        lan = in.readDouble();
        lon = in.readDouble();
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }


    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getLan() {
        return lan;
    }

    public void setLan(double lan) {
        this.lan = lan;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String formatedAddress() {
        String mBlock = "", mLevel = "", mBuilding = "";
        if (!getBlock().equalsIgnoreCase("")) {
            mBlock = "Block: " + getBlock() + " , ";
        }
        if (!getLevel().equalsIgnoreCase("")) {
            mLevel = "Level: " + getLevel() + " , ";
        }
        if (!building.equalsIgnoreCase("")) {
            mBuilding = "Condo / Building: " + getBuilding() + " , ";
        }

        return mBlock + mLevel + mBuilding + " House number " + getHouseNumber() + " , " + getStreetName() + " , " + getPostCode() + " " + getArea() + " , " + getState();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(houseNumber);
        parcel.writeString(level);
        parcel.writeString(block);
        parcel.writeString(building);
        parcel.writeString(streetName);
        parcel.writeString(area);
        parcel.writeString(postCode);
        parcel.writeString(state);
        parcel.writeDouble(lan);
        parcel.writeDouble(lon);
    }
}
