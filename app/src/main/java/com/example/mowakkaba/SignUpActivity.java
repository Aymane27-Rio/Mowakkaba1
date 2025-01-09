package com.example.mowakkaba;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        // Initialize UI elements
        EditText emailInput = findViewById(R.id.signup_email);
        EditText passwordInput = findViewById(R.id.signup_password);
        EditText confirmPasswordInput = findViewById(R.id.signup_confirm_password);
        Button signUpButton = findViewById(R.id.signup_button);

        // Handle sign-up button click
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();
                String confirmPassword = confirmPasswordInput.getText().toString();

                if (password.equals(confirmPassword) && !email.isEmpty()) {
                    // TODO: Add sign-up logic here (e.g., Firebase or API integration)
                    finish(); // Go back to login screen
                }
            }
        });
    }
}
