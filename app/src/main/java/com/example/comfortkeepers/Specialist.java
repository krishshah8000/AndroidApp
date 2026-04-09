package com.example.comfortkeepers;

public class Specialist {
    private String email;
    private String firstName, lastName, regular_charge, visit_charge, subcategory;
    private String id, category;  // Firebase Unique ID


    public Specialist() {

        // Required empty constructor for Firebase
    }





    public Specialist(String id,String firstName, String email, String lastName, String regular_charge, String visit_charge, String subcategory, String category) {
        this.firstName = firstName;
        this.email = email;
        this.id = id;
        this.lastName = lastName;
        this.regular_charge = regular_charge;
        this.visit_charge = visit_charge;
        this.subcategory = subcategory;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getRegular_charge() {
        return regular_charge;
    }

    public String getVisit_charge() {
        return visit_charge;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public String getEmail(){
        return email;
    }

    public String getCategory(){

        return category;
    }
}


