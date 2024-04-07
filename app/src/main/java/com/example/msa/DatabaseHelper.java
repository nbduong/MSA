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
    public static final String COLUMN_NAMEMOVIE;

    static {
        COLUMN_NAMEMOVIE = "movie";
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_RATING + " REAL, " +
                COLUMN_REVIEW + " TEXT, " +
                COLUMN_NAMEMOVIE + " TEXT)";

        db.execSQL(createTableQuery);

        addSampleData(db, "Alice", 4.5f, "Great movie!","Big Buck Bunny");
        addSampleData(db, "Bob", 3.0f, "Average movie","Elephant Dream");
        addSampleData(db, "Charlie", 5.0f, "Excellent!","For Bigger Blazes");
        addSampleData(db, "David", 2.5f, "Not bad","For Bigger Escape");
        addSampleData(db, "Eve", 1.0f, "Terrible","For Bigger Fun");
        addSampleData(db, "Eve12", 2.0f, "Terrible","For Bigger Joyrides");
        addSampleData(db, "Eve14", 2.0f, "Terrible","For Bigger Meltdowns");
        addSampleData(db, "Eve14", 4.0f, "Terrible","Sintel");
        addSampleData(db, "Eve15", 5.0f, "Terrible","Subaru Outback On Street And Dirt");
        addSampleData(db, "Eve16", 3.0f, "Terrible","Tears of Steel");
        addSampleData(db, "Eve14", 2.0f, "Terrible","Volkswagen GTI Review");
        addSampleData(db, "Eve14", 2.0f, "Terrible","We Are Going On Bullrun");
    }
    //Tạo database với bản ghi có sẵn
    private void addSampleData(SQLiteDatabase db, String name, float rating, String review,String movie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_RATING, rating);
        contentValues.put(COLUMN_REVIEW, review);
        contentValues.put(COLUMN_NAMEMOVIE, movie);
        db.insert(TABLE_NAME, null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    //Thêm bản ghi vào database
    public boolean addRating(String name, float rating, String review,String movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_RATING, rating);
        contentValues.put(COLUMN_REVIEW, review);
        contentValues.put(COLUMN_NAMEMOVIE,movie);
        long result = db.insert(TABLE_NAME, null, contentValues);
        System.out.println(result);
        return result !=-1;
    }

    public Cursor getAllRatings() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
    //lấy dữ liệu đánh giá
    public String getAllRatingsAsString(String a) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = { a };
        //Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME+"WHERE ", null);
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAMEMOVIE + " = ?", selectionArgs);

        StringBuilder ratingText = new StringBuilder();
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            @SuppressLint("Range") float rating = cursor.getFloat(cursor.getColumnIndex(COLUMN_RATING));
            @SuppressLint("Range") String review = cursor.getString(cursor.getColumnIndex(COLUMN_REVIEW));
            ratingText.append("______________________________________________").append("\n");
            ratingText.append("Name: ").append(name).append("\n");
            ratingText.append("Rate: ").append(rating).append("\n");
            ratingText.append("Comment: ").append(review).append("\n");
        }
        cursor.close();
        return ratingText.toString();
    }
    //lấy trung bình rate
    public float getAverageRating(String movie) {
        String[] selectionArgs = { movie };
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT AVG(" + COLUMN_RATING + ") FROM " + TABLE_NAME+ " WHERE " + COLUMN_NAMEMOVIE + " = ?", selectionArgs);
        float averageRating = 0;
        if (cursor.moveToFirst()) {
            averageRating = cursor.getFloat(0);
        }
        cursor.close();
        return averageRating;
    }
    //lấy rate count
    public int getallusercomment(String movie){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = { movie };
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME+ " WHERE " + COLUMN_NAMEMOVIE + " = ?", selectionArgs);
        int sumuser = 0;
        if (cursor.moveToFirst()) {
            sumuser = cursor.getInt(0);
        }
        cursor.close();
        return sumuser;
    }

}