package com.example.comfortkeepers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OtpActivity extends AppCompatActivity {

    private EditText otpBox1, otpBox2, otpBox3, otpBox4;
    private Button submitButton;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otp);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            otpBox1 = findViewById(R.id.otpBox1);
            otpBox2 = findViewById(R.id.otpBox2);
            otpBox3 = findViewById(R.id.otpBox3);
            otpBox4 = findViewById(R.id.otpBox4);
            submitButton = findViewById(R.id.submitButton);
            sharedPreferences = getSharedPreferences("OTP_PREF", Context.MODE_PRIVATE);


            // Auto-focus logic
            submitButton.setOnClickListener(view -> {
                String enteredOtp = otpBox1.getText().toString() +
                        otpBox2.getText().toString() +
                        otpBox3.getText().toString() +
                        otpBox4.getText().toString();
                int storedOtp = sharedPreferences.getInt("otp", 0);

                if (enteredOtp.length() != 4) {
                    Toast.makeText(this, "Enter a valid 4-digit OTP", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (enteredOtp.equals(String.valueOf(storedOtp))) {
                    Toast.makeText(this, "OTP Verified!", Toast.LENGTH_SHORT).show();

                    // Move to Reset Password Screen
                    Intent intent = new Intent(OtpActivity.this, ResetPasswordActivity.class);
                    startActivity(intent);

                    // Clear OTP from SharedPreferences
                    sharedPreferences.edit().remove("otp").apply();
                } else {
                    Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                }
            });

                return insets;
        });


    }

    private void setupOtpInput() {
        EditText[] otpFields = {otpBox1, otpBox2, otpBox3, otpBox4};

        for (int i = 0; i < otpFields.length; i++) {
            final int index = i;
            otpFields[index].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1 && index < otpFields.length - 1) {
                        otpFields[index + 1].requestFocus(); // Move to next field
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            otpFields[index].setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == android.view.KeyEvent.KEYCODE_DEL && index > 0 && otpFields[index].getText().toString().isEmpty()) {
                    otpFields[index - 1].requestFocus(); // Move back on delete
                }
                return false;
            });
        }
    }

}