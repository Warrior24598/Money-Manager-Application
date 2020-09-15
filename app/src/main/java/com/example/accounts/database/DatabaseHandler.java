package com.example.accounts.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.accounts.Constants;
import com.example.accounts.models.Entry;
import com.example.accounts.models.EntryType;

import java.util.ArrayList;
import java.util.List;

import static com.example.accounts.database.DatabaseConfig.*;

public class DatabaseHandler extends SQLiteOpenHelper
{
    private static final String TAG = "DatabaseHandler";

    public DatabaseHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String createTableEntry = "CREATE TABLE " + TABLE_ENTRY + " ( " +
                COL_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_SOURCE + " TEXT," +
                COL_AMOUNT + " FLOAT ," +
                COL_DATE + " DATE," +
                COL_CATEGORY_ID + " INTEGER," +
                "FOREIGN KEY (" + COL_CATEGORY_ID + ") REFERENCES "+ TABLE_CATEGORY + "( "+COL_ID+" ) "+
                " );";


        String createTableCategory = "CREATE TABLE " + TABLE_CATEGORY + " ( " +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_NAME + " TEXT," +
                COL_TYPE_ID + " INTEGER," +
                "FOREIGN KEY (" + COL_TYPE_ID + ") REFERENCES "+ TABLE_TYPE + "( "+COL_ID+" ) "+
                " );";


        String createTableType = "CREATE TABLE " + TABLE_TYPE + " ( " +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_NAME + " TEXT" +
                " );";

        String createTableConfig = "CREATE TABLE " + TABLE_CONFIG + " ( " +
                COL_EXPENSE_CATEGORY_ID + " INTEGER PRIMARY KEY," +
                COL_LIMIT + " FLOAT," +
                "FOREIGN KEY (" + COL_EXPENSE_CATEGORY_ID + ") REFERENCES "+ TABLE_CATEGORY + "( "+COL_ID+" ) "+
                " );";

        db.execSQL(createTableType);
        db.execSQL(createTableCategory);
        db.execSQL(createTableEntry);
        db.execSQL(createTableConfig);

        Log.e(TAG, "Database Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TYPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONFIG);
        onCreate(db);

        Log.e(TAG, "Database Updated");

        addType(0, EntryType.EXPENSE);
        addType(1, EntryType.INCOME);
    }

    //--------------GET DATABASE HANDLER OBJECT-----------------//
    public static DatabaseHandler getHandler(Context context)
    {
        return  new DatabaseHandler(context, DATABASE, null, DATABASE_VERSION);
    }

    public void addType(int id, EntryType type)
    {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_NAME,type.toString());
        values.put(COL_ID,type.id);

        database.insert(TABLE_CATEGORY,null,values);
        database.close();

        Log.e(TAG,"ENTRY TYPE ADDED: "+type.toString()+")");
    }

    //------DELETE BOTH TABLES-----//
    public void deleteAll()
    {
        SQLiteDatabase db = getWritableDatabase();

        String q = "DELETE FROM "+TABLE_ENTRY+" WHERE 1";

        db.execSQL(q);


        q = "DELETE FROM "+TABLE_CATEGORY+" WHERE 1";

        db.execSQL(q);

        db.close();

    }

}
