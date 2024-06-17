package at.htlwels.austrofit;

import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.HashMap;
import java.util.Map;

public class TrainingsPlanActivity extends AppCompatActivity {

    private Map<Integer, TrainingsEinheit> trainingsEinheiten;
    private int currentDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainingsplan);

        trainingsEinheiten = new HashMap<>();
        currentDay = 1;

        EditText planName = findViewById(R.id.planName);
        Spinner daySpinner = findViewById(R.id.daySpinner);
        EditText uebungName = findViewById(R.id.uebungName);
        EditText uebungMuskel = findViewById(R.id.uebungMuskel);
        EditText uebungSaetze = findViewById(R.id.uebungSaetze);
        EditText uebungWiederholungen = findViewById(R.id.uebungWiederholungen);
        EditText uebungGewicht = findViewById(R.id.uebungGewicht);
        Button addUebungButton = findViewById(R.id.addUebungButton);
        Button savePlanButton = findViewById(R.id.savePlanButton);
        LinearLayout uebungListLayout = findViewById(R.id.uebungListLayout);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.days_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(adapter);

        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                currentDay = position + 1;
                updateUebungList(uebungListLayout);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        addUebungButton.setOnClickListener(v -> {
            String name = uebungName.getText().toString();
            String muskel = uebungMuskel.getText().toString();
            int saetze = Integer.parseInt(uebungSaetze.getText().toString());
            int wiederholungen = Integer.parseInt(uebungWiederholungen.getText().toString());
            double gewicht = Double.parseDouble(uebungGewicht.getText().toString());

            TrainingsUebung uebung = new TrainingsUebung(name, muskel, gewicht, saetze, wiederholungen);
            TrainingsEinheit einheit = trainingsEinheiten.getOrDefault(currentDay, new TrainingsEinheit());
            einheit.addUebung(uebung);
            trainingsEinheiten.put(currentDay, einheit);

            updateUebungList(uebungListLayout);

            uebungName.setText("");
            uebungMuskel.setText("");
            uebungSaetze.setText("");
            uebungWiederholungen.setText("");
            uebungGewicht.setText("");
        });

        savePlanButton.setOnClickListener(v -> {
            String planNameString = planName.getText().toString();
            if (planNameString.isEmpty()) {
                Toast.makeText(this, "Bitte geben Sie einen Namen für den Plan ein", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences sharedPreferences = getSharedPreferences("TrainingPlans", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            StringBuilder planDetails = new StringBuilder();
            for (int day : trainingsEinheiten.keySet()) {
                planDetails.append("Tag ").append(day).append(":\n");
                for (TrainingsUebung uebung : trainingsEinheiten.get(day).getUebungen()) {
                    planDetails.append(uebung.getNameUebung()).append(" - ").append(uebung.getMuskel())
                            .append(" - ").append(uebung.getGewicht()).append("kg - ").append(uebung.getSaetze())
                            .append("x").append(uebung.getWiederholungen()).append("\n");
                }
            }

            editor.putString(planNameString, planDetails.toString());
            editor.apply();

            Toast.makeText(this, "Trainingsplan gespeichert", Toast.LENGTH_SHORT).show();

            // Zurück zur Planer-Übersichtsseite
            Intent intent = new Intent(TrainingsPlanActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void updateUebungList(LinearLayout uebungListLayout) {
        uebungListLayout.removeAllViews();
        if (trainingsEinheiten.containsKey(currentDay)) {
            for (TrainingsUebung uebung : trainingsEinheiten.get(currentDay).getUebungen()) {
                TextView uebungView = new TextView(this);
                uebungView.setText(uebung.getNameUebung() + " - " + uebung.getMuskel() + " - " + uebung.getGewicht() + "kg - " + uebung.getSaetze() + "x" + uebung.getWiederholungen());
                uebungListLayout.addView(uebungView);
            }
        }
    }
}
