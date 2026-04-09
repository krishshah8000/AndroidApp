package com.example.comfortkeepers;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class AllSpecialistsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Specialist> specialistList;
    AllSpecialistsAdapter adapter;
    DatabaseReference databaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_specialists);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            recyclerView = findViewById(R.id.recyclerViewAllSpecialists);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            specialistList = new ArrayList<>();
            adapter = new AllSpecialistsAdapter(this, specialistList);
            recyclerView.setAdapter(adapter);

            databaseRef = FirebaseDatabase.getInstance().getReference("Specialists");
            loadSpecialists();

            return insets;
        });
    }

    private void loadSpecialists() {
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                specialistList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Specialist specialist = snap.getValue(Specialist.class);
                    if (specialist != null) {
                        specialistList.add(specialist);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AllSpecialists", "Database error: " + error.getMessage());
            }
        });
    }
}