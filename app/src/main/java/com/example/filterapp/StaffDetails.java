package com.example.filterapp;

public class StaffDetails extends UserDetails {
    String position;
    String branch;
    boolean Google;

    public StaffDetails(){
        super("","","");
        position="";
        branch="";
        Google = false;
    }

    public StaffDetails(String fName, String lName, String mobile, String position, String branch, boolean Google) {
        super(fName, lName, mobile);
        this.position = position;
        this.branch = branch;
        this.Google = Google;
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

    public String fullName() {
        return getfName() + " " + getlName();
    }
}

