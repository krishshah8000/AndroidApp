package com.example.comfortkeepers;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference patientRef, specialistRef, adminRef;
    TextView txtfirstname, txtlastname, txtemail, txtmobile, txtadd, txtdob, txtcity, txtstate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            mAuth = FirebaseAuth.getInstance();
            patientRef = FirebaseDatabase.getInstance().getReference("Patient");
            specialistRef = FirebaseDatabase.getInstance().getReference("Specialists");
            adminRef = FirebaseDatabase.getInstance().getReference("Admin");

            txtfirstname = findViewById(R.id.txtfirstname);
            txtlastname = findViewById(R.id.txtlastname);
            txtemail = findViewById(R.id.txtemail);
            txtmobile = findViewById(R.id.txtmobile);
            txtadd = findViewById(R.id.txtadd);
            txtdob = findViewById(R.id.txtdob);
            txtcity = findViewById(R.id.txtcity);
            txtstate = findViewById(R.id.txtstate);

            FirebaseUser user = mAuth.getCurrentUser();

            if (user != null) {
                String email = user.getEmail();
                if (email != null) {
                    checkUserInPatients(email.toLowerCase());
                } else {
                    Toast.makeText(this, "Error: Email not found!", Toast.LENGTH_SHORT).show();
                }
            }

            return insets;
        });
    }

    private void checkUserInPatients(String email) {
        patientRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        displayUserData(userSnapshot);
                    }
                } else {
                    checkUserInSpecialists(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Failed to load patient data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserInSpecialists(String email) {
        specialistRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        displayUserData(userSnapshot);
                    }
                } else {
                    checkUserInAdmins(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Failed to load specialist data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserInAdmins(String email) {
        adminRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        displayUserData(userSnapshot);
                    }
                } else {
                    txtfirstname.setText("Welcome!");
                    Toast.makeText(ProfileActivity.this, "User not found in database!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Failed to load admin data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayUserData(DataSnapshot userSnapshot) {
        txtfirstname.setText(userSnapshot.child("firstName").getValue(String.class));
        txtlastname.setText(userSnapshot.child("lastName").getValue(String.class));
        txtemail.setText(userSnapshot.child("email").getValue(String.class));
        txtmobile.setText(userSnapshot.child("contactNumber").getValue(String.class));
        txtadd.setText(userSnapshot.child("address").getValue(String.class));
        txtdob.setText(userSnapshot.child("dob").getValue(String.class));
        txtcity.setText(userSnapshot.child("city").getValue(String.class));
        txtstate.setText(userSnapshot.child("state").getValue(String.class));
    }
}