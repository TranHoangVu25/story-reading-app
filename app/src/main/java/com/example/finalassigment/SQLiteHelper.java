package com.example.finalassigment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class MyDatabaseHelper extends SQLiteOpenHelper {

    // Tên database và phiên bản
    private static final String DATABASE_NAME = "MyAppDB.db";
    private static final int DATABASE_VERSION = 1;

    // Câu lệnh tạo bảng
    private static final String CREATE_USER_TABLE =
            "CREATE TABLE users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "email TEXT NOT NULL, " +
                    "password TEXT NOT NULL, " +
                    "username TEXT)";
//    private static final String CREATE_STORIES_TABLE =
//            "CREATE TABLE stories ("+
//                    "id INTEGER PRIMARY KEY AUTOINRECEMENT,"+
//                    "title "

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Tạo bảng lần đầu
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }

    // Nâng cấp schema (nếu cần)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xoá bảng cũ nếu có
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }
}
