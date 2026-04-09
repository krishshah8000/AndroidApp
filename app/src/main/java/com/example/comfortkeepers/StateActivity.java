package com.example.comfortkeepers;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.appcompat.app.AppCompatActivity;
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

public class StateActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText searchBar;
    List<String> stateList = new ArrayList<>();
    StateAdapter stateAdapter;

    DatabaseReference databaseRef;

    EditText addStateEditText;
    Button addStateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_state);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            recyclerView = findViewById(R.id.stateRecyclerView);
            searchBar = findViewById(R.id.searchBar);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            stateAdapter = new StateAdapter(stateList);
            recyclerView.setAdapter(stateAdapter);
            addStateEditText = findViewById(R.id.addStateEditText);
            addStateButton = findViewById(R.id.addStateButton);

            databaseRef = FirebaseDatabase.getInstance().getReference("States");

            fetchStatesFromFirebase();

            searchBar.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void afterTextChanged(Editable s) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    stateAdapter.filter(s.toString());
                }
            });

            addStateButton.setOnClickListener(view -> {
                String newState = addStateEditText.getText().toString().trim();
                if (!newState.isEmpty()) {
                    databaseRef.child(newState).setValue(""); // Or you can initialize with cities if needed
                    Toast.makeText(StateActivity.this, "State added!", Toast.LENGTH_SHORT).show();
                    addStateEditText.setText(""); // Clear input
                    fetchStatesFromFirebase(); // Refresh list
                } else {
                    Toast.makeText(StateActivity.this, "Enter a state name", Toast.LENGTH_SHORT).show();
                }
            });
            return insets;
        });
    }

    private void fetchStatesFromFirebase() {
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                stateList.clear();
                for (DataSnapshot stateSnap : snapshot.getChildren()) {
                    stateList.add(stateSnap.getKey()); // get the state name (e.g., "Goa")
                }
                stateAdapter = new StateAdapter(stateList);
                recyclerView.setAdapter(stateAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StateActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}