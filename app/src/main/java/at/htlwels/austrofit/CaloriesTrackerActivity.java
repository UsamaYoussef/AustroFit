package at.htlwels.austrofit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

public class CaloriesTrackerActivity extends AppCompatActivity {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private TextView textViewTotalCalories;
    private FoodAdapter foodAdapter;
    private SharedPreferences sharedPreferences;
    private DataBase db;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_caloriestracker);

        final DrawerLayout drawerLayout = findViewById(R.id.layoutDrawer);

        findViewById(R.id.imageViewMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0); // Lade den Header
        TextView textViewFullNameHeader = headerView.findViewById(R.id.fullName);
        TextView textViewUsernameHeader = headerView.findViewById(R.id.userName);

        db = new DataBase(this);

        // Lade Benutzerdaten aus SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getInt("userId", -1);
        String firstName = sharedPreferences.getString("firstName", "First Name");
        String lastName = sharedPreferences.getString("lastName", "Last Name");
        String userName = sharedPreferences.getString("userName", "Username");

        // Kombiniere Vor- und Nachnamen
        String fullName = firstName + " " + lastName;

        // Setze die TextViews mit den Benutzerdaten
        textViewFullNameHeader.setText(fullName);
        textViewUsernameHeader.setText(userName);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.menuLogOut) {
                    // Benutzer abmelden
                    logOut();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                } else if (itemId == R.id.menuGoalSetting) {
                    // Kalorien-Tracker starten
                    Intent intent = new Intent(CaloriesTrackerActivity.this, GoalSettingActivity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                } else if (itemId == R.id.menuStepCounter) {
                    // Schrittzähler starten
                    Intent intent = new Intent(CaloriesTrackerActivity.this, StepCounterActivity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                } else if (itemId == R.id.menuTrainingPlan) {
                    // Trainingsplan starten
                    Intent intent = new Intent(CaloriesTrackerActivity.this, TrainingPlanActivity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                } else if (itemId == R.id.menuProfile) {
                    // MainActivity starten
                    Intent intent = new Intent(CaloriesTrackerActivity.this, MainActivity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
                // Weitere Menüaktionen hier
                return false;
            }
        });

        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);
        textViewTotalCalories = findViewById(R.id.textViewTotalCalories);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        db = new DataBase(this);

        userId = sharedPreferences.getInt("userId", -1);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        foodAdapter = new FoodAdapter();
        recyclerView.setAdapter(foodAdapter);

        foodAdapter.setOnItemClickListener(new FoodAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Food food) {
                addFoodCalories(food.getCalories());
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchForFood(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        checkForNewDay();
        updateTotalCalories();
    }

    private void searchForFood(String query) {
        LinkedList<Food> foodList = db.searchFood(query);
        foodAdapter.setFoodList(foodList);
    }

    private void updateTotalCalories() {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        int totalCalories = db.getTotalCalories(userId, today);
        textViewTotalCalories.setText("Gesamtkalorien: " + totalCalories);
    }

    private void checkForNewDay() {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String savedDate = sharedPreferences.getString("date", "");

        if (!today.equals(savedDate)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("date", today);
            editor.apply();
            db.addCalories(userId, today, 0);
        }
    }

    public void addFoodCalories(int calories) {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        db.addCalories(userId, today, calories);
        updateTotalCalories();
    }

    private void logOut() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("username");
        editor.remove("userId");
        editor.remove("fullName");
        editor.putBoolean("isLoggedIn", false);
        editor.apply();

        // LoginActivity starten nach Abmeldung
        Intent intent = new Intent(getApplicationContext(), LogIn.class);
        startActivity(intent);
        finish();
    }
}