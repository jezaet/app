package com.example.greenrover.data;

import java.util.Date;

public class UploadData {

    private int numOfResidents;
    private float T1, T2;
    private String LSOA, Date;

    public UploadData() {
    }

    public UploadData(float t1, float t2 , int numOfResidents, String date, String LSOA) {
        this.numOfResidents = numOfResidents;
        this.T1 = t1;
        this.T2 = t2;
        this.LSOA = LSOA;
        this.Date = date;
    }

    public int getNumOfResidents() {
        return numOfResidents;
    }

    public void setNumOfResidents(int numOfResidents) {
        this.numOfResidents = numOfResidents;
    }

    public float getT1() {
        return T1;
    }

    public void setT1(float t1) {
        T1 = t1;
    }

    public float getT2() {
        return T2;
    }

    public void setT2(float t2) {
        T2 = t2;
    }

    public String getLSOA() {
        return LSOA;
    }

    public void setLSOA(String LSOA) {
        this.LSOA = LSOA;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        this.Date = date;
    }


}
