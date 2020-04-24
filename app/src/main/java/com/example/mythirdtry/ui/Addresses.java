package com.example.mythirdtry.ui;

import android.os.Parcel;
import android.os.Parcelable;

public class Addresses implements Parcelable {

    private String name;
    private String address;
    private String clinicId;
    private String website;
    private String phone;
    private String openHours;

    public Addresses()
    {

    }

    protected Addresses(Parcel in) {
        name = in.readString();
        address = in.readString();
        clinicId = in.readString();
        website = in.readString();
        phone = in.readString();
        openHours = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(clinicId);
        dest.writeString(website);
        dest.writeString(phone);
        dest.writeString(openHours);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Addresses> CREATOR = new Creator<Addresses>() {
        @Override
        public Addresses createFromParcel(Parcel in) {
            return new Addresses(in);
        }

        @Override
        public Addresses[] newArray(int size) {
            return new Addresses[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public String getClinicId() {
        return clinicId;
    }

    public void setClinicId(String clinicId) {
        this.clinicId = clinicId;
    }
}
