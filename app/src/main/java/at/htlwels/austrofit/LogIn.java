package at.htlwels.austrofit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
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

    // UI-Elemente deklarieren
    private TextInputEditText textInputEditTextUsername, textInputEditTextPasswd;
    private Button buttonLogIn;
    private TextView textViewSignUp;

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
            // Wenn der Benutzer nicht angemeldet ist, starte die LogInActivity
            setContentView(R.layout.activity_login);
        }

        // UI-Elemente mit ihren XML-IDs verbinden
        textInputEditTextUsername = findViewById(R.id.userNameLogIn);
        textInputEditTextPasswd = findViewById(R.id.passwordLogin);
        buttonLogIn = findViewById(R.id.buttonLogIn);
        textViewSignUp = findViewById(R.id.textViewSignUp);

        // Klick-Listener für den Sign-Up-TextView
        textViewSignUp.setOnClickListener(new View.OnClickListener() {
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
                String username = String.valueOf(textInputEditTextUsername.getText());
                String passwd = String.valueOf(textInputEditTextPasswd.getText());

                if (!username.equals("") && !passwd.equals("")) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = new String[2];
                            field[0] = "userName";
                            field[1] = "password";

                            String[] data = new String[2];
                            data[0] = username;
                            data[1] = passwd;

                            PutData putData = new PutData("http://172.17.77.221/login/login.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    Log.d("LogInResponse", result); // Log die Antwort
                                    try {
                                        JSONObject jsonObject = new JSONObject(result);
                                        if (jsonObject.getString("status").equals("Login erfolgreich")) {
                                            Toast.makeText(getApplicationContext(), "Login erfolgreich", Toast.LENGTH_SHORT).show();

                                            String firstName = jsonObject.getString("firstName");
                                            String lastName = jsonObject.getString("lastName");

                                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LogIn.this);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("userName", username);
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
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Bitte füllen Sie alle Felder aus", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
