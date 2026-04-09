package com.example.comfortkeepers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
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

public class HomeActivity extends AppCompatActivity {

    private ViewFlipper viewFlipper;
    ImageView setting, Physio, Med, Diet;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    TextView user_name;
    LinearLayout llappoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            viewFlipper = findViewById(R.id.viewFlipper);
            setting = findViewById(R.id.setting);
            Physio = findViewById(R.id.Physio);
            Med = findViewById(R.id.Med);
            Diet = findViewById(R.id.imageView8);
            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference("Patient");
            user_name = findViewById(R.id.user_name);

            FirebaseUser user = mAuth.getCurrentUser();
            llappoint = findViewById(R.id.llappoint);

            llappoint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, AppointmentActivity.class);
                    startActivity(intent);
                }
            });

            if (user != null) {
                String email = user.getEmail();

                if (email != null) {
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean userFound = false;

                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                String storedEmail = userSnapshot.child("email").getValue(String.class);

                                if (storedEmail != null && storedEmail.equals(email)) {
                                    String firstName = userSnapshot.child("firstName").getValue(String.class);
                                    user_name.setText(firstName);
                                    userFound = true;
                                    break;
                                }
                            }

                            if (!userFound) {
                                user_name.setText("Welcome!");
                                Toast.makeText(HomeActivity.this, "User not found in database!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(HomeActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            setting.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, SettingActivity.class)));
            Physio.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, PhysioActivity.class)));
            Med.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, MedActivity.class)));
            Diet.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, DietActivity.class)));

            // Add images to the ViewFlipper
            int[] images = {R.drawable.i1, R.drawable.i2, R.drawable.i3, R.drawable.i4};

            for (int image : images) {
                ImageView imageView = new ImageView(this);
                imageView.setImageResource(image);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                viewFlipper.addView(imageView);
            }

            // Start flipping
            viewFlipper.setAutoStart(true);
            viewFlipper.setFlipInterval(3000);
            viewFlipper.startFlipping();

            return insets;
        });
    }

    @Override
    public void onBackPressed() {
        // ✅ Exit app properly when Back is pressed on HomeActivity
        super.onBackPressed();
        finishAffinity();
    }
}
