package com.example.filterapp.classes;

public class ServiceDetails {
    private String serviceID;
    private String technicianID;
    private String servicePrice;
    private String serviceDate;
    private String serviceTime;
    private String note;
    private String changedFilter1;
    private String changedFilter2;
    private String changedFilter3;
    private String changedFilter4;
    private String changedFilter5;
    private String changedWt;
    private String changedWt_s;
    private String changedFC_D;


    public ServiceDetails() {
        this("", "", "", "", "", "",
                "-", "-", "-", "-", "-",
                "-", "-", "-");
    }

    public ServiceDetails(String serviceID, String technicianID, String servicePrice, String serviceDate, String serviceTime,
                          String note, String changedFilter1, String changedFilter2, String changedFilter3, String changedFilter4,
                          String changedFilter5, String changedWt, String changedWt_s, String changedFC_D) {
        this.serviceID = serviceID;
        this.technicianID = technicianID;
        this.servicePrice = servicePrice;
        this.serviceDate = serviceDate;
        this.serviceTime = serviceTime;
        this.note = note;
        this.changedFilter1 = changedFilter1;
        this.changedFilter2 = changedFilter2;
        this.changedFilter3 = changedFilter3;
        this.changedFilter4 = changedFilter4;
        this.changedFilter5 = changedFilter5;
        this.changedWt = changedWt;
        this.changedWt_s = changedWt_s;
        this.changedFC_D = changedFC_D;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getTechnicianID() {
        return technicianID;
    }

    public void setTechnicianID(String technicianID) {
        this.technicianID = technicianID;
    }



    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getChangedFilter1() {
        return changedFilter1;
    }

    public void setChangedFilter1(String changedFilter1) {
        this.changedFilter1 = changedFilter1;
    }

    public String getChangedFilter2() {
        return changedFilter2;
    }

    public void setChangedFilter2(String changedFilter2) {
        this.changedFilter2 = changedFilter2;
    }

    public String getChangedFilter3() {
        return changedFilter3;
    }

    public void setChangedFilter3(String changedFilter3) {
        this.changedFilter3 = changedFilter3;
    }

    public String getChangedFilter4() {
        return changedFilter4;
    }

    public void setChangedFilter4(String changedFilter4) {
        this.changedFilter4 = changedFilter4;
    }

    public String getChangedFilter5() {
        return changedFilter5;
    }

    public void setChangedFilter5(String changedFilter5) {
        this.changedFilter5 = changedFilter5;
    }

    public String getChangedWt() {
        return changedWt;
    }

    public void setChangedWt(String changedWt) {
        this.changedWt = changedWt;
    }

    public String getChangedWt_s() {
        return changedWt_s;
    }

    public void setChangedWt_s(String changedWt_s) {
        this.changedWt_s = changedWt_s;
    }

    public String getChangedFC_D() {
        return changedFC_D;
    }

    public void setChangedFC_D(String changedFC_D) {
        this.changedFC_D = changedFC_D;
    }
}
