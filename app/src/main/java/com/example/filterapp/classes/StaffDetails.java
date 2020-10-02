package com.example.filterapp.classes;

import com.example.filterapp.classes.UserDetails;

public class StaffDetails extends UserDetails {
    String position;
    String branch;
    boolean Google;
    boolean deleted;

    public StaffDetails(){
        super("", "", "", "");
        position="";
        branch="";
        Google = false;
        deleted = false;
    }

    public StaffDetails(String fName, String lName, String mobile, String position, String branch, String email, boolean google, boolean deleted) {
        super(fName, lName, mobile, email);
        this.position = position;
        this.branch = branch;
        Google = google;
        this.deleted = deleted;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public boolean isGoogle() {
        return Google;
    }

    public void setGoogle(boolean google) {
        Google = google;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}

