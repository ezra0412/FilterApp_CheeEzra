package com.example.filterapp;

public class BtLongDoubleItem implements Comparable<BtLongDoubleItem> {
    private String item1;
    private String item2;

    public BtLongDoubleItem(String item1, String item2) {
        this.item1 = item1;
        this.item2 = item2;
    }

    public String getItem1() {
        return item1;
    }

    public void setItem1(String item1) {
        this.item1 = item1;
    }

    public String getItem2() {
        return item2;
    }

    public void setItem2(String item2) {
        this.item2 = item2;
    }

    @Override
    public int compareTo(BtLongDoubleItem o) {
        return this.getItem1().compareTo(o.getItem1());
    }
}
