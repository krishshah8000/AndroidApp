package com.example.comfortkeepers;



import android.content.Intent;

import android.os.Bundle;

import android.view.View;

import android.widget.ArrayAdapter;

import android.widget.AutoCompleteTextView;

import android.widget.Button;

import android.widget.EditText;

import android.widget.RadioGroup;

import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.*;

import java.util.*;



public class PatientDetailsActivity extends AppCompatActivity {

    private Button confirmBookingButton;

    private DatabaseReference database;

    private EditText firstName, middleName, lastName, contactNumber, address, age, pincode1;

    private RadioGroup rgGender;

    private AutoCompleteTextView stateDropdown, cityDropdown;

    private String selectedState, selectedCity;

    private DatabaseReference databaseReference1;

    private String specialistName, selectedDate, selectedSlot, visitCharge, regularCharge, subcategory;



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_patient_details);



        firstName = findViewById(R.id.firstName);

        middleName = findViewById(R.id.middleName);

        lastName = findViewById(R.id.lastName);

        contactNumber = findViewById(R.id.contactNumber);

        address = findViewById(R.id.address);

        pincode1 = findViewById(R.id.pincode);

        rgGender = findViewById(R.id.genderGroup);

        stateDropdown = findViewById(R.id.stateDropdown);

        cityDropdown = findViewById(R.id.cityDropdown);

        age = findViewById(R.id.age);

        confirmBookingButton = findViewById(R.id.submitButton);



        databaseReference1 = FirebaseDatabase.getInstance().getReference("States");

        loadStates();



        stateDropdown.setOnItemClickListener((parent, v, position, id) -> {

            selectedState = (String) parent.getItemAtPosition(position);

            loadCities(selectedState);

        });

        cityDropdown.setOnItemClickListener((parent, v, position, id) -> selectedCity = (String) parent.getItemAtPosition(position));



        Intent intent = getIntent();

        specialistName = intent.getStringExtra("specialistName");

        selectedDate = intent.getStringExtra("selectedDate");

        selectedSlot = intent.getStringExtra("selectedSlot");

        visitCharge = intent.getStringExtra("visitCharge");

        regularCharge = intent.getStringExtra("regularCharge");

        subcategory = intent.getStringExtra("subcategory");  // Get subcategory


        if (specialistName == null || selectedDate == null || selectedSlot == null) {

            Toast.makeText(this, "Error: Missing booking details!", Toast.LENGTH_LONG).show();

            finish();

            return;

        }

        database = FirebaseDatabase.getInstance().getReference("bookings");

        confirmBookingButton.setOnClickListener(v -> saveBookingDetails());

    }



    private void saveBookingDetails() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {

            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();

            return;

        }



        String userEmail = user.getEmail();

        String fname = TextFormatter.capitalizeFirstLetter(firstName.getText().toString().trim());

        String lname = lastName.getText().toString().trim();

        String mname = middleName.getText().toString().trim();

        String age1 = age.getText().toString().trim();

        String contact = contactNumber.getText().toString().trim();

        String addressStr = address.getText().toString().trim();

        String pincode = pincode1.getText().toString().trim();



        int selectedGenderId = rgGender.getCheckedRadioButtonId();

        String gender = selectedGenderId == R.id.male ? "Male" : selectedGenderId == R.id.female ? "Female" : "Other";



        // **Validation Checks**
        if (fname.isEmpty()) { firstName.setError("First Name is required"); return; }
        if (mname.isEmpty()) { middleName.setError("Middle Name is required"); return; }
        if (lname.isEmpty()) { lastName.setError("Last Name is required"); return; }
        if (age1.isEmpty()) { age.setError("Age is required"); return; }
        if (!contact.matches("\\d{10}")) { contactNumber.setError("Enter a valid 10-digit contact number"); return; }
        if (selectedState == null) { stateDropdown.setError("Please select a state"); return; }
        if (selectedCity == null) { cityDropdown.setError("Please select a city"); return; }
        if (addressStr.isEmpty()) { address.setError("Address is required"); return; }
        if (!pincode.matches("\\d{6}")) { pincode1.setError("Enter a valid 6-digit pincode"); return; }
        if (rgGender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select a Gender", Toast.LENGTH_SHORT).show();
            return;
        }




        Map<String, Object> bookingData = new HashMap<>();

        bookingData.put("patientName", fname);

        bookingData.put("middleName", mname);

        bookingData.put("lastName", lname);

        bookingData.put("age", age1);

        bookingData.put("contact", contact);

        bookingData.put("address", addressStr);

        bookingData.put("pincode", pincode);

        bookingData.put("state", selectedState);

        bookingData.put("city", selectedCity);

        bookingData.put("gender", gender);

        bookingData.put("visitCharge", visitCharge);

        bookingData.put("regularCharge", regularCharge);
        bookingData.put("subcategory", subcategory);  // Store subcategory


        bookingData.put("status", "confirmed");

        bookingData.put("email", userEmail);



        database.child(specialistName).child(selectedDate).child(selectedSlot)

                .setValue(bookingData)

                .addOnSuccessListener(aVoid -> {

                    Toast.makeText(PatientDetailsActivity.this, "Booking Confirmed!", Toast.LENGTH_SHORT).show();

                    navigateToHome();

                })

                .addOnFailureListener(e -> Toast.makeText(PatientDetailsActivity.this, "Booking Failed!", Toast.LENGTH_SHORT).show());

    }



    private void navigateToHome() {

        Intent intent = new Intent(PatientDetailsActivity.this, HomeActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);

    }



    private void loadStates() {

        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<String> stateList = new ArrayList<>();

                for (DataSnapshot stateSnapshot : snapshot.getChildren()) {

                    stateList.add(stateSnapshot.getKey());

                }

                stateDropdown.setAdapter(new ArrayAdapter<>(PatientDetailsActivity.this, android.R.layout.simple_dropdown_item_1line, stateList));

            }



            @Override

            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(PatientDetailsActivity.this, "Failed to load states", Toast.LENGTH_SHORT).show();

            }

        });

    }



    private void loadCities(String state) {

        databaseReference1.child(state).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<String> cityList = new ArrayList<>();

                for (DataSnapshot citySnapshot : snapshot.getChildren()) {

                    cityList.add(citySnapshot.getValue(String.class));

                }

                cityDropdown.setAdapter(new ArrayAdapter<>(PatientDetailsActivity.this, android.R.layout.simple_dropdown_item_1line, cityList));

            }



            @Override

            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(PatientDetailsActivity.this, "Failed to load cities", Toast.LENGTH_SHORT).show();

            }

        });

    }

}