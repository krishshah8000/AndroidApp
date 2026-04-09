package com.example.comfortkeepers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import androidx.annotation.NonNull;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference patientRef, specialistRef;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            auth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = auth.getCurrentUser();

            new Handler().postDelayed(() -> {
                if (currentUser != null) {
                    String email = currentUser.getEmail();
                    if (email != null) {
                        Log.d(TAG, "User logged in with email: " + email);
                        checkUserRole(email.toLowerCase()); // Convert to lowercase for consistency
                    } else {
                        Log.e(TAG, "User email is null. Redirecting to login.");
                        redirectToLogin();
                    }
                } else {
                    Log.d(TAG, "No user logged in. Redirecting to login.");
                    redirectToLogin();
                }
            },1000); // Delay execution for splash screen effect
            return insets;
        });
    }

    private void checkUserRole(String email) {
        patientRef = FirebaseDatabase.getInstance().getReference("Patient");
        specialistRef = FirebaseDatabase.getInstance().getReference("Specialists");

        Log.d(TAG, "Checking if user is a patient...");
        patientRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.d(TAG, "User found as Patient. Redirecting to HomeActivity.");
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    finish();
                } else {
                    Log.d(TAG, "User not found as Patient. Checking Specialist role...");
                    checkSpecialistRole(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Database error in Patient lookup: " + error.getMessage());
                redirectToLogin();
            }
        });
    }

    private void checkSpecialistRole(String email) {
        Log.d(TAG, "Checking if user is a Specialist...");
        specialistRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.d(TAG, "User found as Specialist. Redirecting to SpecialistDashboardActivity.");
                    startActivity(new Intent(MainActivity.this, SpecialistDashboardActivity.class));
                    finish();
                } else {
                    Log.d(TAG, "User role not found. Redirecting to login.");
                    redirectToLogin();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Database error in Specialist lookup: " + error.getMessage());
                redirectToLogin();
            }
        });
    }

    private void redirectToLogin() {
        Log.d(TAG, "Redirecting to LoginActivity.");
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
}