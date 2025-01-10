package com.example.mowakkaba;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        EditText firstNameInput = findViewById(R.id.signup_first_name);
        EditText lastNameInput = findViewById(R.id.signup_last_name);
        EditText emailInput = findViewById(R.id.signup_email);
        EditText passwordInput = findViewById(R.id.signup_password);
        EditText verifyPasswordInput = findViewById(R.id.signup_verify_password);
        Button signUpButton = findViewById(R.id.signup_button);

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        signUpButton.setOnClickListener(v -> {
            String firstName = firstNameInput.getText().toString().trim();
            String lastName = lastNameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString();
            String verifyPassword = verifyPasswordInput.getText().toString();

            if (!password.equals(verifyPassword)) {
                Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean success = dbHelper.insertUser(firstName, lastName, email, password);
            if (success) {
                Toast.makeText(SignUpActivity.this, "Sign-Up Successful", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(SignUpActivity.this, "Sign-Up Failed (Email might already exist)", Toast.LENGTH_SHORT).show();
            }
        });
    }
}