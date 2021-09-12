package com.example.trackit;

import java.io.Serializable;

public class Driver implements Serializable {
    private String driverBusNumber;
    private String driverMobileNumber;
    private String driverName;
    private String driverPassword;
    public Driver(){

    }

    public Driver(String busNumber, String driverMobileNumber, String driverName, String driverPassword) {
        this.driverBusNumber = busNumber;
        this.driverMobileNumber = driverMobileNumber;
        this.driverName = driverName;
        this.driverPassword = driverPassword;
    }

    public String getDriverBusNumber() {
        return driverBusNumber;
    }

    public Driver setDriverBusNumber(String busNumber) {
        this.driverBusNumber = busNumber;
        return this;
    }

    public String getDriverMobileNumber() {
        return driverMobileNumber;
    }

    public Driver setDriverMobileNumber(String driverMobileNumber) {
        this.driverMobileNumber = driverMobileNumber;
        return this;
    }

    public String getDriverName() {
        return driverName;
    }

    public Driver setDriverName(String driverName) {
        this.driverName = driverName;
        return this;
    }

    public String getDriverPassword() {
        return driverPassword;
    }

    public Driver setDriverPassword(String driverPassword) {
        this.driverPassword = driverPassword;
        return this;
    }
}
