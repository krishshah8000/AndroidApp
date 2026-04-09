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

public class DietActivity extends AppCompatActivity {

    LinearLayout llweightmanagement, llsports1, lleatingdisorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_diet);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            findViewById(R.id.llweightmanagement).setOnClickListener(view -> {
                Intent intent = new Intent(DietActivity.this, SpecialistActivity.class);
                intent.putExtra("subcategory", "Weight Management Dietitian");
                startActivity(intent);
            });

            findViewById(R.id.llsports1).setOnClickListener(view -> {
                Intent intent = new Intent(DietActivity.this, SpecialistActivity.class);
                intent.putExtra("subcategory", "Sports Dietitian");
                startActivity(intent);
            });

            findViewById(R.id.lleatingdisorder).setOnClickListener(view -> {
                Intent intent = new Intent(DietActivity.this, SpecialistActivity.class);
                intent.putExtra("subcategory", "Eating Disorder Dietitian");
                startActivity(intent);
            });
            return insets;
        });
    }
}