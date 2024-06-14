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

import org.json.JSONException;
import org.json.JSONObject;

public class LogIn extends AppCompatActivity {

    TextInputEditText textInputEditTextUsername, textInputEditTextPasswd;
    Button buttonLogIn;
    TextView textViewSignUp;

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
            finish();
        } else {
            // Wenn der Benutzer nicht angemeldet ist, starte die LogInActivity
            setContentView(R.layout.activity_login);
        }

        textInputEditTextUsername = findViewById(R.id.usernameLogIn);
        textInputEditTextPasswd = findViewById(R.id.passwdLogIn);
        buttonLogIn = findViewById(R.id.buttonLogIn);
        textViewSignUp = findViewById(R.id.textSignUp);

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username, passwd;
                username = String.valueOf(textInputEditTextUsername.getText());
                passwd = String.valueOf(textInputEditTextPasswd.getText());

                if (!username.equals("") && !passwd.equals("")) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[2];
                            field[0] = "benutzername";
                            field[1] = "passwort";
                            //Creating array for data
                            String[] data = new String[2];
                            data[0] = username;
                            data[1] = passwd;
                            PutData putData = new PutData("http://192.168.1.12/login/login.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    try {
                                        JSONObject jsonObject = new JSONObject(result);
                                        if (jsonObject.getString("status").equals("Login erfolgreich")) {
                                            Toast.makeText(getApplicationContext(), "Login erfolgreich", Toast.LENGTH_SHORT).show();

                                            // Abrufen des Vor- und Nachnamens
                                            String firstName = jsonObject.getString("vorname");
                                            String lastName = jsonObject.getString("nachname");

                                            // Speichere die Benutzerdaten in SharedPreferences
                                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LogIn.this);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("username", username);
                                            editor.putString("firstName", firstName);
                                            editor.putString("lastName", lastName);
                                            editor.putBoolean("isLoggedIn", true);
                                            editor.apply();

                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), jsonObject.getString("status"), Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getApplicationContext(), "Fehler beim Parsen der Antwort", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Bitte füllen Sie alle Felder aus", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
