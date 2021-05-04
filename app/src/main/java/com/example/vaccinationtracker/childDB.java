package com.example.vaccinationtracker;

import java.util.List;

public class childDB {
    private int childID;
    private String childName, placeOfBirth;
    private int childAge, childGender;
    private DOB childDOB;
    private List<VaccineData> childVaccines;

    public childDB() {
    }

    public childDB(int childID, String childName, String placeOfBirth, int childAge, int childGender, DOB childDOB) {
        this.childID = childID;
        this.childName = childName;
        this.placeOfBirth = placeOfBirth;
        this.childAge = childAge;
        this.childGender = childGender;
        this.childDOB = childDOB;
    }

    public childDB(int childID, String childName, String placeOfBirth, int childAge, int childGender, DOB childDOB, List<VaccineData> childVaccines) {
        this.childID = childID;
        this.childName = childName;
        this.placeOfBirth = placeOfBirth;
        this.childAge = childAge;
        this.childGender = childGender;
        this.childDOB = childDOB;
        this.childVaccines = childVaccines;
    }

    public int getChildID() {
        return childID;
    }

    public void setChildID(int childID) {
        this.childID = childID;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public int getChildAge() {
        return childAge;
    }

    public void setChildAge(int childAge) {
        this.childAge = childAge;
    }

    public int getChildGender() {
        return childGender;
    }

    public void setChildGender(int childGender) {
        this.childGender = childGender;
    }

    public DOB getChildDOB() {
        return childDOB;
    }

    public void setChildDOB(DOB childDOB) {
        this.childDOB = childDOB;
    }

    public List<VaccineData> getChildVaccines() {
        return childVaccines;
    }

    public void setChildVaccines(List<VaccineData> childVaccines) {
        this.childVaccines = childVaccines;
    }
}
