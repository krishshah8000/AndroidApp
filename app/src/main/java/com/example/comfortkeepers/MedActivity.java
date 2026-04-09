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

public class MedActivity extends AppCompatActivity {

    LinearLayout llhomecare, llelderlycare, llmaternitycare, llpostsurgerycare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_med);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            findViewById(R.id.llhomecare).setOnClickListener(view -> {
                Intent intent = new Intent(MedActivity.this, SpecialistActivity.class);
                intent.putExtra("subcategory", "Home Care Attendant");
                startActivity(intent);
            });

            findViewById(R.id.llelderlycare).setOnClickListener(view -> {
                Intent intent = new Intent(MedActivity.this, SpecialistActivity.class);
                intent.putExtra("subcategory", "Elderly Care Attendant");
                startActivity(intent);
            });

            findViewById(R.id.llmaternitycare).setOnClickListener(view -> {
                Intent intent = new Intent(MedActivity.this, SpecialistActivity.class);
                intent.putExtra("subcategory", "Maternal Care Attendant");
                startActivity(intent);
            });

            findViewById(R.id.llpostsurgerycare).setOnClickListener(view -> {
                Intent intent = new Intent(MedActivity.this, SpecialistActivity.class);
                intent.putExtra("subcategory", "Post-Surgery Care Attendant");
                startActivity(intent);
            });


            return insets;
        });
    }
}