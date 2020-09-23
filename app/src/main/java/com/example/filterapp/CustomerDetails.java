package com.example.filterapp;

import java.util.List;

public class CustomerDetails extends UserDetails {
    String email;
    String note;
    Address address;

    public CustomerDetails() {
        this("", "", "", "", "");
    }

    public CustomerDetails(String fName, String lName, String email, String mobile, String note) {
        super(fName, lName, mobile);
        this.email=email;
        this.note = note;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }


}
