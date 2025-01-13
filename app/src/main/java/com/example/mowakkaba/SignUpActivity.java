package com.example.mowakkaba;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    private EditText firstNameInput, lastNameInput, emailInput, passwordInput, verifyPasswordInput;
    private Spinner userTypeSpinner;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        // Initialize UI elements
        firstNameInput = findViewById(R.id.signup_first_name);
        lastNameInput = findViewById(R.id.signup_last_name);
        emailInput = findViewById(R.id.signup_email);
        passwordInput = findViewById(R.id.signup_password);
        verifyPasswordInput = findViewById(R.id.signup_verify_password);
        userTypeSpinner = findViewById(R.id.user_type_spinner);
        Button signUpButton = findViewById(R.id.signup_button);

        // Initialize Database Helper
        dbHelper = new DatabaseHelper(this);

        // Handle Sign-Up Button Click
        signUpButton.setOnClickListener(v -> handleSignUp());
    }

    private void handleSignUp() {
        String firstName = firstNameInput.getText().toString().trim();
        String lastName = lastNameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();
        String verifyPassword = verifyPasswordInput.getText().toString();
        String userType = userTypeSpinner.getSelectedItem().toString();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || verifyPassword.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(verifyPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert the new user into the database
        boolean success = dbHelper.insertUser(firstName, lastName, email, password, userType, "");
        if (success) {
            Log.d("SignUpActivity", "User signed up with email: " + email);

            // Pass the email to ProfileActivity
            Intent intent = new Intent(SignUpActivity.this, ProfileActivity.class);
            intent.putExtra("user_email", email);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Sign-Up Failed. Email might already exist.", Toast.LENGTH_SHORT).show();
        }
    }
}
