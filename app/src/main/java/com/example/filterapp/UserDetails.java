package com.example.filterapp;

import java.util.List;

public class UserDetails {
    String fName;
    String lName;
    String mobile;

    public UserDetails(String fName, String lName, String mobile) {
        this.fName = fName;
        this.lName = lName;
        this.mobile = mobile;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


}