package com.example.filterapp.classes;

public class Address {
    String houseNumber;
    String level;
    String block;
    String building;
    String streetName;
    String area;
    String postCode;
    String state;
    String lan;
    String lon;

    public Address() {
        this("", "", "", "", "", "", "", "", "", "");
    }

    public Address(String houseNumber, String level, String block, String building, String streetName, String area, String postCode, String state, String lan, String lon) {
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

    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
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


}
