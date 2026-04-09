package com.example.comfortkeepers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class AdminDashboardActivity extends AppCompatActivity {

    LinearLayout llstate,llcity,llspe,llpat ;
    private TextView textViewAdminName;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    ImageView setting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            llstate = findViewById(R.id.llstate);
            llcity = findViewById(R.id.llcity);
            llspe = findViewById(R.id.llspe);
            llpat = findViewById(R.id.llpat);
            setting=findViewById(R.id.setting);

            textViewAdminName = findViewById(R.id.user_name);
            mAuth = FirebaseAuth.getInstance();
            userRef = FirebaseDatabase.getInstance().getReference("Admin"); // or "Admins" if that's your node

            String currentEmail = mAuth.getCurrentUser().getEmail();

            if (currentEmail != null) {
                fetchAdminNameByEmail(currentEmail);
            }

            llstate.setOnClickListener((View vi) -> {
                Intent intent = new Intent(AdminDashboardActivity.this, StateActivity.class);
                startActivity(intent);
            });

            llcity.setOnClickListener((View vi) -> {
                Intent intent = new Intent(AdminDashboardActivity.this, CityActivity.class);
                startActivity(intent);
            });

            llspe.setOnClickListener((View vi) -> {
                Intent intent = new Intent(AdminDashboardActivity.this, AllSpecialistsActivity.class);
                startActivity(intent);
            });

            llpat.setOnClickListener((View vi) -> {
                Intent intent = new Intent(AdminDashboardActivity.this, ShowPatientsActivity.class);
                startActivity(intent);
            });

            setting.setOnClickListener(view -> startActivity(new Intent(AdminDashboardActivity.this, SettingActivity.class)));




            return insets;
        });
    }

    private void fetchAdminNameByEmail(String email) {
        userRef.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String firstName = dataSnapshot.child("firstName").getValue(String.class);
                            String lastName = dataSnapshot.child("lastName").getValue(String.class);

                            String fullName = (firstName != null ? firstName : "") + " " +
                                    (lastName != null ? lastName : "");

                            textViewAdminName.setText(fullName);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        textViewAdminName.setText("Welcome, Admin");
                    }
                });
    }
}