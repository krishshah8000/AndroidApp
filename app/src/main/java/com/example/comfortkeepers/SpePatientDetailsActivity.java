package com.example.comfortkeepers;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SpePatientDetailsActivity extends AppCompatActivity {

    private TextView txtName, txtAge, txtGender, txtContact, txtAddress, txtCity, txtState, txtPincode;
    private DatabaseReference databaseReference;
    private String specialistName, date, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spe_patient_details);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;  // ✅ Ensure we return insets
        });

        // Initialize TextViews
        txtName = findViewById(R.id.txtName);
        txtAge = findViewById(R.id.txtAge);
        txtGender = findViewById(R.id.txtGender);
        txtContact = findViewById(R.id.txtContact);
        txtAddress = findViewById(R.id.txtAddress);
        txtCity = findViewById(R.id.txtCity);
        txtState = findViewById(R.id.txtState);
        txtPincode = findViewById(R.id.txtPincode);

        // Get data from intent
        specialistName = getIntent().getStringExtra("specialistName");
        date = getIntent().getStringExtra("date");
        time = getIntent().getStringExtra("time");

        if (specialistName == null || date == null || time == null) {
            Toast.makeText(this, "Error: Missing appointment details!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("bookings")
                .child(specialistName).child(date).child(time);

        // Fetch and display patient details
        fetchPatientDetails();
    }

    private void fetchPatientDetails() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String firstName = snapshot.child("patientName").getValue(String.class);
                    String lastName = snapshot.child("lastName").getValue(String.class);
                    String age = snapshot.child("age").getValue(String.class);
                    String gender = snapshot.child("gender").getValue(String.class);
                    String contact = snapshot.child("contact").getValue(String.class);
                    String address = snapshot.child("address").getValue(String.class);
                    String city = snapshot.child("city").getValue(String.class);
                    String state = snapshot.child("state").getValue(String.class);
                    String pincode = snapshot.child("pincode").getValue(String.class);

                    // Concatenate first name and last name
                    String fullName = (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");

                    // Set values in TextViews
                    txtName.setText(fullName.trim());
                    txtAge.setText(age != null ? age : "N/A");
                    txtGender.setText(gender != null ? gender : "N/A");
                    txtContact.setText(contact != null ? contact : "N/A");
                    txtAddress.setText(address != null ? address : "N/A");
                    txtCity.setText(city != null ? city : "N/A");
                    txtState.setText(state != null ? state : "N/A");
                    txtPincode.setText(pincode != null ? pincode : "N/A");

                } else {
                    Toast.makeText(SpePatientDetailsActivity.this, "No patient details found!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SpePatientDetailsActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
