package com.example.comfortkeepers;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    ImageView ivTogglePassword;
    Button btnlogin;
    TextView tvForgotPassword, tvSignUpSpe, tvSignUpPat;
    private FirebaseAuth mAuth;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            etUsername = findViewById(R.id.etUsername);
            etPassword = findViewById(R.id.etPassword);
            ivTogglePassword = findViewById(R.id.imageView);
            btnlogin = findViewById(R.id.submitButton);
            tvSignUpPat = findViewById(R.id.tvSignUpPat);
            tvSignUpSpe = findViewById(R.id.tvSignUpSpe);
            tvForgotPassword = findViewById(R.id.tvForgotPassword);
            mAuth = FirebaseAuth.getInstance();

            btnlogin.setOnClickListener(vi -> validateInputs());

            tvForgotPassword.setOnClickListener(vi -> startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));
            tvSignUpPat.setOnClickListener(vi -> startActivity(new Intent(LoginActivity.this, SignUpActivityPat.class)));
            tvSignUpSpe.setOnClickListener(vi -> startActivity(new Intent(LoginActivity.this, SignUpActivitySpe.class)));


            ivTogglePassword.setOnClickListener((View vi) -> {
                if (isPasswordVisible) {
                    // Hide password
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivTogglePassword.setImageResource(R.drawable.ic_eye); // Closed eye icon
                } else {
                    // Show password
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivTogglePassword.setImageResource(R.drawable.ic_eye); // Open eye icon
                }
                isPasswordVisible = !isPasswordVisible;
                etPassword.setSelection(etPassword.length()); // Move cursor to end
            });


            return insets;
        });
    }

    private void validateInputs() {
        String email = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etUsername.setError("Enter a valid email");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Enter your password");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        checkUserRoleByEmail(email);
                    } else {
                        Toast.makeText(LoginActivity.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUserRoleByEmail(String email) {
        DatabaseReference patientRef = FirebaseDatabase.getInstance().getReference("Patient");
        DatabaseReference specialistRef = FirebaseDatabase.getInstance().getReference("Specialists");

        specialistRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.d("LoginActivity", "User is a Specialist. Redirecting to Specialist Dashboard.");
                    startActivity(new Intent(LoginActivity.this, SpecialistDashboardActivity.class));
                    finish();
                } else {
                    // If user is not a specialist, check if they are a patient
                    checkPatientRole(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Database error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkPatientRole(String email) {
        DatabaseReference patientRef = FirebaseDatabase.getInstance().getReference("Patient");

        patientRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                } else {
                    // If user is not a specialist, check if they are a patient
                    checkAdminRole(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Database error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkAdminRole(String email) {
        DatabaseReference adminRef = FirebaseDatabase.getInstance().getReference("Admin");

        adminRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class));
                    finish();
                } else {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(LoginActivity.this, "User role not found. Contact support.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Database error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}