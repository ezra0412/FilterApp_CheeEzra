package com.example.filterapp.classes;

public class CustomerDetails extends UserDetails {
    String note;


    public CustomerDetails() {
        this("", "", "", "", "", false);
    }

    public CustomerDetails(String fName, String lName, String mobile, String email, String note, boolean deleted) {
        super(fName, lName, mobile, email, deleted);
        this.note = note;
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


}
