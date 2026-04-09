package com.example.comfortkeepers;

import android.content.Intent;
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



public class SpecialistActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SpecialistAdapter adapter;
    private List<Specialist> specialistList;
    private DatabaseReference databaseReference;
    private String selectedSubcategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_specialist);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            specialistList = new ArrayList<>();
            adapter = new SpecialistAdapter(SpecialistActivity.this, specialistList); // FIXED
            recyclerView.setAdapter(adapter);

            // Get the clicked subcategory from intent
            Intent intent = getIntent();
            selectedSubcategory = intent.getStringExtra("subcategory");

            databaseReference = FirebaseDatabase.getInstance().getReference("Specialists");

            fetchSpecialists();



            return insets;
        });
    }

    private void fetchSpecialists() {
        databaseReference.orderByChild("subcategory").equalTo(selectedSubcategory)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        specialistList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Specialist specialist = dataSnapshot.getValue(Specialist.class);
                            if (specialist != null) {
                                specialistList.add(specialist);
                            }
                        }
                        adapter.notifyDataSetChanged();

                        if (specialistList.isEmpty()) {
                            Toast.makeText(SpecialistActivity.this, "No specialists found!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SpecialistActivity.this, "Failed to load specialists!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}