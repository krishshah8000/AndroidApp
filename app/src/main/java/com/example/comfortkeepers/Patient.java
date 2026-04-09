package com.example.comfortkeepers;

public class Patient {
    public String firstName, lastName, gender, contactNumber, city, state,email;

    public Patient() {} // Needed for Firebase

    public Patient(String firstName, String lastName, String gender, String contactNumber, String city, String state , String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.city = city;
        this.state = state;
        this.email = email;
    }
}

