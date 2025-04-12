package com.example.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button goToCalculator = findViewById(R.id.buttonCalculator);
        Button goToAboutMe = findViewById(R.id.buttonAboutMe);

        goToCalculator.setOnClickListener(view -> {
            Intent intent = new Intent(MenuActivity.this, CalculatorActivity.class);
            startActivity(intent);
        });
        goToAboutMe.setOnClickListener(view -> {
            Intent intent = new Intent(MenuActivity.this, AboutMeActivity.class);
            startActivity(intent);
        });

    }
}