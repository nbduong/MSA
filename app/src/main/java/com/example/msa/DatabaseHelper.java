package com.example.msa;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "video.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "video_position";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USER_ID = "iduser";
    private static final String COLUMN_MOVIE_ID = "movie_id";
    private static final String COLUMN_POSITION = "position";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_ID + " INTEGER, " +
                COLUMN_MOVIE_ID + " INTEGER, " +
                COLUMN_POSITION + " INTEGER)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void saveVideoPosition(int userId, int movieId, int position) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_MOVIE_ID, movieId);
        values.put(COLUMN_POSITION, position);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    @SuppressLint("Range")
    public int getVideoPosition(int userId, int movieId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_POSITION + " FROM " + TABLE_NAME +
                " WHERE " + COLUMN_USER_ID + " = " + userId +
                " AND " + COLUMN_MOVIE_ID + " = " + movieId;
        Cursor cursor = db.rawQuery(query, null);
        int position = -1;
        if (cursor.moveToFirst()) {
            position = cursor.getInt(cursor.getColumnIndex(COLUMN_POSITION));
        }
        cursor.close();
        db.close();
        return position;
    }


}
