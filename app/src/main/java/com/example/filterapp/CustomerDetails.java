package com.example.filterapp;

import java.util.List;

public class CustomerDetails extends UserDetails {
    String email;
    String note;

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

    public String fullName() {
        return getfName() + " " + getlName();
    }



}
