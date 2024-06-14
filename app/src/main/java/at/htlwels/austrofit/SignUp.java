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

    TextInputEditText textInputEditTextFname, textInputEditTextLname, textInputEditTextUsername, textInputEditTextPasswd, textInputEditTextBDay, textInputEditTextEmail;
    Button buttonSignUp;
    TextView textViewLogIn;

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
            // Wenn der Benutzer nicht angemeldet ist, starte die SignUpActivity
            setContentView(R.layout.activity_signup);
        }

        textInputEditTextFname = findViewById(R.id.fNameSignUp);
        textInputEditTextLname = findViewById(R.id.lNameSignUp);
        textInputEditTextUsername = findViewById(R.id.usernameSignUp);
        textInputEditTextPasswd = findViewById(R.id.passwdSignUp);
        textInputEditTextBDay = findViewById(R.id.bDaySignUp);
        textInputEditTextEmail = findViewById(R.id.emailSignUp);
        buttonSignUp = findViewById(R.id.signUp);
        textViewLogIn = findViewById(R.id.textLogIn);

        textViewLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LogIn.class);
                startActivity(intent);
                finish();
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fName, lName, username, passwd, bDay, email;
                fName = String.valueOf(textInputEditTextFname.getText());
                lName = String.valueOf(textInputEditTextLname.getText());
                username = String.valueOf(textInputEditTextUsername.getText());
                passwd = String.valueOf(textInputEditTextPasswd.getText());
                bDay = String.valueOf(textInputEditTextBDay.getText());
                email = String.valueOf(textInputEditTextEmail.getText());

                if (!fName.equals("") && !lName.equals("") && !username.equals("") && !passwd.equals("") && !bDay.equals("") && !email.equals("")) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[6];
                            field[0] = "vorname";
                            field[1] = "nachname";
                            field[2] = "benutzername";
                            field[3] = "passwort";
                            field[4] = "geburtsdatum";
                            field[5] = "email";
                            //Creating array for data
                            String[] data = new String[6];
                            data[0] = fName;
                            data[1] = lName;
                            data[2] = username;
                            data[3] = passwd;
                            data[4] = bDay;
                            data[5] = email;
                            PutData putData = new PutData("http://192.168.1.12/login/signup.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if (result.equals("Registrierung erfolgreich")) {
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), LogIn.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
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