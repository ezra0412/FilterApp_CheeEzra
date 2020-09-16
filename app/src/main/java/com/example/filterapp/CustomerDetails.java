package com.example.filterapp;

public class CustomerDetails extends UserDetails {
    String email;
    String note;
    Address address;

    public CustomerDetails(String fName, String lName, String email, String mobile, String note, Address address) {
        super(fName, lName, mobile);
        this.email=email;
        this.note = note;
        this.address = address;
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
}
