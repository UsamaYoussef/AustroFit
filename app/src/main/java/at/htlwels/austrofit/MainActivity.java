package at.htlwels.austrofit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        TabHost th = findViewById(R.id.tabHost);
        th.setup();
        TabHost.TabSpec specs = th.newTabSpec("Tag1");
        specs.setContent(R.id.tabPlan);
        specs.setIndicator("Planer");
        th.addTab(specs);

        specs = th.newTabSpec("Tag2");
        specs.setContent(R.id.tabKalorien);
        specs.setIndicator("Tracken");
        th.addTab(specs);

        specs = th.newTabSpec("Tag3");
        specs.setContent(R.id.tabZaehler);
        specs.setIndicator("Schrittzähler");
        th.addTab(specs);

        specs = th.newTabSpec("Tag4");
        specs.setContent(R.id.tabZiele);
        specs.setIndicator("Ziele");
        th.addTab(specs);

        th.setOnTabChangedListener(new OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                // Keine Aktion erforderlich
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button createNewPlanButton = findViewById(R.id.createNewPlanButton);
        Button viewSavedPlansButton = findViewById(R.id.viewSavedPlansButton);

        createNewPlanButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TrainingsPlanActivity.class);
            startActivity(intent);
        });

        viewSavedPlansButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TrainingsPlanListActivity.class);
            startActivity(intent);
        });
    }
}
