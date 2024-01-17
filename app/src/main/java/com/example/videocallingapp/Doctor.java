package com.example.videocallingapp;

public class Doctor {
    public String mDoctorId;
    public String mName;
    public String mAge;
    public String mContactno;
    public String mDepartment;
    public String mGender;
    public String mDoj;
    public String mAddress;
    public String mImageUrl;
    public Doctor(){
        //empty constructor needed
    }

    public Doctor(String mDoctorId, String mName, String mAge, String mContactno, String mDepartment, String mGender, String mDoj, String mAddress, String mImageUrl) {
        this.mDoctorId = mDoctorId;
        this.mName = mName;
        this.mAge = mAge;
        this.mContactno = mContactno;
        this.mDepartment = mDepartment;
        this.mGender = mGender;
        this.mDoj = mDoj;
        this.mAddress = mAddress;
        this.mImageUrl = mImageUrl;
    }

    public String getmDoctorId() {
        return mDoctorId;
    }

    public String getmName() {
        return mName;
    }

    public String getmAge() {
        return mAge;
    }

    public String getmContactno() {
        return mContactno;
    }

    public String getmDepartment() {
        return mDepartment;
    }

    public String getmGender() {
        return mGender;
    }

    public String getmDoj() {
        return mDoj;
    }

    public String getmAddress() {
        return mAddress;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }
}
