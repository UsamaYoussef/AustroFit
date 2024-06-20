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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class GoalSettingActivity extends AppCompatActivity {

    // UI-Elemente deklarieren
    private EditText editTextGoal;
    private Button buttonAddGoal;
    private ListView listViewGoals;

    // Liste zur Speicherung der Ziele
    private ArrayList<String> goalList;
    private ArrayAdapter<String> goalAdapter;
    private DataBase db;
    private int userId;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_setting);

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
                    Intent intent = new Intent(GoalSettingActivity.this, CaloriesTrackerActivity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                } else if (itemId == R.id.menuStepCounter) {
                    // Schrittzähler starten
                    Intent intent = new Intent(GoalSettingActivity.this, StepCounterActivity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                } else if (itemId == R.id.menuTrainingPlan) {
                    // Trainingsplan starten
                    Intent intent = new Intent(GoalSettingActivity.this, TrainingPlanActivity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                } else if (itemId == R.id.menuProfile) {
                    // MainActivity starten
                    Intent intent = new Intent(GoalSettingActivity.this, MainActivity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
                // Weitere Menüaktionen hier
                return false;
            }
        });

        // UI-Elemente mit ihren XML-IDs verbinden
        editTextGoal = findViewById(R.id.editTextGoal);
        buttonAddGoal = findViewById(R.id.buttonAddGoal);
        listViewGoals = findViewById(R.id.listViewGoals);

        // Initialisieren der Zielliste
        goalList = new ArrayList<>();
        goalAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, goalList);
        listViewGoals.setAdapter(goalAdapter);

        // Button-Klick-Listener für das Hinzufügen von Zielen
        buttonAddGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGoal();
            }
        });

        // Lange Klick-Listener für das Entfernen von Zielen
        listViewGoals.setOnItemLongClickListener((parent, view, position, id) -> {
            removeGoal(position);
            return true;
        });
    }

    // Methode zum Hinzufügen eines Ziels
    private void addGoal() {
        String goal = editTextGoal.getText().toString();

        // Überprüfen, ob das Zielfeld nicht leer ist
        if (goal.isEmpty()) {
            Toast.makeText(this, "Bitte ein Ziel eingeben", Toast.LENGTH_SHORT).show();
        } else {
            // Ziel zur Liste hinzufügen und Adapter benachrichtigen
            goalList.add(goal);
            goalAdapter.notifyDataSetChanged();
            editTextGoal.setText(""); // Eingabefeld leeren
            Toast.makeText(this, "Ziel hinzugefügt", Toast.LENGTH_SHORT).show();
        }
    }

    // Methode zum Entfernen eines Ziels
    private void removeGoal(int position) {
        goalList.remove(position);
        goalAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Ziel entfernt", Toast.LENGTH_SHORT).show();
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