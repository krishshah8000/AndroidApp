package com.example.comfortkeepers;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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

public class SettingActivity extends AppCompatActivity {

    LinearLayout llprofile, llcp;
    TextView profile, homeText;
    FirebaseAuth auth;
    DatabaseReference patientRef, specialistRef;
    boolean isPatient = false; // Track user role
    boolean isAdmin = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            auth = FirebaseAuth.getInstance();
            patientRef = FirebaseDatabase.getInstance().getReference("Patient");
            specialistRef = FirebaseDatabase.getInstance().getReference("Specialists");

            profile = findViewById(R.id.profile);
            llprofile = findViewById(R.id.llprofile);
            llcp = findViewById(R.id.llcp);
            homeText = findViewById(R.id.hometext);

            checkUserRole(); // Check user role before setting home redirection

            TextView logoutText = findViewById(R.id.logout_text);

            // Logout ClickableSpan
            SpannableString spannableString = new SpannableString("Logout");
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.signOut(); // Firebase Logout

                    Log.d("SettingActivity", "User logged out successfully. Redirecting to LoginActivity...");

                    new android.os.Handler().postDelayed(() -> {
                        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }, 1000);
                }
            };

            spannableString.setSpan(clickableSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            logoutText.setText(spannableString);
            logoutText.setMovementMethod(LinkMovementMethod.getInstance());

            // Profile Click
            llprofile.setOnClickListener((View vi) -> {
                Intent intent = new Intent(SettingActivity.this, ProfileActivity.class);
                startActivity(intent);
            });

            // Change Password Click
            llcp.setOnClickListener((View vi) -> {
                Intent intent = new Intent(SettingActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            });

            // Home ClickableSpan
            SpannableString homeSpan = new SpannableString("Home");
            ClickableSpan homeClickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    if (isPatient) {
                        startActivity(new Intent(SettingActivity.this, HomeActivity.class));
                    } else if (isAdmin) {
                        startActivity(new Intent(SettingActivity.this, AdminDashboardActivity.class));
                    } else {
                        startActivity(new Intent(SettingActivity.this, SpecialistDashboardActivity.class));
                    }

                }
            };

            homeSpan.setSpan(homeClickableSpan, 0, homeSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            homeText.setText(homeSpan);
            homeText.setMovementMethod(LinkMovementMethod.getInstance());

            return insets;
        });
    }

    private void checkUserRole() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            if (email != null) {
                // First check if the user is a patient
                patientRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            isPatient = true;
                        } else {
                            // Now check if the user is an admin
                            checkAdminRole(email);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SettingActivity.this, "Failed to load user role", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void checkAdminRole(String email) {
        DatabaseReference adminRef = FirebaseDatabase.getInstance().getReference("Admin");
        adminRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    isAdmin = true;
                }
                // If not admin and not patient, assume specialist
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SettingActivity.this, "Failed to load admin role", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkSpecialistRole(String email) {
        specialistRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                isPatient = !snapshot.exists(); // If exists, it's a specialist
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SettingActivity.this, "Failed to load user role", Toast.LENGTH_SHORT).show();
            }
        });
    }
}