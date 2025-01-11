package com.example.mowakkaba;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "Mowakkaba.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";

    // Column Names
    private static final String COL_ID = "id";
    private static final String COL_FIRST_NAME = "first_name";
    private static final String COL_LAST_NAME = "last_name";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";
    private static final String COL_USER_TYPE = "user_type"; // Client, Professional, or Mentor
    private static final String COL_INFO = "info"; // Additional Info (e.g., objectives, skills)

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_FIRST_NAME + " TEXT NOT NULL, " +
                COL_LAST_NAME + " TEXT NOT NULL, " +
                COL_EMAIL + " TEXT NOT NULL UNIQUE, " +
                COL_PASSWORD + " TEXT NOT NULL, " +
                COL_USER_TYPE + " TEXT NOT NULL, " +
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

        try {
            long result = db.insert(TABLE_USERS, null, contentValues);
            return result != -1;
        } catch (Exception e) {
            Log.e(TAG, "Error inserting user: " + e.getMessage());
            return false;
        }
    }

    // Check if an email already exists
    public boolean doesEmailExist(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE email = ?", new String[]{email});
            return (cursor.getCount() > 0);
        } catch (Exception e) {
            Log.e(TAG, "Error checking email existence: " + e.getMessage());
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    // Fetch a user by email and password
    public Cursor getUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT * FROM " + TABLE_USERS + " WHERE email = ? AND password = ?",
                new String[]{email, password}
        );
    }

    // Fetch a user by email only
    public Cursor getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT * FROM " + TABLE_USERS + " WHERE email = ?",
                new String[]{email}
        );
    }

    // Fetch all coaches for matching purposes
    public Cursor getAllCoaches() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT * FROM " + TABLE_USERS + " WHERE user_type = ?",
                new String[]{"Coach/Mentor"}
        );
    }
}