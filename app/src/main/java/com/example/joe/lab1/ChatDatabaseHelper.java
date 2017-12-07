package com.example.joe.lab1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class ChatDatabaseHelper extends SQLiteOpenHelper {

    //issue here - what context?

    public static final String KEY_ID = "KEY_ID";
    static final String TABLE_NAME = "MESSAGES";
    static final String KEY_MESSAGE = "KEY_MESSAGE";
    private static final String DATABASE_NAME = "Messages.db";
    private static final int VERSION_NUM = 1;


    ChatDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("ChatDatabaseHelper", "Calling onCreate");
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_MESSAGE + " TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        Log.i("ChatDatabaseHelper", "Calling onUpgrade. Old Version: " + oldVer + ", New Version: " + newVer);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVer, int newVer) {
        Log.i("ChatDatabaseHelper", "Calling onUpgrade. Old Version: " + oldVer + ", New Version: " + newVer);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

}
