package at.htlwels.austrofit;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TodayPlanActivity extends AppCompatActivity {

    // UI-Elemente deklarieren
    private ListView listViewTodayPlan;
    private DataBase db;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_plan);

        // UI-Elemente mit ihren XML-IDs verbinden
        listViewTodayPlan = findViewById(R.id.listViewTodayPlan);

        // Initialisieren der Datenbank-Helferklasse
        db = new DataBase(this);

        // Benutzer-ID aus SharedPreferences abrufen
        SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);

        // Aktuellen Tag aus Intent erhalten
        String currentDay = getIntent().getStringExtra("currentDay");

        // Übungen für den aktuellen Tag aus der Datenbank abrufen
        ArrayList<Exercise> todayExercises = db.getExercises(userId, currentDay);

        // Adapter erstellen und für ListView setzen
        ExerciseAdapter exerciseAdapter = new ExerciseAdapter(this, todayExercises);
        listViewTodayPlan.setAdapter(exerciseAdapter);
    }
}
