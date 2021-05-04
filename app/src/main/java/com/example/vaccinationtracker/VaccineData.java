package com.example.vaccinationtracker;

public class VaccineData {
    private String vaccineName;
    private int vaccineWeek, vaccineDose;
    private boolean isVaccincated;

    public VaccineData() {
    }

    public VaccineData(String vaccineName, int vaccineWeek, int vaccineDose, boolean isVaccincated) {
        this.vaccineName = vaccineName;
        this.vaccineWeek = vaccineWeek;
        this.vaccineDose = vaccineDose;
        this.isVaccincated = isVaccincated;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public int getVaccineWeek() {
        return vaccineWeek;
    }

    public void setVaccineWeek(int vaccineWeek) {
        this.vaccineWeek = vaccineWeek;
    }

    public int getVaccineDose() {
        return vaccineDose;
    }

    public void setVaccineDose(int vaccineDose) {
        this.vaccineDose = vaccineDose;
    }

    public boolean isVaccincated() {
        return isVaccincated;
    }

    public void setVaccincated(boolean vaccincated) {
        isVaccincated = vaccincated;
    }
}
