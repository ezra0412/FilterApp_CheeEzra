package com.example.filterapp.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class StaffDetails extends UserDetails implements Parcelable {
    String position;
    String branch;
    boolean Google;
    String token;
    boolean profilePic;

    public StaffDetails(){
        super("", "", "", "", false);
        position="";
        branch="";
        Google = false;
        deleted = false;
        token = "";
        profilePic = false;
    }

    public StaffDetails(String fName, String lName, String mobile, String position, String branch, String email, boolean google, boolean deleted, String token, boolean profilePic ) {
        super(fName, lName, mobile, email, deleted);
        this.position = position;
        this.branch = branch;
        this.Google = google;
        this.token = token;
        this.profilePic = profilePic;
    }

    protected StaffDetails(Parcel in) {
        fName = in.readString();
        lName = in.readString();
        mobile = in.readString();
        email = in.readString();
        deleted = in.readByte() != 0;
        position = in.readString();
        branch = in.readString();
        Google = in.readByte() != 0;
        token = in.readString();
        profilePic = in.readByte() !=0;
    }

    public static final Creator<StaffDetails> CREATOR = new Creator<StaffDetails>() {
        @Override
        public StaffDetails createFromParcel(Parcel in) {
            return new StaffDetails(in);
        }

        @Override
        public StaffDetails[] newArray(int size) {
            return new StaffDetails[size];
        }
    };

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

    public boolean isProfilePic() {
        return profilePic;
    }

    public void setProfilePic(boolean profilePic) {
        this.profilePic = profilePic;
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
        parcel.writeString(position);
        parcel.writeString(branch);
        parcel.writeByte((byte) (Google ? 1 : 0));
        parcel.writeString(token);
        parcel.writeByte((byte) (profilePic ? 1 : 0));
    }
}

