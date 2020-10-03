package com.example.filterapp.classes;

public class StaffDetails extends UserDetails {
    String position;
    String branch;
    boolean Google;
    String token;

    public StaffDetails(){
        super("", "", "", "", false);
        position="";
        branch="";
        Google = false;
        deleted = false;
        token = "";
    }

    public StaffDetails(String fName, String lName, String mobile, String position, String branch, String email, boolean google, boolean deleted, String token) {
        super(fName, lName, mobile, email, deleted);
        this.position = position;
        this.branch = branch;
        Google = google;
        this.token = token;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

