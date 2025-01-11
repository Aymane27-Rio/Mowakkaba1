package com.example.mowakkaba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        // Fetch user data (for demonstration, hardcoded user name)
        String userName = "Ayman Ayman"; // Replace with actual user data

        // Set the welcome message
        TextView welcomeText = findViewById(R.id.dashboard_welcome_text);
        welcomeText.setText("Welcome, " + userName + "!");

        // Initialize buttons
        Button profileButton = findViewById(R.id.dashboard_profile_button);
        Button appointmentsButton = findViewById(R.id.dashboard_appointments_button);
        Button resourcesButton = findViewById(R.id.dashboard_resources_button);

        // Objectives ListView
        ListView objectivesList = findViewById(R.id.dashboard_objectives_list);

        // Populate objectives (for demonstration)
        ArrayList<String> objectives = new ArrayList<>();
        objectives.add("Complete coding challenge");
        objectives.add("Attend mentor session");
        objectives.add("Update resume");

        ObjectivesAdapter adapter = new ObjectivesAdapter(this, objectives);
        objectivesList.setAdapter(adapter);

        // Set up button actions
        profileButton.setOnClickListener(v -> {
            // Navigate to profile activity
            startActivity(new Intent(DashboardActivity.this, ProfileActivity.class));
        });

        appointmentsButton.setOnClickListener(v -> {
            // Navigate to appointments activity
            startActivity(new Intent(DashboardActivity.this, AppointmentsActivity.class));
        });

        resourcesButton.setOnClickListener(v -> {
            // Navigate to resources activity
            startActivity(new Intent(DashboardActivity.this, ResourcesActivity.class));
        });
    }
}