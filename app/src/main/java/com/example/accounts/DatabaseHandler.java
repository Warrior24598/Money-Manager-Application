package com.example.accounts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.YuvImage;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.accounts.data.Entry;

import java.sql.SQLData;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper
{
    private static final String TAG = "DatabaseHandler";

    private static final String DATABASE = "accounts.db";
    private static final int DATABASE_VERSION = 1;

    //---------Accounts entry table---------//
    private static final String TABLE_ENTRY = "entry";
    private static final String COL_ID = "id";
    private static final String COL_SOURCE = "source";
    private static final String COL_AMOUNT = "amount";
    private static final String COL_DATE = "date";
    private static final String COL_CATEGORY = "category";
    private static final String COL_TYPE = "type";


    //---------Category table---------//
    private static final String TABLE_CATEGORY= "category";
    private static final String COL_NAME = "name";


    private static DatabaseHandler handler;


    public DatabaseHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String createTable = "CREATE TABLE " + TABLE_ENTRY + " ( " +
                COL_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_SOURCE + " TEXT," +
                COL_AMOUNT + " FLOAT ," +
                COL_DATE + " DATE," +
                COL_CATEGORY + " TEXT," +
                COL_TYPE + " TEXT" +
                " );";

        db.execSQL(createTable);

        createTable = "CREATE TABLE " + TABLE_CATEGORY + " ( " +
                COL_NAME + " TEXT," +
                COL_TYPE + " TEXT" +
                " );";

        db.execSQL(createTable);

        Log.e(TAG, "Database Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        onCreate(db);

        Log.e(TAG, "Database Updated");

        addCategory("Regular",Constants.TYPE_INCOME);
        addCategory("Regular",Constants.TYPE_EXPENSE);
    }

    //--------------GET DATABASE HANDLER OBJECT-----------------//
    public static DatabaseHandler getHandler(Context context)
    {
        if(handler==null)
        {
            handler = new DatabaseHandler(context, DATABASE, null, DATABASE_VERSION);
        }

        return handler;
    }


    //-------------------------------------------------------------//
    //--------------METHODS FOR ENTRY TABLE MANIPULATION-----------//
    //-------------------------------------------------------------//

    public void addEntry(Entry e)
    {
        Log.e(TAG,"ENTRY To Be Done: "+e.print());

        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_SOURCE,e.getSource());
        values.put(COL_AMOUNT,e.getAmount());
        values.put(COL_DATE,e.getDate());
        values.put(COL_CATEGORY,e.getCategory());
        values.put(COL_TYPE,e.getType());

        long l = database.insert(TABLE_ENTRY,null,values);

        Log.e(TAG,"INSERT STATUS: "+l);
        database.close();
    }

    public void deleteEntry(int id)
    {
        SQLiteDatabase database = getWritableDatabase();

        String delQuery = "DELETE FROM "+TABLE_ENTRY+" WHERE "+COL_ID+"="+id;
        database.execSQL(delQuery);
        database.close();

        Log.e(TAG,"Entry Deleted: "+id);
    }

    public void updateEntry(Entry e)
    {
        SQLiteDatabase db = getWritableDatabase();

        String updateQ = "UPDATE "+TABLE_ENTRY+" SET "+
                COL_SOURCE+"='"+e.getSource()+"', " +
                COL_AMOUNT+"="+e.getAmount()+", " +
                COL_DATE+"='"+e.getDate()+"', " +
                COL_CATEGORY+"='"+e.getCategory()+"' " +
                "WHERE "+COL_ID+"="+e.getId()+";";

        db.execSQL(updateQ);
        db.close();
    }

    public Entry getEntry(int id)
    {
        SQLiteDatabase db = getReadableDatabase();

        String select = "SELECT * FROM "+TABLE_ENTRY+" WHERE " +
                    COL_ID+"="+id+";";

        Cursor c = db.rawQuery(select,null);
        c.moveToFirst();


        Entry e = new Entry(c.getInt(0),
                c.getString(1),
                c.getFloat(2),
                c.getString(3),
                c.getString(4),
                c.getString(5));

        Log.e(TAG,e.print());

        db.close();

        return e;
    }

    public List getEntries(String date, String category, String type)
    {
        SQLiteDatabase db = getReadableDatabase();

        List<Entry> entryList = new ArrayList<>();

        String select;

        Log.e(TAG,"Entry from Date/Category/Type: "+date+"/"+category+"/"+type);

        if(category.equals(Constants.ALLCATS))
        {
            select = "SELECT * FROM "+TABLE_ENTRY+" WHERE " +
                    "strftime('%d/%m/%Y',"+COL_DATE+")='"+date+"' AND " +
                    COL_TYPE+"='"+type+"';";
        }
        else
        {
            select = "SELECT * FROM "+TABLE_ENTRY+" WHERE " +
                    "strftime('%d/%m/%Y',"+COL_DATE+")='"+date+"' AND " +
                    COL_CATEGORY+"='"+category+"' AND " +
                    COL_TYPE+"='"+type+"';";
        }

        Cursor c = db.rawQuery(select,null);
        c.moveToFirst();

        while(!c.isAfterLast())
        {
            Entry e = new Entry(c.getInt(0),
                                c.getString(1),
                                c.getFloat(2),
                                c.getString(3),
                                c.getString(4),
                                c.getString(5)
                                );

            entryList.add(e);

            Log.e(TAG,e.print());

            c.moveToNext();
        }

        db.close();

        return entryList;
    }

    public List getYears(String category, String type)
    {
        SQLiteDatabase db = getReadableDatabase();

        List<String> years = new ArrayList<>();

        String select;

        Log.e(TAG,"Year From Category/Type: "+category+"/"+type);

        if(type.equals(Constants.ALLTYPE))
        {
            select = "SELECT DISTINCT strftime('%Y',"+COL_DATE+") FROM "+TABLE_ENTRY+";";
        }
        else if(category.equals(Constants.ALLCATS))
        {
            select = "SELECT DISTINCT strftime('%Y',"+COL_DATE+") FROM "+TABLE_ENTRY+" WHERE " +
                    COL_TYPE+"='"+type+"';";
        }
        else
        {
            select = "SELECT DISTINCT strftime('%Y',"+COL_DATE+") FROM "+TABLE_ENTRY+" WHERE " +
                    COL_CATEGORY+"='"+category+"' AND " +
                    COL_TYPE+"='"+type+"';";
        }

        Cursor c = db.rawQuery(select,null);
        c.moveToFirst();

        while(!c.isAfterLast())
        {
            String year = c.getString(0);

            years.add(year);

            Log.e(TAG,"Y:"+year);

            c.moveToNext();
        }

        db.close();

        return years;
    }

    public List getMonths(String year, String category, String type)
    {
        SQLiteDatabase db = getReadableDatabase();

        List<String> months = new ArrayList<>();

        String select;

        Log.e(TAG,"Months From Year/Category/Type: "+year+"/"+category+"/"+type);

        if(type.equals(Constants.ALLTYPE))
        {
            select = "SELECT DISTINCT strftime('%m',"+COL_DATE+") FROM "+TABLE_ENTRY+" WHERE " +
                    "strftime('%Y',"+COL_DATE+")='"+year+"';";
        }
        else if(category.equals(Constants.ALLCATS))
        {
            select = "SELECT DISTINCT strftime('%m',"+COL_DATE+") FROM "+TABLE_ENTRY+" WHERE " +
                    "strftime('%Y',"+COL_DATE+")='"+year+"' AND " +
                    COL_TYPE+"='"+type+"';";
        }
        else
        {
            select = "SELECT DISTINCT strftime('%m',"+COL_DATE+") FROM "+TABLE_ENTRY+" WHERE " +
                    "strftime('%Y',"+COL_DATE+")='"+year+"' AND " +
                    COL_CATEGORY+"='"+category+"' AND " +
                    COL_TYPE+"='"+type+"';";
        }

        Cursor c = db.rawQuery(select,null);
        c.moveToFirst();

        while(!c.isAfterLast())
        {
            String month = c.getString(0);

            months.add(month);

            Log.e(TAG,"m: "+month);

            c.moveToNext();
        }

        db.close();

        return months;
    }

    public List getDays(String monthAndYear, String category, String type)
    {
        SQLiteDatabase db = getReadableDatabase();

        List<String> days = new ArrayList<>();

        String select;

        Log.e(TAG,"Day From Month/Year/Category/Type: "+monthAndYear+"/"+category+"/"+type);

        if(category.equals(Constants.ALLCATS))
        {
            select = "SELECT DISTINCT strftime('%d',"+COL_DATE+") FROM "+TABLE_ENTRY+" WHERE " +
                    "strftime('%m/%Y',"+COL_DATE+")='"+monthAndYear+"' AND " +
                    COL_TYPE+"='"+type+"';";
        }
        else
        {
            select = "SELECT DISTINCT strftime('%d',"+COL_DATE+") FROM "+TABLE_ENTRY+" WHERE " +
                    "strftime('%m/%Y',"+COL_DATE+")='"+monthAndYear+"' AND " +
                    COL_CATEGORY+"='"+category+"' AND " +
                    COL_TYPE+"='"+type+"';";
        }

        Cursor c = db.rawQuery(select,null);
        c.moveToFirst();

        while(!c.isAfterLast())
        {
            String day = c.getString(0);

            days.add(day);

            Log.e(TAG,"D: "+day);

            c.moveToNext();
        }

        db.close();

        return days;
    }

    public float getDayTotal(String date, String category, String type)
    {
        float total;

        SQLiteDatabase db = getReadableDatabase();

        String selectTotal;

        if(category.equals(Constants.ALLCATS))
        {

            selectTotal = "SELECT SUM("+COL_AMOUNT+") FROM "+TABLE_ENTRY+" WHERE " +
                    "strftime('%d/%m/%Y',"+COL_DATE+")='"+date+"' AND " +
                    COL_TYPE+"='"+type+"';";
        }
        else
        {

            selectTotal = "SELECT SUM("+COL_AMOUNT+") FROM "+TABLE_ENTRY+" WHERE " +
                    "strftime('%d/%m/%Y',"+COL_DATE+")='"+date+"' AND " +
                    COL_CATEGORY+"='"+category+"' AND " +
                    COL_TYPE+"='"+type+"';";
        }

        Cursor c = db.rawQuery(selectTotal,null);
        c.moveToFirst();

        total = c.getFloat(0);
        db.close();

        return total;
    }

    public float getMonthTotal(String monthAndYear, String category, String type)
    {
        float total;

        SQLiteDatabase db = getReadableDatabase();

        String selectTotal;

        if(category.equals(Constants.ALLCATS))
        {
            selectTotal = "SELECT SUM("+COL_AMOUNT+") FROM "+TABLE_ENTRY+" WHERE " +
                    "strftime('%m/%Y',"+COL_DATE+")='"+monthAndYear+"' AND " +
                    COL_TYPE+"='"+type+"';";
        }
        else
        {

            selectTotal = "SELECT SUM("+COL_AMOUNT+") FROM "+TABLE_ENTRY+" WHERE " +
                    "strftime('%m/%Y',"+COL_DATE+")='"+monthAndYear+"' AND " +
                    COL_CATEGORY+"='"+category+"' AND " +
                    COL_TYPE+"='"+type+"';";

        }

        Cursor c = db.rawQuery(selectTotal,null);
        c.moveToFirst();

        total = c.getFloat(0);
        db.close();

        return total;
    }

    public float getYearTotal(String year, String category, String type)
    {
        float total;

        SQLiteDatabase db = getReadableDatabase();

        String selectTotal;

        if(category.equals(Constants.ALLCATS))
        {

            selectTotal = "SELECT SUM("+COL_AMOUNT+") FROM "+TABLE_ENTRY+" WHERE " +
                    "strftime('%Y',"+COL_DATE+")='"+year+"' AND " +
                    COL_TYPE+"='"+type+"';";
        }
        else
        {

            selectTotal = "SELECT SUM("+COL_AMOUNT+") FROM "+TABLE_ENTRY+" WHERE " +
                    "strftime('%Y',"+COL_DATE+")='"+year+"' AND " +
                    COL_CATEGORY+"='"+category+"' AND " +
                    COL_TYPE+"='"+type+"';";
        }

        Cursor c = db.rawQuery(selectTotal,null);
        c.moveToFirst();

        total = c.getFloat(0);
        db.close();

        return total;
    }

    public float getGrandTotal(String category, String type)
    {
        float total;

        SQLiteDatabase db = getReadableDatabase();

        String selectTotal;

        if(category.equals(Constants.ALLCATS))
        {

            selectTotal = "SELECT SUM("+COL_AMOUNT+") FROM "+TABLE_ENTRY+" WHERE " +
                    COL_TYPE+"='"+type+"';";
        }
        else
        {

            selectTotal = "SELECT SUM("+COL_AMOUNT+") FROM "+TABLE_ENTRY+" WHERE " +
                    COL_CATEGORY+"='"+category+"' AND " +
                    COL_TYPE+"='"+type+"';";
        }

        Cursor c = db.rawQuery(selectTotal,null);
        c.moveToFirst();

        total = c.getFloat(0);
        db.close();


        return total;
    }


    //----------------------------------------------------------------//
    //-----------METHODS FOR MANIPULATION OF CATEGORY TABLE-----------//
    //----------------------------------------------------------------//

    public void addCategory(String name, String type)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_NAME,name);
        values.put(COL_TYPE,type);

        db.insert(TABLE_CATEGORY,null,values);
        db.close();

        Log.e(TAG,"CATEGORY ADDED: \n"+name+"\n"+type+")");
    }

    public void deleteCategory(String name, String type)
    {
        SQLiteDatabase db = getWritableDatabase();


        String delQuery = "DELETE FROM "+TABLE_ENTRY+" WHERE "+
                COL_CATEGORY+"='"+name+"' AND " +
                COL_TYPE+"='"+type+"';";

        db.execSQL(delQuery);

        Log.e(TAG,"ALL ENTRIES DELETED: CATEGORY/TYPE: "+name+"/"+type);

        String delQ = "DELETE FROM "+TABLE_CATEGORY+" WHERE " +
                COL_NAME+"='"+name+"' AND " +
                COL_TYPE+"='"+type+"';";

        db.execSQL(delQ);

        Log.e(TAG,"CATEGORY DELETED: CATEGORY/TYPE: "+name+"/"+type);
        db.close();
    }

    public List getCategories(String type)
    {
        List<String> categories = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String getQ = "SELECT "+COL_NAME+" FROM "+TABLE_CATEGORY+" WHERE " +
                COL_TYPE+"='"+type+"';";

        Cursor c = db.rawQuery(getQ,null);
        c.moveToFirst();

        while(!c.isAfterLast())
        {
            categories.add(c.getString(0));
            c.moveToNext();
        }

        db.close();

        if(categories.size()==0)
        {
            addCategory("Regular",type);

            categories.add("Regular");
        }

        return categories;
    }


    //------------------------------//
    //------PRINT TABLES-------//
    //------------------------------//

    public List<Entry> getAllEntries()
    {
        List<Entry> entryList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String select = "SELECT * FROM "+TABLE_ENTRY+";";

        Cursor c = db.rawQuery(select,null);
        c.moveToFirst();


        while(!c.isAfterLast())
        {
            Entry e = new Entry(c.getInt(0),
                    c.getString(1),
                    c.getFloat(2),
                    c.getString(3),
                    c.getString(4),
                    c.getString(5)
            );

            entryList.add(e);

            Log.e(TAG,e.print());

            c.moveToNext();
        }

        db.close();

        return entryList;
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

        addCategory("Regular",Constants.TYPE_INCOME);
        addCategory("Regular",Constants.TYPE_EXPENSE);
    }

}
