package com.example.comfortkeepers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SpecialistDashboardActivity extends AppCompatActivity {

    private ImageView setting;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private TextView user_name;

    private RecyclerView recyclerView;
    private BookingAdapter bookingAdapter;
    private List<Booking> bookingList;
    private String specialistName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_specialist_dashboard);

        // Handling edge-to-edge UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase and UI elements
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        user_name = findViewById(R.id.user_name);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookingList = new ArrayList<>();
        bookingAdapter = new BookingAdapter(this, bookingList, specialistName);
        recyclerView.setAdapter(bookingAdapter);

        setting = findViewById(R.id.setting);
        setting.setOnClickListener(v -> {
            Intent intent = new Intent(SpecialistDashboardActivity.this, SettingActivity.class);
            startActivity(intent);
        });

        // Fetch specialist name and appointments
        fetchSpecialistName();
    }

    private void fetchSpecialistName() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null || user.getEmail() == null) {
            Toast.makeText(this, "Error: No logged-in specialist!", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.child("Specialists").orderByChild("email").equalTo(user.getEmail())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            Toast.makeText(SpecialistDashboardActivity.this, "Specialist not found!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for (DataSnapshot data : snapshot.getChildren()) {
                            specialistName = data.child("firstName").getValue(String.class);

                            if (specialistName != null && !specialistName.isEmpty()) {
                                Log.d("SpecialistDebug", "Specialist Name: " + specialistName);
                                user_name.setText(specialistName); // Set the name in the TextView
                                bookingAdapter = new BookingAdapter(SpecialistDashboardActivity.this, bookingList, specialistName);
                                recyclerView.setAdapter(bookingAdapter);
                                fetchBookings();
                            } else {
                                Toast.makeText(SpecialistDashboardActivity.this, "Specialist name is missing!", Toast.LENGTH_SHORT).show();
                            }
                            break; // Stop loop after first match
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SpecialistDashboardActivity.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void fetchBookings() {
        if (specialistName == null || specialistName.isEmpty()) {
            Toast.makeText(this, "Error: No specialist name found!", Toast.LENGTH_SHORT).show();
            Log.e("BookingDebug", "Specialist name is NULL or EMPTY");
            return;
        }

        databaseReference.child("bookings").child(specialistName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        bookingList.clear();

                        // Check if there are no appointments
                        if (!snapshot.exists() || snapshot.getChildrenCount() == 0) {
                            Log.e("BookingDebug", "No Appointments for " + specialistName);
                            Toast.makeText(SpecialistDashboardActivity.this, "No Appointments Available", Toast.LENGTH_SHORT).show();
                            bookingAdapter.notifyDataSetChanged(); // 🔥 Ensure adapter refreshes
                            return;
                        }

                        Log.d("BookingDebug", "Appointments found for " + specialistName);

                        for (DataSnapshot dateSnapshot : snapshot.getChildren()) {
                            String date = dateSnapshot.getKey();
                            if (dateSnapshot.getChildrenCount() == 0) continue;

                            for (DataSnapshot timeSnapshot : dateSnapshot.getChildren()) {
                                String time = timeSnapshot.getKey();

                                // Skip boolean flags
                                if (timeSnapshot.getValue() instanceof Boolean) {
                                    Log.w("BookingDebug", "Skipping boolean flag at " + date + " " + time);
                                    continue;
                                }

                                Booking booking = timeSnapshot.getValue(Booking.class);
                                if (booking != null) {
                                    booking.setDate(date);
                                    booking.setTime(time);
                                    bookingList.add(booking);
                                    Log.d("BookingDebug", "Added Booking: " + booking.getDate() + " " + booking.getTime());
                                }
                            }
                        }

                        // 🔥 Even if no appointments, update the adapter to reflect changes
                        bookingAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SpecialistDashboardActivity.this, "Error loading bookings: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}