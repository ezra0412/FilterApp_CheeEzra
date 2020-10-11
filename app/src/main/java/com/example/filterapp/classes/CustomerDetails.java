package com.example.filterapp.classes;


import android.os.Parcel;
import android.os.Parcelable;

public class CustomerDetails extends UserDetails implements Parcelable {
    String note;


    public CustomerDetails() {
        this("", "", "", "", "", false);
    }

    public CustomerDetails(String fName, String lName, String mobile, String email, String note, boolean deleted) {
        super(fName, lName, mobile, email, deleted);
        this.note = note;
    }


    protected CustomerDetails(Parcel in) {
        fName = in.readString();
        lName = in.readString();
        mobile = in.readString();
        email = in.readString();
        deleted = in.readByte() != 0;
        note = in.readString();
    }

    public static final Creator<CustomerDetails> CREATOR = new Creator<CustomerDetails>() {
        @Override
        public CustomerDetails createFromParcel(Parcel in) {
            return new CustomerDetails(in);
        }

        @Override
        public CustomerDetails[] newArray(int size) {
            return new CustomerDetails[size];
        }
    };

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(fName);
        parcel.writeString(lName);
        parcel.writeString(mobile);
        parcel.writeString(email);
        parcel.writeInt(deleted ? 1 : 0);
        parcel.writeString(note);
    }
}
