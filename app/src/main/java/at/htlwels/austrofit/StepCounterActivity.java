package at.htlwels.austrofit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StepCounterActivity extends AppCompatActivity implements SensorEventListener {

    // Deklaration der UI-Elemente
    private TextView stepCountTextView, distanceTextView, timeTextView, stepCountTargetTextView;
    private ProgressBar progressBar;

    // SensorManager und StepCounter-Sensor deklarieren
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;

    // Variablen zur Speicherung der Schrittanzahl und anderer Daten
    private int stepCount = 0;
    private boolean isPaused = false;
    private long timePaused = 0;
    private float stepLengthInMeter = 0.762f; // Standard-Schrittlänge
    private long startTime;
    private int stepCountTarget = 6000; // Ziel für die Schrittanzahl
    private int initialStepCount = -1; // Initiale Schrittanzahl
    private Handler timerHandler = new Handler();
    private SharedPreferences sharedPreferences;
    private DataBase db;
    private int userId;

    // Konstanten für SharedPreferences
    private static final String PREFS_NAME = "StepCounterPrefs";
    private static final String STEP_COUNT_KEY = "stepCountKey";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);

        // UI-Elemente mit ihren XML-IDs verbinden
        stepCountTextView = findViewById(R.id.textViewCount);
        distanceTextView = findViewById(R.id.textViewDistance);
        timeTextView = findViewById(R.id.textViewTime);
        stepCountTargetTextView = findViewById(R.id.textViewTarget);
        progressBar = findViewById(R.id.progressBar);

        // Startzeit festlegen
        startTime = System.currentTimeMillis();

        // SharedPreferences initialisieren
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Datenbank-Helferklasse initialisieren
        db = new DataBase(this);

        // Benutzer-ID aus SharedPreferences abrufen
        SharedPreferences userPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        userId = userPrefs.getInt("userId", -1);

        // SensorManager initialisieren und den StepCounter-Sensor abrufen
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        // Fortschrittsbalken für das Schrittziel festlegen
        progressBar.setMax(stepCountTarget);
        stepCountTargetTextView.setText("Schritt Ziel: " + stepCountTarget);

        // Überprüfen, ob der StepCounter-Sensor verfügbar ist
        if (stepCounterSensor == null) {
            stepCountTextView.setText("Schrittzähler nicht verfügbar");
        }
    }

    // Runnable für den Timer, um die verstrichene Zeit anzuzeigen
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int min = seconds / 60;
            seconds = seconds % 60;
            timeTextView.setText(String.format(Locale.getDefault(), "Zeit: %02d:%02d", min, seconds));
            timerHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (stepCounterSensor != null) {
            // Registriere den Sensor-Listener für den StepCounter-Sensor
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);

            // Starte den Timer-Handler
            timerHandler.postDelayed(timerRunnable, 0);

            // Heutiges Datum abrufen
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            // Lade die gespeicherte Schrittanzahl aus der Datenbank
            stepCount = db.getSteps(userId, currentDate);
            stepCountTextView.setText("Step Count: " + stepCount);
            progressBar.setProgress(stepCount);

            // Berechne die zurückgelegte Distanz und aktualisiere die Anzeige
            float distanceInKm = stepCount * stepLengthInMeter / 1000;
            distanceTextView.setText(String.format(Locale.getDefault(), "Distanz: %.2f km", distanceInKm));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (stepCounterSensor != null) {
            // Unregistriere den Sensor-Listener und stoppe den Timer-Handler
            sensorManager.unregisterListener(this);
            timerHandler.removeCallbacks(timerRunnable);

            // Heutiges Datum abrufen
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            // Speichere die aktuelle Schrittanzahl in der Datenbank
            db.updateSteps(userId, currentDate, stepCount);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            int currentSteps = (int) sensorEvent.values[0];

            // Initiale Schrittanzahl festlegen, wenn noch nicht gesetzt
            if (initialStepCount < 0) {
                initialStepCount = currentSteps;
            }

            // Berechne die tatsächliche Schrittanzahl
            stepCount = currentSteps - initialStepCount + sharedPreferences.getInt(STEP_COUNT_KEY, 0);
            stepCountTextView.setText("Schritte: " + stepCount);
            progressBar.setProgress(stepCount);

            // Überprüfen, ob das Schrittziel erreicht wurde
            if (stepCount >= stepCountTarget) {
                stepCountTargetTextView.setText("Schrittziel erreicht!");
            }

            // Berechne die zurückgelegte Distanz und aktualisiere die Anzeige
            float distanceInKm = stepCount * stepLengthInMeter / 1000;
            distanceTextView.setText(String.format(Locale.getDefault(), "Distanz: %.2f km", distanceInKm));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    // Methode zum Pausieren und Fortsetzen des Zählers
    public void onPauseButtonClicked(View view) {
        if (isPaused) {
            isPaused = false;
            startTime = System.currentTimeMillis() - timePaused;
            timerHandler.postDelayed(timerRunnable, 0);
        } else {
            isPaused = true;
            timerHandler.removeCallbacks(timerRunnable);
            timePaused = System.currentTimeMillis() - startTime;
        }
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
