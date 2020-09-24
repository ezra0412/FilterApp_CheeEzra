package com.example.filterapp;

public class Address {
    String houseNumber;
    String level;
    String block;
    String building;
    String streetName;
    String garden;
    String area;
    String city;
    String postCode;
    String state;
    String addressNote;

    public Address() {
        this("", "", "", "", "", "", "", "", "", "", "");
    }

    public Address(String houseNumber, String level, String block, String building, String streetName, String garden, String area, String city, String postCode, String state, String addressNote) {
        this.houseNumber = houseNumber;
        this.level = level;
        this.block = block;
        this.building = building;
        this.streetName = streetName;
        this.garden = garden;
        this.area = area;
        this.city = city;
        this.postCode = postCode;
        this.state = state;
        this.addressNote = addressNote;
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

    public String getGarden() {
        return garden;
    }

    public void setGarden(String garden) {
        this.garden = garden;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getAddressNote() {
        return addressNote;
    }

    public void setAddressNote(String addressNote) {
        this.addressNote = addressNote;
    }
}
