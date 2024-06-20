package at.htlwels.austrofit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.LinkedList;

public class DataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "austrofit.db";
    private static final int DATABASE_VERSION = 3;

    // Benutzer Tabelle
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_NAME = "userName";
    private static final String COLUMN_USER_PASSWORD = "password";
    private static final String COLUMN_USER_FIRST_NAME = "firstName";
    private static final String COLUMN_USER_LAST_NAME = "lastName";

    // Lebensmittel Tabelle
    private static final String TABLE_FOOD = "food";
    private static final String COLUMN_FOOD_ID = "id";
    private static final String COLUMN_FOOD_NAME = "name";
    private static final String COLUMN_FOOD_WEIGHT = "weight";
    private static final String COLUMN_FOOD_CALORIES = "calories";

    // Kalorienverbrauch Tabelle
    private static final String TABLE_CALORIES = "caloriesConsumption";
    private static final String COLUMN_CALORIES_ID = "id";
    private static final String COLUMN_CALORIES_USER_ID = "userId";
    private static final String COLUMN_CALORIES_DATE = "date";
    private static final String COLUMN_CALORIES_AMOUNT = "calories";

    // Trainingsplan Tabelle
    private static final String TABLE_EXERCISES = "exercises";
    private static final String COLUMN_EXERCISES_ID = "id";
    private static final String COLUMN_EXERCISES_USER_ID = "userId";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_REPS = "reps";
    private static final String COLUMN_SETS = "sets";
    private static final String COLUMN_DAY = "day";

    // Schrittzähler Tabelle
    private static final String TABLE_STEPS = "steps";
    private static final String COLUMN_STEPS_ID = "id";
    private static final String COLUMN_STEPS_USER_ID = "userID";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_STEP_COUNT = "stepCount";

    // Ziele Tabelle
    private static final String TABLE_GOALS = "goals";
    private static final String COLUMN_GOALS_ID = "id";
    private static final String COLUMN_GOALS_USER_ID = "userId";
    private static final String COLUMN_GOALS = "goals";

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_NAME + " TEXT NOT NULL UNIQUE, " +
                COLUMN_USER_PASSWORD + " TEXT NOT NULL, " +
                COLUMN_USER_FIRST_NAME + " TEXT NOT NULL, " +
                COLUMN_USER_LAST_NAME + " TEXT NOT NULL)";
        db.execSQL(createUserTable);

        String createFoodTable = "CREATE TABLE " + TABLE_FOOD + " (" +
                COLUMN_FOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FOOD_NAME + " TEXT NOT NULL, " +
                COLUMN_FOOD_WEIGHT + " INTEGER NOT NULL, " +
                COLUMN_FOOD_CALORIES + " INTEGER NOT NULL)";
        db.execSQL(createFoodTable);

        String createCaloriesTable = "CREATE TABLE " + TABLE_CALORIES + " (" +
                COLUMN_CALORIES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CALORIES_USER_ID + " INTEGER NOT NULL, " +
                COLUMN_CALORIES_DATE + " TEXT NOT NULL, " +
                COLUMN_CALORIES_AMOUNT + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + COLUMN_CALORIES_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";
        db.execSQL(createCaloriesTable);

        String CREATE_EXERCISES_TABLE = "CREATE TABLE " + TABLE_EXERCISES + "("
                + COLUMN_EXERCISES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_EXERCISES_USER_ID + " INTEGER,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_REPS + " INTEGER,"
                + COLUMN_SETS + " INTEGER,"
                + COLUMN_DAY + " TEXT" + ")";
        db.execSQL(CREATE_EXERCISES_TABLE);

        String CREATE_STEPS_TABLE = "CREATE TABLE " + TABLE_STEPS + "("
                + COLUMN_STEPS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_STEPS_USER_ID + " INTEGER,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_STEP_COUNT + " INTEGER" + ")";
        db.execSQL(CREATE_STEPS_TABLE);

        String CREATE_GOALS_TABLE = "CREATE TABLE " + TABLE_GOALS + "("
                + COLUMN_GOALS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_GOALS_USER_ID + " INTEGER,"
                + COLUMN_GOALS + " TEXT" + ")";
        db.execSQL(CREATE_GOALS_TABLE);

        // Insert initial data
        insertInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STEPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GOALS);
        onCreate(db);
    }

    // Benutzer Funktionen
    public boolean addUser(String userName, String password, String firstName, String lastName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, userName);
        values.put(COLUMN_USER_PASSWORD, password);
        values.put(COLUMN_USER_FIRST_NAME, firstName);
        values.put(COLUMN_USER_LAST_NAME, lastName);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1; // Rückgabe, ob das Einfügen erfolgreich war
    }

    public boolean checkUser(String userName, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_USER_ID},
                COLUMN_USER_NAME + "=? AND " + COLUMN_USER_PASSWORD + "=?",
                new String[]{userName, password},
                null, null, null);

        int count = cursor.getCount();
        cursor.close();
        return count > 0; // Rückgabe, ob der Benutzer gefunden wurde
    }

    public int getUserId(String userName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_USER_ID},
                COLUMN_USER_NAME + "=?",
                new String[]{userName},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
            cursor.close();
            return userId;
        }
        return -1;
    }

    public String getUserFullName(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_USER_FIRST_NAME, COLUMN_USER_LAST_NAME},
                COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String fullName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_FIRST_NAME)) + " " +
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_LAST_NAME));
            cursor.close();
            return fullName;
        }
        return null;
    }

    // Lebensmittel Funktionen
    public LinkedList<Food> searchFood(String query) {
        LinkedList<Food> foodList = new LinkedList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FOOD,
                new String[]{COLUMN_FOOD_ID, COLUMN_FOOD_NAME, COLUMN_FOOD_WEIGHT, COLUMN_FOOD_CALORIES},
                COLUMN_FOOD_NAME + " LIKE ?",
                new String[]{"%" + query + "%"},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Food food = new Food(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FOOD_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOOD_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FOOD_WEIGHT)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FOOD_CALORIES))
                );
                foodList.add(food);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return foodList;
    }

    public void addFood(Food food) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FOOD_NAME, food.getName());
        values.put(COLUMN_FOOD_WEIGHT, food.getWeight());
        values.put(COLUMN_FOOD_CALORIES, food.getCalories());
        db.insert(TABLE_FOOD, null, values);
    }

    // Kalorienverbrauch Funktionen
    public void addCalories(int userId, String date, int calories) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CALORIES_USER_ID, userId);
        values.put(COLUMN_CALORIES_DATE, date);
        values.put(COLUMN_CALORIES_AMOUNT, calories);
        db.insert(TABLE_CALORIES, null, values);
    }

    public int getTotalCalories(int userId, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CALORIES,
                new String[]{"SUM(" + COLUMN_CALORIES_AMOUNT + ") AS totalCalories"},
                COLUMN_CALORIES_USER_ID + "=? AND " + COLUMN_CALORIES_DATE + "=?",
                new String[]{String.valueOf(userId), date},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int totalCalories = cursor.getInt(cursor.getColumnIndexOrThrow("totalCalories"));
            cursor.close();
            return totalCalories;
        }
        return 0;
    }

    // Trainingsplan Funktionen
    public void addExercise(int userId, String name, int reps, int sets, String day) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXERCISES_USER_ID, userId);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_REPS, reps);
        values.put(COLUMN_SETS, sets);
        values.put(COLUMN_DAY, day);

        db.insert(TABLE_EXERCISES, null, values);
    }

    public ArrayList<Exercise> getExercises(int userId, String day) {
        ArrayList<Exercise> exerciseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EXERCISES, new String[]{COLUMN_NAME, COLUMN_REPS, COLUMN_SETS, COLUMN_DAY},
                COLUMN_EXERCISES_USER_ID + "=? AND " + COLUMN_DAY + "=?",
                new String[]{String.valueOf(userId), day}, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Exercise exercise = new Exercise(cursor.getString(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3));
                exerciseList.add(exercise);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return exerciseList;
    }

    public String getTrainingPlan(int userId) {
        StringBuilder plan = new StringBuilder();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EXERCISES, new String[]{COLUMN_NAME, COLUMN_REPS, COLUMN_SETS, COLUMN_DAY},
                COLUMN_EXERCISES_USER_ID + "=?",
                new String[]{String.valueOf(userId)}, null, null, COLUMN_DAY, null);

        if (cursor.moveToFirst()) {
            do {
                plan.append(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DAY)))
                        .append(": ")
                        .append(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)))
                        .append(" - ")
                        .append(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REPS)))
                        .append(" reps x ")
                        .append(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SETS)))
                        .append(" sets\n");
            } while (cursor.moveToNext());
        }

        cursor.close();
        return plan.toString();
    }

    // Schrittzähler Funktionen
    public void addSteps(int userId, String date, int steps) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STEPS_USER_ID, userId);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_STEP_COUNT, steps);

        db.insert(TABLE_STEPS, null, values);
    }

    public int getSteps(int userId, String date) {
        int steps = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_STEPS, new String[]{COLUMN_STEP_COUNT},
                COLUMN_STEPS_USER_ID + "=? AND " + COLUMN_DATE + "=?",
                new String[]{String.valueOf(userId), date}, null, null, null, null);

        if (cursor.moveToFirst()) {
            steps = cursor.getInt(0);
        }

        cursor.close();
        return steps;
    }

    public void updateSteps(int userId, String date, int steps) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STEP_COUNT, steps);

        db.update(TABLE_STEPS, values, COLUMN_STEPS_USER_ID + "=? AND " + COLUMN_DATE + "=?",
                new String[]{String.valueOf(userId), date});
    }

    // Ziele Funktionen
    public void addGoals(int userId, String goals) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_GOALS_USER_ID, userId);
        values.put(COLUMN_GOALS, goals);

        db.insert(TABLE_GOALS, null, values);
    }

    public String getGoals(int userId) {
        String goals = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_GOALS, new String[]{COLUMN_GOALS},
                COLUMN_GOALS_USER_ID + "=?",
                new String[]{String.valueOf(userId)}, null, null, null, null);

        if (cursor.moveToFirst()) {
            goals = cursor.getString(0);
        }

        cursor.close();
        return goals;
    }

    private void insertInitialData(SQLiteDatabase db) {
        String[][] foodData = {
                {"Apfel", "100", "52"},
                {"Banane", "100", "89"},
                {"Karotte", "100", "41"},
                {"Brokkoli", "100", "55"},
                {"Pizza", "100", "266"},
                {"Burger", "100", "295"},
                {"Pommes", "100", "312"},
                {"Käse", "100", "402"},
                {"Hühnchenbrust", "100", "165"},
                {"Reis", "100", "130"},
                {"Nudeln", "100", "131"},
                {"Steak", "100", "271"},
                {"Lachs", "100", "208"},
                {"Eiscreme", "100", "207"},
                {"Schokolade", "100", "546"},
                {"Milch", "100", "42"},
                {"Orange", "100", "47"},
                {"Trauben", "100", "69"},
                {"Joghurt", "100", "59"},
                {"Kartoffeln", "100", "77"},
                {"Wurst", "100", "301"},
                {"Salat", "100", "15"},
                {"Tomate", "100", "18"},
                {"Gurke", "100", "16"},
                {"Avocado", "100", "160"},
                {"Erdbeeren", "100", "32"},
                {"Blaubeeren", "100", "57"},
                {"Mandeln", "100", "575"},
                {"Walnüsse", "100", "654"},
                {"Olivenöl", "100", "884"},
                {"Butter", "100", "717"},
                {"Honig", "100", "304"},
                {"Ei", "100", "155"},
                {"Toastbrot", "100", "265"},
                {"Vollkornbrot", "100", "247"},
                {"Cornflakes", "100", "357"},
                {"Haferflocken", "100", "389"},
                {"Apfelsaft", "100", "46"},
                {"Orangensaft", "100", "45"},
                {"Cola", "100", "42"},
                {"Kaffee", "100", "2"},
                {"Tee", "100", "1"},
                {"Bier", "100", "43"},
                {"Wein", "100", "85"},
                {"Wodka", "100", "231"},
                {"Whisky", "100", "250"}
        };

        for (String[] food : foodData) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_FOOD_NAME, food[0]);
            values.put(COLUMN_FOOD_WEIGHT, food[1]);
            values.put(COLUMN_FOOD_CALORIES, food[2]);
            db.insert(TABLE_FOOD, null, values);
        }
    }
}
