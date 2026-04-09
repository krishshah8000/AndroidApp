package com.example.comfortkeepers;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowPatientsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PatientAdapter adapter;
    private List<Patient> patientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_patients);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            patientList = new ArrayList<>();

            adapter = new PatientAdapter(patientList);
            recyclerView.setAdapter(adapter);

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Patient");

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    patientList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String role = dataSnapshot.child("role").getValue(String.class);
                        if ("Patient".equals(role)) {
                            String firstName = dataSnapshot.child("firstName").getValue(String.class);
                            String lastName = dataSnapshot.child("lastName").getValue(String.class);
                            String gender = dataSnapshot.child("gender").getValue(String.class);
                            String contact = dataSnapshot.child("contactNumber").getValue(String.class);
                            String city = dataSnapshot.child("city").getValue(String.class);
                            String state = dataSnapshot.child("state").getValue(String.class);
                            String email = dataSnapshot.child("email").getValue(String.class);

                            patientList.add(new Patient(firstName, lastName, gender, contact, city, state , email));
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ShowPatientsActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            });

            return insets;
        });
    }
}