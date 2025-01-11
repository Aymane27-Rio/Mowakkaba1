package com.example.mowakkaba;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private String userEmail; // Passed from previous activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize UI Elements
        TextView nameTextView = findViewById(R.id.profile_name);
        TextView emailTextView = findViewById(R.id.profile_email);
        TextView userTypeTextView = findViewById(R.id.profile_user_type);
        LinearLayout detailsContainer = findViewById(R.id.profile_details_container);
        Button editProfileButton = findViewById(R.id.edit_profile_button);
        Button logoutButton = findViewById(R.id.logout_button);

        // Initialize Database Helper
        dbHelper = new DatabaseHelper(this);

        // Get user email from Intent
        userEmail = getIntent().getStringExtra("user_email");

        // Fetch user data from the database
        Cursor cursor = dbHelper.getUserByEmail(userEmail); // Use getUserByEmail
        if (cursor != null && cursor.moveToFirst()) {
            // Populate profile details
            @SuppressLint("Range") String fullName = cursor.getString(cursor.getColumnIndex("first_name")) + " " +
                    cursor.getString(cursor.getColumnIndex("last_name"));
            @SuppressLint("Range") String userType = cursor.getString(cursor.getColumnIndex("user_type"));
            @SuppressLint("Range") String additionalInfo = cursor.getString(cursor.getColumnIndex("info"));

            nameTextView.setText(fullName);
            emailTextView.setText(userEmail);
            userTypeTextView.setText(userType);

            // Add dynamic details based on user type
            detailsContainer.removeAllViews();
            if (userType.equals("Recent Graduate")) {
                addTextView(detailsContainer, "Objectives: " + additionalInfo);
            } else if (userType.equals("Professional in Transition")) {
                addTextView(detailsContainer, additionalInfo); // Format: "Last Job: ..., Reason: ..."
            } else if (userType.equals("Coach/Mentor")) {
                addTextView(detailsContainer, "Skills: " + additionalInfo.split(",")[0]);
                if (additionalInfo.contains("Certifications:")) {
                    addTextView(detailsContainer, additionalInfo.split(",")[1]);
                }
            }
        } else {
            Toast.makeText(this, "Error fetching user data", Toast.LENGTH_SHORT).show();
        }

        // Handle Edit Profile Button
        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            intent.putExtra("user_email", userEmail);
            startActivity(intent);
        });

        // Handle Logout Button
        logoutButton.setOnClickListener(v -> {
            // Navigate back to LoginActivity
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    /**
     * Dynamically adds a TextView to a container with the given text.
     */
    private void addTextView(LinearLayout container, String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(16);
        textView.setPadding(0, 8, 0, 8);
        container.addView(textView);
    }
}