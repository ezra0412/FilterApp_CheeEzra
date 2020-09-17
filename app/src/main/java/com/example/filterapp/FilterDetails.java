package com.example.filterapp;

public class FilterDetails {
    private String invoiceNumber;
    private String InvoiceNum;
    private String Mobile;
    private String FModel;
    private String Commission;
    private String Price;
    private String Note;
    private String Filter1;
    private String Filter2;
    private String Filter3;
    private String Filter4;
    private String Filter5;

    public FilterDetails(String invoiceNumber, String invoiceNum, String mobile, String FModel, String commission,
                         String price, String note, String filter1, String filter2, String filter3, String filter4, String filter5) {
        this.invoiceNumber = invoiceNumber;
        InvoiceNum = invoiceNum;
        Mobile = mobile;
        this.FModel = FModel;
        Commission = commission;
        Price = price;
        Note = note;
        Filter1 = filter1;
        Filter2 = filter2;
        Filter3 = filter3;
        Filter4 = filter4;
        Filter5 = filter5;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceNum() {
        return InvoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        InvoiceNum = invoiceNum;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getFModel() {
        return FModel;
    }

    public void setFModel(String FModel) {
        this.FModel = FModel;
    }

    public String getCommission() {
        return Commission;
    }

    public void setCommission(String commission) {
        Commission = commission;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getFilter1() {
        return Filter1;
    }

    public void setFilter1(String filter1) {
        Filter1 = filter1;
    }

    public String getFilter2() {
        return Filter2;
    }

    public void setFilter2(String filter2) {
        Filter2 = filter2;
    }

    public String getFilter3() {
        return Filter3;
    }

    public void setFilter3(String filter3) {
        Filter3 = filter3;
    }

    public String getFilter4() {
        return Filter4;
    }

    public void setFilter4(String filter4) {
        Filter4 = filter4;
    }

    public String getFilter5() {
        return Filter5;
    }

    public void setFilter5(String filter5) {
        Filter5 = filter5;
    }
}
