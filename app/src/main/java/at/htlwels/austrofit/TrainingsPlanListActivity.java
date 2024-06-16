package at.htlwels.austrofit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Map;

public class TrainingsPlanListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainings_plan_list);

        ListView planListView = findViewById(R.id.planListView);

        SharedPreferences sharedPreferences = getSharedPreferences("TrainingPlans", MODE_PRIVATE);
        Map<String, ?> allPlans = sharedPreferences.getAll();
        ArrayList<String> planNames = new ArrayList<>(allPlans.keySet());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, planNames);
        planListView.setAdapter(adapter);

        planListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String planName = planNames.get(position);
                Intent intent = new Intent(TrainingsPlanListActivity.this, TrainingsPlanDetailsActivity.class);
                intent.putExtra("planName", planName);
                startActivity(intent);
            }
        });
    }
}
