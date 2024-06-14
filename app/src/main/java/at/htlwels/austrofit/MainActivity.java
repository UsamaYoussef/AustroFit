package at.htlwels.austrofit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DrawerLayout drawerLayout = findViewById(R.id.layoutDrawer);

        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.navigation);
        View headerView = navigationView.getHeaderView(0);
        TextView textViewFullName = headerView.findViewById(R.id.textViewFullName);
        TextView textViewUsername = headerView.findViewById(R.id.textViewUsername);

        // Lade Benutzerdaten aus SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String firstName = sharedPreferences.getString("firstName", "First Name");
        String lastName = sharedPreferences.getString("lastName", "Last Name");
        String username = sharedPreferences.getString("username", "Username");

        // Kombiniere Vor- und Nachnamen
        String fullName = firstName + " " + lastName;

        // Setze die TextViews mit den Benutzerdaten
        textViewFullName.setText(fullName);
        textViewUsername.setText(username);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menuAbmelden) {
                    // Benutzer abmelden
                    logOut();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                    // Weitere Menüaktionen hier
                } else {
                    return false;
                }
            }
        });
    }

    private void logOut() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("username");
        editor.remove("firstName");
        editor.remove("lastName");
        editor.putBoolean("isLoggedIn", false);
        editor.apply();

        // WelcomeActivity starten nach Abmeldung
        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
        startActivity(intent);
        finish();
    }
}