package at.htlwels.austrofit;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TrainingsPlanDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainings_plan_details);

        TextView planDetailsTextView = findViewById(R.id.planDetailsTextView);

        String planName = getIntent().getStringExtra("planName");

        SharedPreferences sharedPreferences = getSharedPreferences("TrainingPlans", MODE_PRIVATE);
        String planDetails = sharedPreferences.getString(planName, "Kein Plan gefunden");

        planDetailsTextView.setText(planDetails);
    }
}
