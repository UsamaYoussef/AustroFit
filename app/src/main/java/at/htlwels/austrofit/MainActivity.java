package at.htlwels.austrofit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView textViewFullName, textViewUsername, textViewCalories, textViewSteps, textViewPlan, textViewGoals;
    private EditText editTextWeight;
    private Button buttonSaveWeight;
    private DataBase db;
    private int userId;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        textViewFullName = findViewById(R.id.textViewFullName);
        textViewUsername = findViewById(R.id.textViewUsername);
        editTextWeight = findViewById(R.id.editTextWeight);
        buttonSaveWeight = findViewById(R.id.buttonSaveWeight);
        textViewCalories = findViewById(R.id.textViewCalories);
        textViewSteps = findViewById(R.id.textViewSteps);
        textViewPlan = findViewById(R.id.textViewPlan);
        textViewGoals = findViewById(R.id.textViewGoals);

        db = new DataBase(this);

        // Lade Benutzerdaten aus SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getInt("userId", -1);
        String firstName = sharedPreferences.getString("firstName", "First Name");
        String lastName = sharedPreferences.getString("lastName", "Last Name");
        String userName = sharedPreferences.getString("userName", "Username");
        String weight = sharedPreferences.getString("weight", "Not set");

        // Kombiniere Vor- und Nachnamen
        String fullName = firstName + " " + lastName;

        // Setze die TextViews mit den Benutzerdaten
        textViewFullName.setText(fullName);
        textViewUsername.setText(userName);
        textViewFullNameHeader.setText(fullName);
        textViewUsernameHeader.setText(userName);
        editTextWeight.setText(weight);

        buttonSaveWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newWeight = editTextWeight.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("weight", newWeight);
                editor.apply();
                Toast.makeText(MainActivity.this, "Gewicht gespeichert", Toast.LENGTH_SHORT).show();
            }
        });

        updateCalories();
        updateSteps();
        updatePlan();
        updateGoals();

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
                    Intent intent = new Intent(MainActivity.this, CaloriesTrackerActivity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                } else if (itemId == R.id.menuStepCounter) {
                    // Schrittzähler starten
                    Intent intent = new Intent(MainActivity.this, StepCounterActivity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                } else if (itemId == R.id.menuTrainingPlan) {
                    // Trainingsplan starten
                    Intent intent = new Intent(MainActivity.this, TrainingPlanActivity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                } else if (itemId == R.id.menuGoalSetting) {
                    // Zielesetzer starten
                    Intent intent = new Intent(MainActivity.this, GoalSettingActivity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
                // Weitere Menüaktionen hier
                return false;
            }
        });
    }

    private void updateCalories() {
        // Kalorienverbrauch für den aktuellen Tag aus der Datenbank abrufen und anzeigen
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        int calories = db.getTotalCalories(userId, currentDate);
        textViewCalories.setText("Zugenommene Kalorien heute: " + calories);
    }

    private void updateSteps() {
        // Schrittanzahl für den aktuellen Tag aus der Datenbank abrufen und anzeigen
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        int steps = db.getSteps(userId, currentDate);
        textViewSteps.setText("Zurückgelegte Schritte heute: " + steps);
    }

    private void updatePlan() {
        // Trainingsplan für den Benutzer abrufen und anzeigen
        String planDetails = db.getTrainingPlan(userId);
        textViewPlan.setText("Dein Trainingsplan: " + planDetails);
    }

    private void updateGoals() {
        // Ziele für den Benutzer abrufen und anzeigen
        String goals = db.getGoals(userId);
        textViewGoals.setText("Deine Ziele: " + goals);
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