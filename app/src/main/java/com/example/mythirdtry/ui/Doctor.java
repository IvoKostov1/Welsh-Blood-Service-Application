package com.example.mythirdtry.ui;

import android.os.Parcel;
import android.os.Parcelable;

/*Parcelable is a class which allows object to be converted to byes
* and transferred through activities*/
public class Doctor implements Parcelable {

    private String doctor_id, name;

    public Doctor() {
    }

    protected Doctor(Parcel in) {
        doctor_id = in.readString();
        name = in.readString();
    }

    //Implements the Parcelable interface
    public static final Creator<Doctor> CREATOR = new Creator<Doctor>() {
        @Override
        public Doctor createFromParcel(Parcel in) {
            return new Doctor(in);
        }

        @Override
        public Doctor[] newArray(int size) {
            return new Doctor[size];
        }
    };

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(doctor_id);
        dest.writeString(name);
    }
}
