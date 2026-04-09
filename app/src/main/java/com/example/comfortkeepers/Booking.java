package com.example.comfortkeepers;

public class Booking {
    private String patientName;
    private String specialistName;
    private String date;
    private String time;
    private String status;

    public Booking() {
        // Required empty constructor for Firebase
    }

    public Booking(String patientName, String specialistName,String date, String time, String status) {
        this.patientName = patientName;
        this.specialistName = specialistName;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
