package com.example.filterapp.classes;

public class CustomerDetails extends UserDetails {
    String note;


    public CustomerDetails() {
        this("", "", "", "", "");
    }

    public CustomerDetails(String fName, String lName, String mobile, String email, String note) {
        super(fName, lName, mobile, email);
        this.note = note;
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


}
