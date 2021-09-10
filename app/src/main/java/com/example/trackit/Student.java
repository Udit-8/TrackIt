package com.example.trackit;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Student implements Serializable {
    private String admissionNumber;
    private String busNumber;
    private String mobileNumber;
    private String password;
    private String studentName;
    public Student(){

    }

    public Student(String admissionNumber, String busNumber, String mobileNumber, String password, String studentName) {
        this.admissionNumber = admissionNumber;
        this.busNumber = busNumber;
        this.mobileNumber = mobileNumber;
        this.password = password;
        this.studentName = studentName;
    }

    public String getAdmissionNumber() {
        return admissionNumber;
    }

    public Student setAdmissionNumber(String admissionNumber) {
        this.admissionNumber = admissionNumber;
        return this;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public Student setBusNumber(String busNumber) {
        this.busNumber = busNumber;
        return this;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public Student setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Student setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getStudentName() {
        return studentName;
    }

    public Student setStudentName(String studentName) {
        this.studentName = studentName;
        return this;
    }
}
