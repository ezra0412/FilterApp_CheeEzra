package com.example.filterapp.classes;

public class UserDetails implements Comparable<UserDetails> {
    String fName;
    String lName;
    String mobile;
    String email;
    boolean deleted;


    public UserDetails() {
        this("", "", "", "", false);
    }

    public UserDetails(String fName, String lName, String mobile, String email, boolean deleted) {
        this.fName = fName;
        this.lName = lName;
        this.mobile = mobile;
        this.email = email;
        this.deleted = deleted;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String fullName() {
        return getfName() + " " + getlName();
    }


    @Override
    public int compareTo(UserDetails o) {
        return this.getfName().compareTo(o.getfName());
    }
}