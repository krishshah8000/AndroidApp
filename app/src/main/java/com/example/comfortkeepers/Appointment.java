package com.example.comfortkeepers;

public class Appointment {
    private String specialistName, date, time, visitCharge, regularCharge, subcategory;

    public Appointment() {
        // Default constructor required for Firebase
    }

    public Appointment(String specialistName, String date, String time, String visitCharge, String regularCharge, String subcategory) {
        this.specialistName = specialistName;
        this.date = date;
        this.time = time;
        this.visitCharge = visitCharge;
        this.regularCharge = regularCharge;
        this.subcategory = subcategory;
    }

    // Constructor that does not require subcategory
    public Appointment(String specialistName, String date, String time, String visitCharge, String regularCharge) {
        this.specialistName = specialistName;
        this.date = date;
        this.time = time;
        this.visitCharge = visitCharge;
        this.regularCharge = regularCharge;
        this.subcategory = "Not specified";  // Default value if subcategory is missing
    }

    // Getters and Setters
    public String getSpecialistName() {
        return specialistName;
    }

    public void setSpecialistName(String specialistName) {
        this.specialistName = specialistName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVisitCharge() {
        return visitCharge;
    }

    public void setVisitCharge(String visitCharge) {
        this.visitCharge = visitCharge;
    }

    public String getRegularCharge() {
        return regularCharge;
    }

    public void setRegularCharge(String regularCharge) {
        this.regularCharge = regularCharge;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }
}