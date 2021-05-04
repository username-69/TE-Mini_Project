package com.example.vaccinationtracker;

public class DoctorDB {
    String doctorName, doctorDegree, doctorHospital, doctorCity;
    long doctorID;

    public DoctorDB() {
    }

    public DoctorDB(String doctorName, String doctorDegree, String doctorHospital, String doctorCity, long doctorID) {
        this.doctorName = doctorName;
        this.doctorDegree = doctorDegree;
        this.doctorHospital = doctorHospital;
        this.doctorCity = doctorCity;
        this.doctorID = doctorID;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorDegree() {
        return doctorDegree;
    }

    public void setDoctorDegree(String doctorDegree) {
        this.doctorDegree = doctorDegree;
    }

    public String getDoctorHospital() {
        return doctorHospital;
    }

    public void setDoctorHospital(String doctorHospital) {
        this.doctorHospital = doctorHospital;
    }

    public String getDoctorCity() {
        return doctorCity;
    }

    public void setDoctorCity(String doctorCity) {
        this.doctorCity = doctorCity;
    }

    public long getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(long doctorID) {
        this.doctorID = doctorID;
    }
}
