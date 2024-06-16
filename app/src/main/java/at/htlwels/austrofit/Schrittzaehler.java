package at.htlwels.austrofit;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;

public class Schrittzaehler implements SensorEventListener {

    private Context context;
    private TextView stepCountTextView;
    private TextView distanceTextView;
    private TextView timeTextView;
    private TextView stepCountTargetTextView;
    private ProgressBar progressBar;

    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private int stepCount = 0;
    private boolean isPaused = false;
    private long timePaused = 0;
    private float stepLengthInMeter = 0.762f;
    private long startTime;
    private int stepCountTarget = 6000;
    private int initialStepCount = -1;
    private Handler timerHandler = new Handler();
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "StepCounterPrefs";
    private static final String STEP_COUNT_KEY = "stepCountKey";

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int min = seconds / 60;
            seconds = seconds % 60;
            timeTextView.setText(String.format(Locale.getDefault(), "Time: %02d:%02d", min, seconds));
            timerHandler.postDelayed(this, 1000);
        }
    };

    public Schrittzaehler(Context context, View view) {
        this.context = context;

        stepCountTextView = view.findViewById(R.id.stepCountTextView);
        distanceTextView = view.findViewById(R.id.distanceTextView);
        timeTextView = view.findViewById(R.id.timeTextView);
        stepCountTargetTextView = view.findViewById(R.id.stepCountTargetTextView);
        progressBar = view.findViewById(R.id.progressBar);

        startTime = System.currentTimeMillis();
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        progressBar.setMax(stepCountTarget);
        stepCountTargetTextView.setText("Step Goal: " + stepCountTarget);

        if (stepCounterSensor == null) {
            stepCountTextView.setText("Step counter not available");
        }
    }

    public void resume() {
        if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
            timerHandler.postDelayed(timerRunnable, 0);
            stepCount = sharedPreferences.getInt(STEP_COUNT_KEY, 0);
            stepCountTextView.setText("Step Count: " + stepCount);
            progressBar.setProgress(stepCount);
            float distanceInKm = stepCount * stepLengthInMeter / 1000;
            distanceTextView.setText(String.format(Locale.getDefault(), "Distance: %.2f km", distanceInKm));
        }
    }

    public void stop() {
        if (stepCounterSensor != null) {
            sensorManager.unregisterListener(this);
            timerHandler.removeCallbacks(timerRunnable);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(STEP_COUNT_KEY, stepCount);
            editor.apply();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            int currentSteps = (int) sensorEvent.values[0];
            if (initialStepCount < 0) {
                initialStepCount = currentSteps;
            }
            stepCount = currentSteps - initialStepCount + sharedPreferences.getInt(STEP_COUNT_KEY, 0);
            stepCountTextView.setText("Step Count: " + stepCount);
            progressBar.setProgress(stepCount);
            if (stepCount >= stepCountTarget) {
                stepCountTargetTextView.setText("Schrittziel erreicht!");
            }
            float distanceInKm = stepCount * stepLengthInMeter / 1000;
            distanceTextView.setText(String.format(Locale.getDefault(), "Distance: %.2f km", distanceInKm));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

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
}
