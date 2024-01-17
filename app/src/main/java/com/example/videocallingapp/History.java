package com.example.videocallingapp;

public class History {
    public String mHistoryid;
    public String mPatientname;
    public String mTest;
    public String mDateoftest;
    public String mTestresult;
    public String mOthercases;

    public History() {
    }

    public History(String mHistoryid, String mPatientname, String mTest, String mDateoftest, String mTestresult, String mOthercases) {
        this.mHistoryid = mHistoryid;
        this.mPatientname = mPatientname;
        this.mTest = mTest;
        this.mDateoftest = mDateoftest;
        this.mTestresult = mTestresult;
        this.mOthercases = mOthercases;
    }

    public String getmHistoryid() {
        return mHistoryid;
    }

    public String getmPatientname() {
        return mPatientname;
    }

    public String getmTest() {
        return mTest;
    }

    public String getmDateoftest() {
        return mDateoftest;
    }

    public String getmTestresult() {
        return mTestresult;
    }

    public String getmOthercases() {
        return mOthercases;
    }
}
