package com.example.comfortkeepers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PhysioActivity extends AppCompatActivity {

    LinearLayout llorthopedic, llNeuro, llcardiopulmonary, llwomenhealth, llsports;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_physio);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);


            findViewById(R.id.llorthopedic).setOnClickListener(view -> {
                Intent intent = new Intent(PhysioActivity.this, SpecialistActivity.class);
                intent.putExtra("subcategory", "Orthopedic Physiotherapy");
                startActivity(intent);
            });

            findViewById(R.id.llneuro).setOnClickListener(view -> {
                Intent intent = new Intent(PhysioActivity.this, SpecialistActivity.class);
                intent.putExtra("subcategory", "Neurological Physiotherapy");
                startActivity(intent);
            });

            findViewById(R.id.llcardiopulmonary).setOnClickListener(view -> {
                Intent intent = new Intent(PhysioActivity.this, SpecialistActivity.class);
                intent.putExtra("subcategory", "Cardiopulmonary Physiotherapy");
                startActivity(intent);
            });

            findViewById(R.id.llwomenhealth).setOnClickListener(view -> {
                Intent intent = new Intent(PhysioActivity.this, SpecialistActivity.class);
                intent.putExtra("subcategory", "Women’s Health Physiotherapy");
                startActivity(intent);
            });

            findViewById(R.id.llsports).setOnClickListener(view -> {
                Intent intent = new Intent(PhysioActivity.this, SpecialistActivity.class);
                intent.putExtra("subcategory", "Sports Physiotherapy");
                startActivity(intent);
            });

            return insets;
        });
    }

}