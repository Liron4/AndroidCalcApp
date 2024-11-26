package com.example.AndroidCalcApp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidcalcapp.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView input;
    private TextView calculationHistory;
    private List<String> calculationSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        input = findViewById(R.id.input);
        calculationHistory = findViewById(R.id.calculationHistory);
        calculationSteps = new ArrayList<>();

        input.setText("");
        calculationHistory.setText(""); // Optional: clear history on start
    }

    public void InsertNum(View view) {
        Button button = (Button) view;
        String num = button.getText().toString();
        input.append(num);
    }

    public void onOperatorClick(View view) {
        Button button = (Button) view;
        String operator = button.getText().toString();

        String currentInput = input.getText().toString();

        // Check if the input is invalid (starts with '0' and has more digits)
        if (currentInput.startsWith("0") && currentInput.length() > 1) {
            // Display error message
            Toast.makeText(this, "Numbers cannot begin with 0!", Toast.LENGTH_SHORT).show();
            input.setText(""); // Reset the input field
            return; // Do not proceed further
        }

        // Save the current number if valid
        if (!currentInput.isEmpty()) {
            calculationSteps.add(currentInput);
            calculationSteps.add(operator); // Save the operator
            calculationHistory.setText(String.join(" ", calculationSteps)); // Update history view
            input.setText(""); // Clear input for the next number
        }
    }
}