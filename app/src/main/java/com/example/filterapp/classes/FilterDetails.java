package com.example.filterapp.classes;


public class FilterDetails {
    private String invoiceNumber;
    private String mobile;
    private String fName;
    private String fModel;
    private String filter1;
    private String filter2;
    private String filter3;
    private String filter4;
    private String filter5;
    private String commission;
    private String price;
    private String note;
    private String salesID;
    private String filter1LC;
    private String filter2LC;
    private String filter3LC;
    private String filter4LC;
    private String filter5LC;
    private String wt;
    private String wt_s;
    private String fc_D;
    private String wtLC;
    private String wt_sLC;
    private String fc_DLC;
    private String imageLocation;
    private String dayBrought;
    private String timeBrought;


    public FilterDetails() {
        this("", "", "", "", "-", "-", "-", "-", "-", "",
                "", "", "", "No Record", "No Record", "No Record", "No Record", "No Record",
                "0", "0", "0", "No Record", "No Record", "No Record", "", "", ""/*bae i loveee u*/);
    }

    public FilterDetails(String invoiceNumber, String mobile, String fName, String fModel, String filter1, String filter2, String filter3,
                         String filter4, String filter5, String commission, String price, String note, String salesID, String filter1LC, String filter2LC,
                         String filter3LC, String filter4LC, String filter5LC, String wt, String wt_s, String fc_D, String wtLC, String wt_sLC, String fc_DLC,
                         String imageLocation, String dayBrought, String timeBrought) {
        this.invoiceNumber = invoiceNumber;
        this.mobile = mobile;
        this.fName = fName;
        this.fModel = fModel;
        this.filter1 = filter1;
        this.filter2 = filter2;
        this.filter3 = filter3;
        this.filter4 = filter4;
        this.filter5 = filter5;
        this.commission = commission;
        this.price = price;
        this.note = note;
        this.salesID = salesID;
        this.filter1LC = filter1LC;
        this.filter2LC = filter2LC;
        this.filter3LC = filter3LC;
        this.filter4LC = filter4LC;
        this.filter5LC = filter5LC;
        this.wt = wt;
        this.wt_s = wt_s;
        this.fc_D = fc_D;
        this.wtLC = wtLC;
        this.wt_sLC = wt_sLC;
        this.fc_DLC = fc_DLC;
        this.imageLocation = imageLocation;
        this.dayBrought = dayBrought;
        this.timeBrought = timeBrought;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getfModel() {
        return fModel;
    }

    public void setfModel(String fModel) {
        this.fModel = fModel;
    }

    public String getFilter1() {
        return filter1;
    }

    public void setFilter1(String filter1) {
        this.filter1 = filter1;
    }

    public String getFilter2() {
        return filter2;
    }

    public void setFilter2(String filter2) {
        this.filter2 = filter2;
    }

    public String getFilter3() {
        return filter3;
    }

    public void setFilter3(String filter3) {
        this.filter3 = filter3;
    }

    public String getFilter4() {
        return filter4;
    }

    public void setFilter4(String filter4) {
        this.filter4 = filter4;
    }

    public String getFilter5() {
        return filter5;
    }

    public void setFilter5(String filter5) {
        this.filter5 = filter5;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSalesID() {
        return salesID;
    }

    public void setSalesID(String salesID) {
        this.salesID = salesID;
    }

    public String getFilter1LC() {
        return filter1LC;
    }

    public void setFilter1LC(String filter1LC) {
        this.filter1LC = filter1LC;
    }

    public String getFilter2LC() {
        return filter2LC;
    }

    public void setFilter2LC(String filter2LC) {
        this.filter2LC = filter2LC;
    }

    public String getFilter3LC() {
        return filter3LC;
    }

    public void setFilter3LC(String filter3LC) {
        this.filter3LC = filter3LC;
    }

    public String getFilter4LC() {
        return filter4LC;
    }

    public void setFilter4LC(String filter4LC) {
        this.filter4LC = filter4LC;
    }

    public String getFilter5LC() {
        return filter5LC;
    }

    public void setFilter5LC(String filter5LC) {
        this.filter5LC = filter5LC;
    }

    public String getWt() {
        return wt;
    }

    public void setWt(String wt) {
        this.wt = wt;
    }

    public String getWt_s() {
        return wt_s;
    }

    public void setWt_s(String wt_s) {
        this.wt_s = wt_s;
    }

    public String getFc_D() {
        return fc_D;
    }

    public void setFc_D(String fc_D) {
        this.fc_D = fc_D;
    }

    public String getWtLC() {
        return wtLC;
    }

    public void setWtLC(String wtLC) {
        this.wtLC = wtLC;
    }

    public String getWt_sLC() {
        return wt_sLC;
    }

    public void setWt_sLC(String wt_sLC) {
        this.wt_sLC = wt_sLC;
    }

    public String getFc_DLC() {
        return fc_DLC;
    }

    public void setFc_DLC(String fc_DLC) {
        this.fc_DLC = fc_DLC;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    public String getDayBrought() {
        return dayBrought;
    }

    public void setDayBrought(String dayBrought) {
        this.dayBrought = dayBrought;
    }

    public String getTimeBrought() {
        return timeBrought;
    }

    public void setTimeBrought(String timeBrought) {
        this.timeBrought = timeBrought;
    }
}