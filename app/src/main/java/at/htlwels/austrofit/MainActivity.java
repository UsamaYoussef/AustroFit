package at.htlwels.austrofit;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TabHost;

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

        TabHost th = (TabHost) findViewById(R.id.tabHost);
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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}