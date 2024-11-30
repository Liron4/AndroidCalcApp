package com.example.AndroidCalcApp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Vibrator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidcalcapp.R;

public class MainActivity extends AppCompatActivity {
    private TextView input;
    private Vibrator vibrator;
    private TextView calculationHistory;

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
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        input.setText("");
        calculationHistory.setText("");

        Button eraseButton = findViewById(R.id.buttonerase);
        eraseButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onEraseLongClick(v);
                return true;
            }
        });
    }

    public void InsertNum(View view) {
        animateButton(view, R.anim.button_click);
        vibrate();
        Button button = (Button) view;
        String num = button.getText().toString();
        input.append(num);
    }


    public void onOperatorClick(View view) {
        animateButton(view, R.anim.button_click);
        vibrate();
        Button button = (Button) view;
        String operator = button.getText().toString();

        String currentInput = input.getText().toString();

        // Case 1: If there's already an operator, don't allow more calculations
        if (currentInput.isEmpty()) return; // Prevent empty input

        // Case 2: Prevent using multiple operators before hitting equals
        if (currentInput.contains("+") || currentInput.contains("-") || currentInput.contains("×") || currentInput.contains("÷")) {
            return; // Ignore if operator already exists in input
        }

        // Add the operator to the input string
        input.setText(currentInput + " " + operator + " ");
    }

    public void onEqualClick(View view) {
        animateButton(view, R.anim.button_click);
        vibrate();
        String currentInput = input.getText().toString();
        calculationHistory.setText("");

        if (currentInput.isEmpty()) return;

        try {
            // Use a simple expression evaluation
            double result = evaluateExpression(currentInput);

            // Check if the result is an integer
            if (result == (int) result) {
                input.setText(String.valueOf((int) result));
            } else {
                input.setText(String.format("%.3f", result));
            }

            String historyEntry = currentInput + " = " + input.getText().toString();
            calculationHistory.append(historyEntry);
        } catch (Exception e) {
            // Show error if evaluation fails
            Toast.makeText(this, "Invalid Calculation!", Toast.LENGTH_SHORT).show();
        }
    }

    private double evaluateExpression(String expression) {
        // Check if the expression is empty or invalid
        if (expression == null || expression.trim().isEmpty()) {
            throw new IllegalArgumentException("Input is empty. Please enter a valid expression.");
        }

        // Replace '×' and '÷' with '*' and '/' for evaluation
        expression = expression.replace("×", "*").replace("÷", "/");

        // Regular expression to split the expression into numbers and operators
        String[] tokens = expression.split("(?<=[-+*/])|(?=[-+*/])");

        if (tokens.length < 3) {
            throw new IllegalArgumentException("Incomplete expression. Please provide both operands and operator.");
        }

        double result = Double.parseDouble(tokens[0]);

        for (int i = 1; i < tokens.length; i += 2) {
            String operator = tokens[i];
            double value = Double.parseDouble(tokens[i + 1]);

            // Check for division by zero
            if (operator.equals("/") && value == 0) {
                throw new ArithmeticException("Impossible to divide by 0.");
            }

            // Perform the operation
            switch (operator) {
                case "+":
                    result += value;
                    break;
                case "-":
                    result -= value;
                    break;
                case "*":
                    result *= value;
                    break;
                case "/":
                    result /= value;
                    break;
            }
        }

        // Ensure the result is an integer if no decimals are involved
        if (result == (int) result) {
            return (int) result; // Return as an integer if the result is whole
        }

        return result;
    }

    public void onEraseClick(View view) {
        animateButton(view, R.anim.button_click);
        vibrate();
        String currentInput = input.getText().toString();

        if (!currentInput.isEmpty()) {
            // Delete the last character from the input
            input.setText(currentInput.substring(0, currentInput.length() - 1));
        } else {
            Toast.makeText(this, "Nothing to erase!", Toast.LENGTH_SHORT).show();
        }
    }


    // Handle long-press to erase everything
    public void onEraseLongClick(View view) {
        animateButton(view, R.anim.button_long_click);
        vibrate();// Reset all fields and lists
        input.setText("");
        calculationHistory.setText("");
        Toast.makeText(this, "Calculation reset!", Toast.LENGTH_SHORT).show();
    }

    private void animateButton(View view, int animationResource) {
        Animation animation = AnimationUtils.loadAnimation(this, animationResource);
        view.startAnimation(animation);
    }

    private void vibrate() {
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(50); // Vibrate for 50 milliseconds
        }
    }



} // main activity class ends here