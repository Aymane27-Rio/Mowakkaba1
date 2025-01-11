package com.example.mowakkaba;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MatchingActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);

        dbHelper = new DatabaseHelper(this);

        // For demonstration: Hardcoded client objectives
        String clientObjectives = "career growth, leadership";

        // Perform matching
        List<String> matchingCoaches = findMatchingCoaches(clientObjectives);

        // Display matching coaches
        TextView matchingResults = findViewById(R.id.matching_results);
        if (!matchingCoaches.isEmpty()) {
            StringBuilder results = new StringBuilder("Matching Coaches:\n");
            for (String coach : matchingCoaches) {
                results.append(coach).append("\n");
            }
            matchingResults.setText(results.toString());
        } else {
            matchingResults.setText("No matching coaches found.");
        }
    }

    private List<String> findMatchingCoaches(String clientObjectives) {
        List<String> matches = new ArrayList<>();
        Cursor coachesCursor = dbHelper.getAllCoaches();

        if (coachesCursor != null) {
            while (coachesCursor.moveToNext()) {
                String coachName = coachesCursor.getString(coachesCursor.getColumnIndex("first_name")) + " " +
                        coachesCursor.getString(coachesCursor.getColumnIndex("last_name"));
                String coachSkills = coachesCursor.getString(coachesCursor.getColumnIndex("skills"));

                // Simple matching logic: Check if any skill matches any objective
                for (String objective : clientObjectives.split(", ")) {
                    if (coachSkills != null && coachSkills.toLowerCase().contains(objective.toLowerCase())) {
                        matches.add(coachName);
                        break; // Stop checking further objectives for this coach
                    }
                }
            }
            coachesCursor.close();
        }
        return matches;
    }
}