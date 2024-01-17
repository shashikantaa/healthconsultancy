package com.example.videocallingapp;

public class Patient {
    public String mPatientId;
    public String mName;
    public String mAge;
    public String mContactno;
    public String mDisease;
    public String mNoofvisit;
    public String mGender;
    public String mDot;
    public String mAddress;
    public String mMedicine;
    public String mImageUrl;
    public Patient(){
        //empty constructor needed
    }

    public Patient(String mPatientId, String mName, String mAge, String mContactno, String mDisease, String mNoofvisit, String mGender, String mDot, String mAddress, String mMedicine, String mImageUrl) {
        this.mPatientId = mPatientId;
        this.mName = mName;
        this.mAge = mAge;
        this.mContactno = mContactno;
        this.mDisease = mDisease;
        this.mNoofvisit = mNoofvisit;
        this.mGender = mGender;
        this.mDot = mDot;
        this.mAddress = mAddress;
        this.mMedicine = mMedicine;
        this.mImageUrl = mImageUrl;
    }

    public String getmPatientId() {
        return mPatientId;
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

    public String getmDisease() {
        return mDisease;
    }

    public String getmNoofvisit() {
        return mNoofvisit;
    }

    public String getmGender() {
        return mGender;
    }

    public String getmDot() {
        return mDot;
    }

    public String getmAddress() {
        return mAddress;
    }
    public String getmMedicine() {
        return mMedicine;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }
}
