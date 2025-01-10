package com.example.mowakkaba;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        // Initialize UI elements
        EditText emailInput = findViewById(R.id.login_email);
        EditText passwordInput = findViewById(R.id.login_password);
        Button loginButton = findViewById(R.id.login_button);
        TextView signUpText = findViewById(R.id.sign_up_text); // Sign-Up link

        // Initialize Database Helper
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Handle Login Button Click
        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();

            // Validate Credentials
            Cursor userCursor = dbHelper.getUser(email, password);
            if (userCursor != null && userCursor.moveToFirst()) {
                // Login Successful
                String firstName = userCursor.getString(userCursor.getColumnIndex("first_name"));
                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                intent.putExtra("user_name", firstName);
                startActivity(intent);
                finish(); // Close the Login Activity
            } else {
                // Login Failed
                Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Sign-Up Text Click
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Sign-Up screen
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }
}