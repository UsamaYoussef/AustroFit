package at.htlwels.austrofit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class TrainingPlanActivity extends AppCompatActivity {

    // UI-Elemente deklarieren
    private EditText editTextExercise, editTextReps, editTextSets;
    private Spinner spinnerDay;
    private Button buttonSave, buttonShowTodayPlan;

    // Datenbank-Helferklasse
    private DataBase db;
    private int userId;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_plan);

        final DrawerLayout drawerLayout = findViewById(R.id.layoutDrawer);

        findViewById(R.id.imageViewMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0); // Lade den Header
        TextView textViewFullNameHeader = headerView.findViewById(R.id.fullName);
        TextView textViewUsernameHeader = headerView.findViewById(R.id.userName);

        db = new DataBase(this);

        // Lade Benutzerdaten aus SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getInt("userId", -1);
        String firstName = sharedPreferences.getString("firstName", "First Name");
        String lastName = sharedPreferences.getString("lastName", "Last Name");
        String userName = sharedPreferences.getString("userName", "Username");

        // Kombiniere Vor- und Nachnamen
        String fullName = firstName + " " + lastName;

        // Setze die TextViews mit den Benutzerdaten
        textViewFullNameHeader.setText(fullName);
        textViewUsernameHeader.setText(userName);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.menuLogOut) {
                    // Benutzer abmelden
                    logOut();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                } else if (itemId == R.id.menuCaloriesTracker) {
                    // Kalorien-Tracker starten
                    Intent intent = new Intent(TrainingPlanActivity.this, CaloriesTrackerActivity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                } else if (itemId == R.id.menuStepCounter) {
                    // Schrittzähler starten
                    Intent intent = new Intent(TrainingPlanActivity.this, StepCounterActivity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                } else if (itemId == R.id.menuGoalSetting) {
                    // Trainingsplan starten
                    Intent intent = new Intent(TrainingPlanActivity.this, GoalSettingActivity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                } else if (itemId == R.id.menuProfile) {
                    // MainActivity starten
                    Intent intent = new Intent(TrainingPlanActivity.this, MainActivity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
                // Weitere Menüaktionen hier
                return false;
            }
        });

        // UI-Elemente mit ihren XML-IDs verbinden
        editTextExercise = findViewById(R.id.editTextExercise);
        editTextReps = findViewById(R.id.editTextReps);
        editTextSets = findViewById(R.id.editTextSets);
        spinnerDay = findViewById(R.id.spinnerDay);
        buttonSave = findViewById(R.id.buttonSave);
        buttonShowTodayPlan = findViewById(R.id.buttonShowTodayPlan);

        // Adapter für Spinner erstellen und Tage der Woche hinzufügen
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.days_of_week, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDay.setAdapter(arrayAdapter);

        // Initialisieren der Datenbank-Helferklasse
        db = new DataBase(this);

        // Benutzer-ID aus SharedPreferences abrufen
        SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);

        // Button-Klick-Listener für das Speichern von Übungen
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExercise();
            }
        });

        // Button-Klick-Listener für das Anzeigen des heutigen Trainingsplans
        buttonShowTodayPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTodayPlan();
            }
        });
    }

    // Methode zum Speichern einer Übung
    private void saveExercise() {
        String exerciseName = editTextExercise.getText().toString();
        String reps = editTextReps.getText().toString();
        String sets = editTextSets.getText().toString();
        String day = spinnerDay.getSelectedItem().toString();

        // Überprüfen, ob alle Felder ausgefüllt sind
        if (exerciseName.isEmpty() || reps.isEmpty() || sets.isEmpty()) {
            Toast.makeText(this, "Bitte füllen Sie alle Felder aus", Toast.LENGTH_SHORT).show();
        } else {
            // Neue Übung erstellen und zur Datenbank hinzufügen
            db.addExercise(userId, exerciseName, Integer.parseInt(reps), Integer.parseInt(sets), day);
            Toast.makeText(this, "Übung gespeichert", Toast.LENGTH_SHORT).show();

            // Felder leeren
            editTextExercise.setText("");
            editTextReps.setText("");
            editTextSets.setText("");
        }
    }

    // Methode zum Anzeigen des heutigen Trainingsplans
    private void showTodayPlan() {
        Intent intent = new Intent(TrainingPlanActivity.this, TodayPlanActivity.class);
        intent.putExtra("currentDay", spinnerDay.getSelectedItem().toString());
        startActivity(intent);
    }

    private void logOut() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("username");
        editor.remove("userId");
        editor.remove("fullName");
        editor.putBoolean("isLoggedIn", false);
        editor.apply();

        // LoginActivity starten nach Abmeldung
        Intent intent = new Intent(getApplicationContext(), LogIn.class);
        startActivity(intent);
        finish();
    }
}
