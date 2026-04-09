package com.example.comfortkeepers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.regex.Pattern;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText password1, confirmpassword;
    ImageView passwordToggle, confirmPasswordToggle;
    private boolean isPasswordVisible = false;
    private boolean isPasswordVisible1 = false;
    private Button btnResetPassword;
    private SharedPreferences sharedPreferences;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]{8,}$");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            passwordToggle = findViewById(R.id.passwordToggle);
            password1 = findViewById(R.id.password);
            confirmpassword = findViewById(R.id.confirmPassword);
            confirmPasswordToggle = findViewById(R.id.confirmPasswordToggle);
            btnResetPassword = findViewById(R.id.btnResetPassword);
            sharedPreferences = getSharedPreferences("OTP_PREF", Context.MODE_PRIVATE);

            passwordToggle.setOnClickListener((View vi) -> {
                if (isPasswordVisible) {
                    // Hide password
                    password1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passwordToggle.setImageResource(R.drawable.ic_eye); // Closed eye icon
                } else {
                    // Show password
                    password1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passwordToggle.setImageResource(R.drawable.ic_eye); // Open eye icon
                }
                isPasswordVisible = !isPasswordVisible;
                password1.setSelection(password1.length()); // Move cursor to end
            });

            confirmPasswordToggle.setOnClickListener((View vi) -> {
                if (isPasswordVisible1) {
                    // Hide password
                    confirmpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    confirmPasswordToggle.setImageResource(R.drawable.ic_eye); // Closed eye icon
                } else {
                    // Show password
                    confirmpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    confirmPasswordToggle.setImageResource(R.drawable.ic_eye); // Open eye icon
                }
                isPasswordVisible1 = !isPasswordVisible1;
                confirmpassword.setSelection(confirmpassword.length()); // Move cursor to end
            });

            btnResetPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validateAndResetPassword();
                }
            });

            return insets;
        });
    }

    private void validateAndResetPassword() {
        String password = password1.getText().toString().trim();
        String confirmPassword = confirmpassword.getText().toString().trim();

        // Check if password is empty
        if (TextUtils.isEmpty(password)) {
            password1.setError("Password cannot be empty");
            return;
        }

        // Check if password meets criteria
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            password1.setError("Password must be at least 8 characters, include a digit and a special character.");
            return;
        }

        // Check if confirm password matches
        if (!password.equals(confirmPassword)) {
            confirmpassword.setError("Passwords do not match!");
            return;
        }

        // If validation passes, show success and navigate to Login Page
        Toast.makeText(this, "Password Reset Successful!", Toast.LENGTH_SHORT).show();

        // Redirect to Login Page
        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}