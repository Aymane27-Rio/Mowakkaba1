package com.example.mowakkaba;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Mowakkaba.db";
    private static final String TABLE_USERS = "users";

    // Columns
    private static final String COL_ID = "id";
    private static final String COL_FIRST_NAME = "first_name";
    private static final String COL_LAST_NAME = "last_name";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";
    private static final String COL_USER_TYPE = "user_type"; // Client or Coach
    private static final String COL_INFO = "info"; // Objectives (Clients) or Skills (Coaches)

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_FIRST_NAME + " TEXT, " +
                COL_LAST_NAME + " TEXT, " +
                COL_EMAIL + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT, " +
                COL_USER_TYPE + " TEXT, " +
                COL_INFO + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Insert a new user into the database
    public boolean insertUser(String firstName, String lastName, String email, String password, String userType, String info) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_FIRST_NAME, firstName);
        contentValues.put(COL_LAST_NAME, lastName);
        contentValues.put(COL_EMAIL, email);
        contentValues.put(COL_PASSWORD, password);
        contentValues.put(COL_USER_TYPE, userType);
        contentValues.put(COL_INFO, info);

        long result = db.insert(TABLE_USERS, null, contentValues);
        return result != -1; // Return true if insert was successful
    }

    // Fetch a user by email and password
    public Cursor getUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT * FROM " + TABLE_USERS + " WHERE email = ? AND password = ?",
                new String[]{email, password}
        );
    }

    // Fetch all coaches
    public Cursor getAllCoaches() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE user_type = ?", new String[]{"Coach"});
    }

    // Fetch all recent graduates (for matching purposes)
    public Cursor getAllRecentGraduates() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE user_type = ?", new String[]{"Recent Graduate"});
    }
}