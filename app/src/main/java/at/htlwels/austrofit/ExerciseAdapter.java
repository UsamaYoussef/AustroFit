package at.htlwels.austrofit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ExerciseAdapter extends ArrayAdapter<Exercise> {

    // Konstruktor für den Adapter
    public ExerciseAdapter(Context context, ArrayList<Exercise> exercises) {
        super(context, 0, exercises);
    }

    // Methode zur Erstellung der Listenansicht für jede Übung
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Übung an aktueller Position erhalten
        Exercise exercise = getItem(position);

        // Wenn die Ansicht noch nicht wiederverwendet wird, eine neue Ansicht erstellen
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_exercise, parent, false);
        }

        // UI-Elemente innerhalb des Listenelements finden
        TextView textViewName = convertView.findViewById(R.id.textViewName);
        TextView textViewReps = convertView.findViewById(R.id.textViewReps);
        TextView textViewSets = convertView.findViewById(R.id.textViewSets);

        // Daten der Übung in die UI-Elemente setzen
        textViewName.setText(exercise.getName());
        textViewReps.setText("Wiederholungen: " + exercise.getReps());
        textViewSets.setText("Sätze: " + exercise.getSets());

        // Ansicht zurückgeben
        return convertView;
    }
}