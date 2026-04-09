package com.example.comfortkeepers;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class AppointmentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AppointmentAdapter adapter;
    private List<Appointment> appointmentList;
    private DatabaseReference bookingsRef;
    private FirebaseAuth auth;
    private String currentUserEmail;
    private static final String TAG = "AppointmentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_appointment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            appointmentList = new ArrayList<>();
            adapter = new AppointmentAdapter(this, appointmentList);
            recyclerView.setAdapter(adapter);

            auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();

            if (user != null) {
                currentUserEmail = user.getEmail(); // Get the logged-in user's email
                fetchAppointments();
            } else {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            }

            return insets;
        });
    }

    private void fetchAppointments() {
        bookingsRef = FirebaseDatabase.getInstance().getReference("bookings");

        bookingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                appointmentList.clear();

                for (DataSnapshot specialistSnapshot : snapshot.getChildren()) {
                    String specialistName = specialistSnapshot.getKey(); // Get the specialist name

                    for (DataSnapshot dateSnapshot : specialistSnapshot.getChildren()) {
                        String appointmentDate = dateSnapshot.getKey();

                        for (DataSnapshot timeSnapshot : dateSnapshot.getChildren()) {
                            String appointmentTime = timeSnapshot.getKey();

                            if (timeSnapshot.hasChild("email")) {
                                String patientEmail = timeSnapshot.child("email").getValue(String.class);

                                if (patientEmail != null && patientEmail.equals(currentUserEmail)) {
                                    String visitCharge = timeSnapshot.child("visitCharge").getValue(String.class);
                                    String regularCharge = timeSnapshot.child("regularCharge").getValue(String.class);

                                    // Get the subcategory - check if it exists in the data
                                    String subcategory = "Not specified";
                                    if (timeSnapshot.hasChild("subcategory")) {
                                        subcategory = timeSnapshot.child("subcategory").getValue(String.class);
                                    }

                                    // Create Appointment object with the subcategory
                                    Appointment appointment = new Appointment(
                                            specialistName,
                                            appointmentDate,
                                            appointmentTime,
                                            visitCharge,
                                            regularCharge,
                                            subcategory);

                                    appointmentList.add(appointment);
                                    Log.d(TAG, "Added appointment with subcategory: " + subcategory);
                                }
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged();

                if (appointmentList.isEmpty()) {
                    Toast.makeText(AppointmentActivity.this, "No appointments found", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "Loaded " + appointmentList.size() + " appointments");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Database error: " + error.getMessage());
                Toast.makeText(AppointmentActivity.this, "Failed to load appointments", Toast.LENGTH_SHORT).show();
            }
        });
    }
}