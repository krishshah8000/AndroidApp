package com.example.comfortkeepers;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;

public class CityActivity extends AppCompatActivity {

    Spinner stateSpinner;
    EditText cityEditText;
    Button addCityButton;
    ListView cityListView;

    List<String> stateList = new ArrayList<>();
    List<String> cityList = new ArrayList<>();

    ArrayAdapter<String> stateAdapter;
    ArrayAdapter<String> cityAdapter;

    DatabaseReference statesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_city);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            stateSpinner = findViewById(R.id.stateSpinner);
            cityEditText = findViewById(R.id.cityEditText);
            addCityButton = findViewById(R.id.addCityButton);
            cityListView = findViewById(R.id.cityListView);

            statesRef = FirebaseDatabase.getInstance().getReference("States");

            loadStatesIntoSpinner();

            addCityButton.setOnClickListener(view -> {
                String selectedState = stateSpinner.getSelectedItem().toString();
                String newCity = cityEditText.getText().toString().trim();

                if (newCity.isEmpty()) {
                    Toast.makeText(this, "Enter a city", Toast.LENGTH_SHORT).show();
                } else {
                    addCityToFirebase(selectedState, newCity);
                }
            });

            stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedState = stateList.get(position);
                    loadCitiesForState(selectedState);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            return insets;
        });
    }

    private void loadStatesIntoSpinner() {
        statesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                stateList.clear();
                for (DataSnapshot stateSnap : snapshot.getChildren()) {
                    stateList.add(stateSnap.getKey());
                }

                stateAdapter = new ArrayAdapter<>(CityActivity.this, android.R.layout.simple_spinner_item, stateList);
                stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                stateSpinner.setAdapter(stateAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void loadCitiesForState(String stateName) {
        cityList.clear();

        statesRef.child(stateName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot citySnap : snapshot.getChildren()) {
                    String cityName = citySnap.getValue(String.class);
                    if (cityName != null) {
                        cityList.add(cityName);
                    }
                }

                cityAdapter = new ArrayAdapter<>(CityActivity.this, android.R.layout.simple_list_item_1, cityList);
                cityListView.setAdapter(cityAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void addCityToFirebase(String stateName, String cityName) {
        DatabaseReference cityRef = statesRef.child(stateName);

        cityRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long nextIndex = snapshot.getChildrenCount() + 1;
                cityRef.child(String.valueOf(nextIndex)).setValue(cityName)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(CityActivity.this, "City added!", Toast.LENGTH_SHORT).show();
                            cityEditText.setText("");
                            loadCitiesForState(stateName); // Refresh list
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}