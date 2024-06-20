package at.htlwels.austrofit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class SignUp extends AppCompatActivity {

    // UI-Elemente deklarieren
    private TextInputEditText textInputEditTextFname, textInputEditTextLname, textInputEditTextUserName, textInputEditTextPassword, textInputEditTextBDay, textInputEditTextEmail;
    private Button buttonSignUp;
    private TextView textViewLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Überprüfe, ob der Benutzer angemeldet ist
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            // Wenn der Benutzer angemeldet ist, starte die MainActivity
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish(); // Schließe die aktuelle Aktivität
        } else {
            // Wenn der Benutzer nicht angemeldet ist, starte die SignUpActivity
            setContentView(R.layout.activity_signup);
        }

        // UI-Elemente mit ihren XML-IDs verbinden
        textInputEditTextFname = findViewById(R.id.firstNameSignUp);
        textInputEditTextLname = findViewById(R.id.lastNameSignUp);
        textInputEditTextUserName = findViewById(R.id.userNameSignUp);
        textInputEditTextPassword = findViewById(R.id.passwordSignUp);
        textInputEditTextBDay = findViewById(R.id.birthDaySignUp);
        textInputEditTextEmail = findViewById(R.id.emailSignUp);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        textViewLogIn = findViewById(R.id.textViewLogIn);

        // Klick-Listener für den Log-In-TextView
        textViewLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Starte die LogIn-Aktivität
                Intent intent = new Intent(getApplicationContext(), LogIn.class);
                startActivity(intent);
                finish(); // Schließe die aktuelle Aktivität
            }
        });

        // Klick-Listener für den Sign-Up-Button
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Benutzerdaten aus den Eingabefeldern abrufen
                String firstName = String.valueOf(textInputEditTextFname.getText());
                String lastName = String.valueOf(textInputEditTextLname.getText());
                String userName = String.valueOf(textInputEditTextUserName.getText());
                String password = String.valueOf(textInputEditTextPassword.getText());
                String birthDay = String.valueOf(textInputEditTextBDay.getText());
                String email = String.valueOf(textInputEditTextEmail.getText());

                // Überprüfen, ob alle Felder ausgefüllt sind
                if (!firstName.equals("") && !lastName.equals("") && !userName.equals("") && !password.equals("") && !birthDay.equals("") && !email.equals("")) {
                    // Handler und Runnable zum Senden der Daten an den Server
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            // Felder für die HTTP-Anfrage erstellen
                            String[] field = new String[6];
                            field[0] = "firstName";
                            field[1] = "lastName";
                            field[2] = "userName";
                            field[3] = "password";
                            field[4] = "birthDay";
                            field[5] = "email";

                            // Daten für die HTTP-Anfrage erstellen
                            String[] data = new String[6];
                            data[0] = firstName;
                            data[1] = lastName;
                            data[2] = userName;
                            data[3] = password;
                            data[4] = birthDay;
                            data[5] = email;

                            // PutData-Objekt erstellen und HTTP-Anfrage starten
                            PutData putData = new PutData("http://172.17.77.221/login/signup.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if (result.equals("Registrierung erfolgreich")) {
                                        // Erfolgreiche Registrierung
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), LogIn.class);
                                        startActivity(intent);
                                        finish(); // Schließe die aktuelle Aktivität
                                    } else {
                                        // Fehlgeschlagene Registrierung
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                } else {
                    // Fehlermeldung anzeigen, wenn nicht alle Felder ausgefüllt sind
                    Toast.makeText(getApplicationContext(), "Bitte füllen Sie alle Felder aus", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
