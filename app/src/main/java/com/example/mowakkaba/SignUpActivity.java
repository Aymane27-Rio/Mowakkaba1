package com.example.mowakkaba;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    private LinearLayout dynamicFormContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        // Initialize UI elements
        EditText firstNameInput = findViewById(R.id.signup_first_name);
        EditText lastNameInput = findViewById(R.id.signup_last_name);
        EditText emailInput = findViewById(R.id.signup_email);
        EditText passwordInput = findViewById(R.id.signup_password);
        EditText verifyPasswordInput = findViewById(R.id.signup_verify_password);
        Spinner userTypeSpinner = findViewById(R.id.user_type_spinner);
        dynamicFormContainer = findViewById(R.id.dynamic_form_container);
        Button signUpButton = findViewById(R.id.signup_button);

        // Populate the spinner with user types
        String[] userTypes = {"Recent Graduate", "Professional in Transition", "Coach/Mentor"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, userTypes);
        userTypeSpinner.setAdapter(adapter);

        // Listen for user type selection
        userTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dynamicFormContainer.removeAllViews(); // Clear previous form fields
                switch (position) {
                    case 0: // Recent Graduate
                        addRecentGraduateFields();
                        break;
                    case 1: // Professional in Transition
                        addProfessionalFields();
                        break;
                    case 2: // Coach/Mentor
                        addMentorFields();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                dynamicFormContainer.removeAllViews();
            }
        });

        // Handle Sign-Up Button Click
        signUpButton.setOnClickListener(v -> {
            String firstName = firstNameInput.getText().toString().trim();
            String lastName = lastNameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString();
            String verifyPassword = verifyPasswordInput.getText().toString();
            String userType = userTypeSpinner.getSelectedItem().toString();

            // Validate mandatory fields
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(verifyPassword)) {
                Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if email already exists
            DatabaseHelper dbHelper = new DatabaseHelper(SignUpActivity.this);
            if (dbHelper.doesEmailExist(email)) {
                Toast.makeText(SignUpActivity.this, "Email already exists. Please use a different email.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Collect additional information based on user type
            String additionalInfo = "";
            try {
                if (userType.equals("Recent Graduate")) {
                    EditText objectivesInput = (EditText) dynamicFormContainer.getChildAt(1);
                    additionalInfo = objectivesInput.getText().toString().trim();
                    if (additionalInfo.isEmpty()) {
                        Toast.makeText(SignUpActivity.this, "Please provide your objectives", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (userType.equals("Professional in Transition")) {
                    EditText lastJobTitleInput = (EditText) dynamicFormContainer.getChildAt(1);
                    EditText reasonForTransitionInput = (EditText) dynamicFormContainer.getChildAt(3);
                    String lastJob = lastJobTitleInput.getText().toString().trim();
                    String reason = reasonForTransitionInput.getText().toString().trim();
                    if (lastJob.isEmpty() || reason.isEmpty()) {
                        Toast.makeText(SignUpActivity.this, "Please fill in all fields for professionals", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    additionalInfo = "Last Job: " + lastJob + ", Reason: " + reason;
                } else if (userType.equals("Coach/Mentor")) {
                    EditText skillsInput = (EditText) dynamicFormContainer.getChildAt(1);
                    EditText certificationsInput = (EditText) dynamicFormContainer.getChildAt(3);
                    String skills = skillsInput.getText().toString().trim();
                    String certifications = certificationsInput.getText().toString().trim();
                    if (skills.isEmpty()) {
                        Toast.makeText(SignUpActivity.this, "Please provide your skills", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    additionalInfo = "Skills: " + skills + ", Certifications: " + certifications;
                }
            } catch (Exception e) {
                Toast.makeText(SignUpActivity.this, "Error in retrieving additional fields. Please try again.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Insert user data into the database
            boolean success = dbHelper.insertUser(firstName, lastName, email, password, userType, additionalInfo);
            if (success) {
                Toast.makeText(SignUpActivity.this, "Sign-Up Successful!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(SignUpActivity.this, "Sign-Up Failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addRecentGraduateFields() {
        TextView objectiveLabel = new TextView(this);
        objectiveLabel.setText("Your Objectives:");
        dynamicFormContainer.addView(objectiveLabel);

        EditText objectiveInput = new EditText(this);
        objectiveInput.setHint("Enter your objectives");
        dynamicFormContainer.addView(objectiveInput);
    }

    private void addProfessionalFields() {
        TextView lastJobLabel = new TextView(this);
        lastJobLabel.setText("Last Job Title:");
        dynamicFormContainer.addView(lastJobLabel);

        EditText lastJobInput = new EditText(this);
        lastJobInput.setHint("Enter your last job title");
        dynamicFormContainer.addView(lastJobInput);

        TextView transitionReasonLabel = new TextView(this);
        transitionReasonLabel.setText("Reason for Transition:");
        dynamicFormContainer.addView(transitionReasonLabel);

        EditText transitionReasonInput = new EditText(this);
        transitionReasonInput.setHint("Enter the reason for your transition");
        dynamicFormContainer.addView(transitionReasonInput);
    }

    private void addMentorFields() {
        TextView skillsLabel = new TextView(this);
        skillsLabel.setText("Your Skills:");
        dynamicFormContainer.addView(skillsLabel);

        EditText skillsInput = new EditText(this);
        skillsInput.setHint("Enter your skills (comma-separated)");
        dynamicFormContainer.addView(skillsInput);

        TextView certificationsLabel = new TextView(this);
        certificationsLabel.setText("Certifications:");
        dynamicFormContainer.addView(certificationsLabel);

        EditText certificationsInput = new EditText(this);
        certificationsInput.setHint("Enter your certifications");
        dynamicFormContainer.addView(certificationsInput);
    }
}