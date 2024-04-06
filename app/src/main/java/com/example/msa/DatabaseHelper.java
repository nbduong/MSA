package com.example.msa;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ratings.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "ratings";
    private static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_REVIEW = "review";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_RATING + " REAL, " +
                COLUMN_REVIEW + " TEXT)";
        db.execSQL(createTableQuery);

        addSampleData(db, "Alice", 4.5f, "Great movie!");
        addSampleData(db, "Bob", 3.0f, "Average movie");
        addSampleData(db, "Charlie", 5.0f, "Excellent!");
        addSampleData(db, "David", 2.5f, "Not bad");
        addSampleData(db, "Eve", 1.0f, "Terrible");
    }
    private void addSampleData(SQLiteDatabase db, String name, float rating, String review) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_RATING, rating);
        contentValues.put(COLUMN_REVIEW, review);
        db.insert(TABLE_NAME, null, contentValues);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addRating(String name, float rating, String review) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_RATING, rating);
        contentValues.put(COLUMN_REVIEW, review);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor getAllRatings() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public String getAllRatingsAsString() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        StringBuilder ratingText = new StringBuilder();
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            @SuppressLint("Range") float rating = cursor.getFloat(cursor.getColumnIndex(COLUMN_RATING));
            @SuppressLint("Range") String review = cursor.getString(cursor.getColumnIndex(COLUMN_REVIEW));
            ratingText.append("Tên người dùng: ").append(name).append("\n");
            ratingText.append("Đánh giá: ").append(rating).append("\n");
            ratingText.append("Nhận xét: ").append(review).append("\n\n");
        }
        cursor.close();
        return ratingText.toString();
    }
    public float getAverageRating() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT AVG(" + COLUMN_RATING + ") FROM " + TABLE_NAME, null);
        float averageRating = 0;
        if (cursor.moveToFirst()) {
            averageRating = cursor.getFloat(0);
        }
        cursor.close();
        return averageRating;
    }

}