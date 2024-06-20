package at.htlwels.austrofit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    // UI-Elemente deklarieren
    private Button buttonLogIn, signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Aktiviert Edge-to-Edge-Display-Modus
        EdgeToEdge.enable(this);

        // Überprüfe, ob der Benutzer angemeldet ist
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            // Wenn der Benutzer angemeldet ist, starte die MainActivity
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish(); // Schließe die aktuelle Aktivität
        } else {
            // Wenn der Benutzer nicht angemeldet ist, zeige die WelcomeActivity an
            setContentView(R.layout.activity_welcome);
        }

        // UI-Elemente mit ihren XML-IDs verbinden
        buttonLogIn = findViewById(R.id.buttonLogIn);
        signUp = findViewById(R.id.buttonSignIn);

        // Klick-Listener für den Sign-Up-Button
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Starte die SignUp-Aktivität
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
                finish(); // Schließe die aktuelle Aktivität
            }
        });

        // Klick-Listener für den Log-In-Button
        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Starte die LogIn-Aktivität
                Intent intent = new Intent(getApplicationContext(), LogIn.class);
                startActivity(intent);
                finish(); // Schließe die aktuelle Aktivität
            }
        });
    }
}
