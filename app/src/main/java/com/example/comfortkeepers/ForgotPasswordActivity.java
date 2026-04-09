package com.example.comfortkeepers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.Random;



public class ForgotPasswordActivity extends AppCompatActivity {

    private Button submitButton;

    private EditText etEmail;


    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            etEmail = findViewById(R.id.etEmail);
            sharedPreferences = getSharedPreferences("OTP_PREF", Context.MODE_PRIVATE);

            submitButton = findViewById(R.id.submitButton);

            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String email = etEmail.getText().toString().trim();
                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(ForgotPasswordActivity.this, "Enter your email", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Generate a random 4-digit OTP
                    int otp = new Random().nextInt(9000) + 1000;

                    // Save OTP in SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("otp", otp);
                    editor.putString("email", email);
                    editor.apply();

                    Toast.makeText(ForgotPasswordActivity.this, "OTP sent to email: " + otp, Toast.LENGTH_LONG).show();

                    // Move to Verify OTP screen
                    Intent intent = new Intent(ForgotPasswordActivity.this, OtpActivity.class);
                    startActivity(intent);


                }
            });

            return insets;
        });
    }
}