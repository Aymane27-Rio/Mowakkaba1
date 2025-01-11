package com.example.mowakkaba;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.database.sqlite.SQLiteDatabase;

import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private String userEmail; // Passed from previous activity

    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText emailInput;
    private LinearLayout detailsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize UI Elements
        firstNameInput = findViewById(R.id.edit_first_name);
        lastNameInput = findViewById(R.id.edit_last_name);
        emailInput = findViewById(R.id.edit_email);
        detailsContainer = findViewById(R.id.edit_details_container);
        Button saveButton = findViewById(R.id.save_profile_button);

        // Initialize Database Helper
        dbHelper = new DatabaseHelper(this);

        // Get user email from Intent
        userEmail = getIntent().getStringExtra("user_email");

        // Fetch user data and populate fields
        loadUserData();

        // Handle Save Button Click
        saveButton.setOnClickListener(v -> saveChanges());
    }

    /**
     * Loads user data from the database and populates the fields.
     */
    private void loadUserData() {
        Cursor cursor = dbHelper.getUserByEmail(userEmail);
        if (cursor != null && cursor.moveToFirst()) {
            // Populate user details
            String firstName = cursor.getString(cursor.getColumnIndex("first_name"));
            String lastName = cursor.getString(cursor.getColumnIndex("last_name"));
            String userType = cursor.getString(cursor.getColumnIndex("user_type"));
            String additionalInfo = cursor.getString(cursor.getColumnIndex("info"));

            // Set static fields
            firstNameInput.setText(firstName);
            lastNameInput.setText(lastName);
            emailInput.setText(userEmail);

            // Set dynamic fields
            detailsContainer.removeAllViews();
            if (userType.equals("Recent Graduate")) {
                addEditableField(detailsContainer, "Objectives", additionalInfo);
            } else if (userType.equals("Professional in Transition")) {
                String[] parts = additionalInfo.split(", Reason: ");
                if (parts.length == 2) {
                    addEditableField(detailsContainer, "Last Job Title", parts[0].replace("Last Job: ", "").trim());
                    addEditableField(detailsContainer, "Reason for Transition", parts[1].trim());
                }
            } else if (userType.equals("Coach/Mentor")) {
                String[] parts = additionalInfo.split(", Certifications: ");
                if (parts.length == 2) {
                    addEditableField(detailsContainer, "Skills", parts[0].replace("Skills: ", "").trim());
                    addEditableField(detailsContainer, "Certifications", parts[1].trim());
                }
            }
        } else {
            Toast.makeText(this, "Error loading user data", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Dynamically adds an editable field to the container.
     */
    private void addEditableField(LinearLayout container, String label, String value) {
        TextView fieldLabel = new TextView(this);
        fieldLabel.setText(label);
        fieldLabel.setTextSize(16);
        container.addView(fieldLabel);

        EditText fieldInput = new EditText(this);
        fieldInput.setText(value);
        fieldInput.setHint("Enter " + label);
        container.addView(fieldInput);
    }

    /**
     * Saves the changes made to the user's profile.
     */
    private void saveChanges() {
        String firstName = firstNameInput.getText().toString().trim();
        String lastName = lastNameInput.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            Toast.makeText(this, "First Name and Last Name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Collect dynamic fields
        String additionalInfo = "";
        for (int i = 0; i < detailsContainer.getChildCount(); i++) {
            View child = detailsContainer.getChildAt(i);
            if (child instanceof EditText) {
                additionalInfo += ((EditText) child).getText().toString().trim() + ", ";
            }
        }
        additionalInfo = additionalInfo.replaceAll(", $", ""); // Remove trailing comma

        // Update user in the database
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("first_name", firstName);
        values.put("last_name", lastName);
        values.put("info", additionalInfo);

        int rowsAffected = db.update("users", values, "email = ?", new String[]{userEmail});
        if (rowsAffected > 0) {
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            finish(); // Close activity
        } else {
            Toast.makeText(this, "Error updating profile", Toast.LENGTH_SHORT).show();
        }
    }
}