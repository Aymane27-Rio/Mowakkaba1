package com.example.mowakkaba;



import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private String userEmail;
    private TextView nameTextView, emailTextView, userTypeTextView;
    private LinearLayout detailsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Get user email from Intent
        userEmail = getIntent().getStringExtra("user_email");
        Log.d("ProfileActivity", "Received user email: " + userEmail);

        if (userEmail == null || userEmail.isEmpty()) {
            Toast.makeText(this, "No user email provided. Redirecting to Login.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
            return;
        }

        // Initialize UI elements
        initializeUI();

        // Load user data
        loadUserData();
    }

    private void initializeUI() {
        nameTextView = findViewById(R.id.profile_name);
        emailTextView = findViewById(R.id.profile_email);
        userTypeTextView = findViewById(R.id.profile_user_type);
        detailsContainer = findViewById(R.id.profile_details_container);
        Button logoutButton = findViewById(R.id.logout_button);

        // Logout button action
        logoutButton.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void loadUserData() {
        dbHelper = new DatabaseHelper(this);
        Cursor cursor = dbHelper.getUserByEmail(userEmail);

        if (cursor != null && cursor.moveToFirst()) {
            String fullName = cursor.getString(cursor.getColumnIndex("first_name")) + " " +
                    cursor.getString(cursor.getColumnIndex("last_name"));
            String userType = cursor.getString(cursor.getColumnIndex("user_type"));
            String additionalInfo = cursor.getString(cursor.getColumnIndex("info"));

            nameTextView.setText(fullName);
            emailTextView.setText(userEmail);
            userTypeTextView.setText(userType);

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
            Toast.makeText(this, "No user data found.", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    private void addTextView(LinearLayout container, String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(16);
        textView.setPadding(0, 8, 0, 8);
        container.addView(textView);
    }
}
