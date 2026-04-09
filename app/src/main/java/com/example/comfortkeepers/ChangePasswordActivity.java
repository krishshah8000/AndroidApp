package com.example.comfortkeepers;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ChangePasswordActivity extends AppCompatActivity {

    private EditText edtOldPassword, edtNewPassword, edtConfirmPassword;
    private Button btnChangePassword;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    ImageView passwordToggle, confirmPasswordToggle,newPasswordToggle;
    private boolean isPasswordVisible = false;
    private boolean isPasswordVisible1 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            mAuth = FirebaseAuth.getInstance();
            currentUser = mAuth.getCurrentUser();

            // Initialize UI elements
            edtOldPassword = findViewById(R.id.edtOldPassword);
            edtNewPassword = findViewById(R.id.edtNewPassword);
            edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
            btnChangePassword = findViewById(R.id.btnChangePassword);
            passwordToggle = findViewById(R.id.passwordToggle);
            newPasswordToggle = findViewById(R.id.newPasswordToggle);
            confirmPasswordToggle = findViewById(R.id.confirmPasswordToggle);

            passwordToggle.setOnClickListener((View vi) -> {
                if (isPasswordVisible) {
                    // Hide password
                    edtOldPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passwordToggle.setImageResource(R.drawable.ic_eye); // Closed eye icon
                } else {
                    // Show password
                    edtOldPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passwordToggle.setImageResource(R.drawable.ic_eye); // Open eye icon
                }
                isPasswordVisible = !isPasswordVisible;
                edtOldPassword.setSelection(edtOldPassword.length()); // Move cursor to end
            });

            newPasswordToggle.setOnClickListener((View vi) -> {
                if (isPasswordVisible) {
                    // Hide password
                    edtNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    newPasswordToggle.setImageResource(R.drawable.ic_eye); // Closed eye icon
                } else {
                    // Show password
                    edtNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    newPasswordToggle.setImageResource(R.drawable.ic_eye); // Open eye icon
                }
                isPasswordVisible = !isPasswordVisible;
                edtNewPassword.setSelection(edtNewPassword.length()); // Move cursor to end
            });

            confirmPasswordToggle.setOnClickListener((View vi) -> {
                if (isPasswordVisible1) {
                    // Hide password
                    edtConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    confirmPasswordToggle.setImageResource(R.drawable.ic_eye); // Closed eye icon
                } else {
                    // Show password
                    edtConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    confirmPasswordToggle.setImageResource(R.drawable.ic_eye); // Open eye icon
                }
                isPasswordVisible1 = !isPasswordVisible1;
                edtConfirmPassword.setSelection(edtConfirmPassword.length()); // Move cursor to end
            });

            // Button Click Listener
            btnChangePassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changePassword();
                }
            });
            return insets;
        });
    }

    private void changePassword() {
        String oldPassword = edtOldPassword.getText().toString().trim();
        String newPassword = edtNewPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        // Validation Checks
        if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (newPassword.length() < 6) {
            Toast.makeText(this, "New password must be at least 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "New passwords do not match!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Re-authenticate User before Changing Password
        if (currentUser != null && currentUser.getEmail() != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), oldPassword);

            currentUser.reauthenticate(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Old password is correct, now update the password
                    currentUser.updatePassword(newPassword).addOnCompleteListener(updateTask -> {
                        if (updateTask.isSuccessful()) {
                            Toast.makeText(ChangePasswordActivity.this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ChangePasswordActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                            // Close Activity
                        } else {
                            Toast.makeText(ChangePasswordActivity.this, "Failed to update password! Try again later.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Old password is incorrect!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}