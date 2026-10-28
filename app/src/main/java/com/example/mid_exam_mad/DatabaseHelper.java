package com.example.mid_exam_mad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "StudentDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "students";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                "student_id TEXT PRIMARY KEY, " +
                "student_name TEXT NOT NULL, " +
                "major TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addStudent(String id, String name, String major) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("student_id", id);
        values.put("student_name", name);
        values.put("major", major);

        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    public boolean updateStudent(String id, String name, String major) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("student_name", name);
        values.put("major", major);

        int result = db.update(TABLE_NAME, values,
                "student_id = ?", new String[]{id});

        return result > 0;
    }

    public boolean deleteStudent(String id) {
        SQLiteDatabase db = getWritableDatabase();

        int result = db.delete(TABLE_NAME,
                "student_id = ?", new String[]{id});

        return result > 0;
    }

    public Cursor getStudent(String id) {
        SQLiteDatabase db = getReadableDatabase();

        return db.query(TABLE_NAME,
                new String[]{"student_id", "student_name", "major"},
                "student_id = ?", new String[]{id},
                null, null, null);
    }
}
