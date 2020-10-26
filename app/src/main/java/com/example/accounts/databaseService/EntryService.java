package com.example.accounts.databaseService;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.accounts.Constants;
import com.example.accounts.models.Category;
import com.example.accounts.models.Entry;
import com.example.accounts.models.EntryType;

import java.util.ArrayList;
import java.util.List;

import static com.example.accounts.database.DatabaseConfig.COL_AMOUNT;
import static com.example.accounts.database.DatabaseConfig.COL_CATEGORY_ID;
import static com.example.accounts.database.DatabaseConfig.COL_DATE;
import static com.example.accounts.database.DatabaseConfig.COL_ID;
import static com.example.accounts.database.DatabaseConfig.COL_NAME;
import static com.example.accounts.database.DatabaseConfig.COL_SOURCE;
import static com.example.accounts.database.DatabaseConfig.COL_TYPE_ID;
import static com.example.accounts.database.DatabaseConfig.TABLE_CATEGORY;
import static com.example.accounts.database.DatabaseConfig.TABLE_ENTRY;
import static com.example.accounts.database.DatabaseConfig.TABLE_TYPE;

public class EntryService implements IEntryService
{
    private static final String TAG = "EntryService";
    private SQLiteOpenHelper helper;

    public EntryService(SQLiteOpenHelper helper)
    {
        this.helper = helper;
    }

    @Override
    public void addEntry(Entry e)
    {
        SQLiteDatabase database = helper.getWritableDatabase();
        Log.e(TAG,"ENTRY To Be Done: "+e.toString());

        ContentValues values = new ContentValues();

        values.put(COL_SOURCE,e.getSource());
        values.put(COL_AMOUNT,e.getAmount());
        values.put(COL_DATE,e.getDate());
        values.put(COL_CATEGORY_ID,e.getCategory().getId());

        long l = database.insert(TABLE_ENTRY,null,values);

        Log.e(TAG,"INSERT STATUS: "+l);
        database.close();
    }

    @Override
    public void deleteEntry(int id)
    {
        SQLiteDatabase database = helper.getWritableDatabase();

        String deleteQuery = "DELETE FROM "+TABLE_ENTRY+" WHERE "+COL_ID+"="+id;

        Log.e(TAG,deleteQuery);

        database.execSQL(deleteQuery);
        database.close();

        Log.e(TAG,"Entry Deleted: "+id);
    }

    @Override
    public void updateEntry(Entry e)
    {
        SQLiteDatabase db = helper.getWritableDatabase();

        String updateEntryQuery = "UPDATE "+TABLE_ENTRY+" SET "+
                COL_SOURCE+"='"+e.getSource()+"', " +
                COL_AMOUNT+"="+e.getAmount()+", " +
                COL_DATE+"='"+e.getDate()+"'," +
                COL_CATEGORY_ID +"="+e.getCategory().getId()+" " +
                "WHERE "+COL_ID+"="+e.getId()+";";

        Log.e(TAG,updateEntryQuery);

        db.execSQL(updateEntryQuery);
        db.close();

        Log.e(TAG,"Entry Updated: "+e.toString());
    }

    @Override
    public Entry getEntry(int id)
    {
        SQLiteDatabase db = helper.getReadableDatabase();

        String select = "SELECT "+
                "e."+COL_ID+" as id,"+
                "e."+COL_SOURCE+" as source,"+
                "e."+COL_AMOUNT+" as amount,"+
                "strftime('%d/%m/%Y',e."+COL_DATE+") as date,"+
                "e."+COL_CATEGORY_ID+" as categoryId,"+
                "c."+COL_TYPE_ID+" as typeId,"+
                "c."+COL_NAME+" as category"
                +" FROM "+TABLE_ENTRY+" as e, "+TABLE_CATEGORY+" as c WHERE "+
                " c."+COL_ID+"=e."+COL_CATEGORY_ID+
                " AND e."+COL_ID+"="+id;

        Log.e(TAG,select);

        Cursor c = db.rawQuery(select,null);

        if(false==c.moveToFirst())
        {
            Log.e(TAG,"No Data Found");
            db.close();
            return null;
        }

        Entry e = createEntryObject(c);


        Log.e(TAG,"Fetched Entry: "+e.toString());

        db.close();

        return e;
    }

    @Override
    public List getEntries()
    {
        Log.e(TAG,"Fetching ALL Entries");

        String getQuery = "SELECT "+
                "e."+COL_ID+" as id,"+
                "e."+COL_SOURCE+" as source,"+
                "e."+COL_AMOUNT+" as amount,"+
                "strftime('%d/%m/%Y',e."+COL_DATE+") as date,"+
                "e."+COL_CATEGORY_ID+" as categoryId,"+
                "c."+COL_TYPE_ID+" as typeId,"+
                "c."+COL_NAME+" as category"
                +" FROM "+TABLE_ENTRY+" as e, "+TABLE_CATEGORY+" as c WHERE "+
                " c."+COL_ID+"=e."+COL_CATEGORY_ID;

        return getEntriesFromQuery(getQuery);
    }

    @Override
    public List getEntries(String date, EntryType type)
    {
        Log.e(TAG,"Fetching Entry for date: "+date+" | Type: "+type.toString());

        String getQuery = "SELECT "+
                "e."+COL_ID+" as id"+
                ",e."+COL_SOURCE+" as source"+
                ",e."+COL_AMOUNT+" as amount"+
                ",strftime('%d/%m/%Y',e."+COL_DATE+") as date"+
                ",e."+COL_CATEGORY_ID+" as categoryId"+
                ",c."+COL_TYPE_ID+" as typeId"+
                ",c."+COL_NAME+" as category"
                +" FROM "+TABLE_ENTRY+" as e, "+TABLE_CATEGORY+" as c WHERE "+
                " c."+COL_ID+"=e."+COL_CATEGORY_ID+
                " AND strftime('%d/%m/%Y',e."+COL_DATE+")='"+date+"'"+
                " AND c."+COL_TYPE_ID+"="+type.id;

        return getEntriesFromQuery(getQuery);
    }

    @Override
    public List getEntries(String date, Category category, EntryType type)
    {
        if(category==null)
        {
            return getEntries(date, type);
        }
        Log.e(TAG,"Fetching Entry for date: "+date+" | Type: "+type.toString()+" | Category: "+category.getName());

        String getQuery = "SELECT "+
                "e."+COL_ID+" as id"+
                ",e."+COL_SOURCE+" as source"+
                ",e."+COL_AMOUNT+" as amount"+
                ", strftime('%d/%m/%Y',e."+COL_DATE+") as date"+
                ",e."+COL_CATEGORY_ID+" as categoryId"+
                ",c."+COL_TYPE_ID+" as typeId"+
                ",c."+COL_NAME+" as category"
                +" FROM "+TABLE_ENTRY+" as e, "+TABLE_CATEGORY+" as c WHERE "+
                " c."+COL_ID+"=e."+COL_CATEGORY_ID+
                " AND strftime('%d/%m/%Y',e."+COL_DATE+")='"+date+"'"+
                " AND c."+COL_TYPE_ID+"="+type.id+
                " AND e."+COL_CATEGORY_ID+"="+category.getId();

        return getEntriesFromQuery(getQuery);
    }

    @Override
    public List searchEntries(String searchQuery, String date,Category category, EntryType type)
    {
        Log.e(TAG,"Fetching Entry for search: "+searchQuery+" | Category: "+category+" | Date: "+date);

        String getQuery = "SELECT "+
                "e."+COL_ID+" as id"+
                ",e."+COL_SOURCE+" as source"+
                ",e."+COL_AMOUNT+" as amount"+
                ",strftime('%d/%m/%Y',e."+COL_DATE+") as date"+
                ",e."+COL_CATEGORY_ID+" as categoryId"+
                ",c."+COL_TYPE_ID+" as typeId"+
                ",c."+COL_NAME+" as category"
                +" FROM "+TABLE_ENTRY+" as e, "+TABLE_CATEGORY+" as c WHERE "+
                " c."+COL_ID+"=e."+COL_CATEGORY_ID+
                " AND c."+COL_TYPE_ID+"="+type.id+
                " AND e."+COL_SOURCE+" LIKE '"+searchQuery+"%'";

        if(category!=null)
        {
            getQuery+=" AND c."+COL_ID+"="+category.getId();
        }
        if(date!=null) //search in months
        {
            getQuery+=" AND strftime('%m/%Y',e."+COL_DATE+")='"+date+"'";
        }

        return getEntriesFromQuery(getQuery);
    }

    @Override
    public List getRecentEntries(int numberOfEntries)
    {
        Log.e(TAG,"Fetching "+numberOfEntries+" Recent Entries");

        String getQuery = "SELECT "+
                "e."+COL_ID+" as id,"+
                "e."+COL_SOURCE+" as source,"+
                "e."+COL_AMOUNT+" as amount,"+
                "strftime('%d/%m/%Y',e."+COL_DATE+") as date,"+
                "e."+COL_CATEGORY_ID+" as categoryId,"+
                "c."+COL_TYPE_ID+" as typeId,"+
                "c."+COL_NAME+" as category"
                +" FROM "+TABLE_ENTRY+" as e, "+TABLE_CATEGORY+" as c  WHERE "+
                " c."+COL_ID+"=e."+COL_CATEGORY_ID+
                " order by id desc";

        return getEntriesFromQuery(getQuery);
    }

    @Override
    public List getYears(Category category, EntryType type)
    {
        if(category==null)
        {
            return getYears(type);
        }
        Log.e(TAG,"Fetching Years: Type: "+type.toString()+" | Category: "+category.getName());

        String query = "SELECT"+
                " DISTINCT strftime('%Y',"+COL_DATE+") as date"+
                " FROM "+TABLE_ENTRY+" WHERE "+
                COL_CATEGORY_ID+"="+category.getId()+" order by date";

        return getDateFormatFromQuery(query);
    }

    @Override
    public List getYears(EntryType type)
    {
        Log.e(TAG,"Fetching Years: Type: "+type.toString());

        String query = "SELECT"+
                " DISTINCT strftime('%Y',e."+COL_DATE+") as date"+
                " FROM "+TABLE_ENTRY+" as e, "+TABLE_CATEGORY+" as c WHERE "+
                " c."+COL_ID+"=e."+COL_CATEGORY_ID+
                " AND c."+COL_TYPE_ID+"="+type.id+" order by date";

        return getDateFormatFromQuery(query);
    }

    @Override
    public List getYears()
    {
        Log.e(TAG,"Fetching all Years");

        String query = "SELECT "+
                " DISTINCT strftime('%Y',e."+COL_DATE+") as date"+
                " FROM "+TABLE_ENTRY+" as e, "+TABLE_CATEGORY+" as c WHERE "+
                " c."+COL_ID+"=e."+COL_CATEGORY_ID+" order by date";

        return getDateFormatFromQuery(query);
    }

    @Override
    public List getMonths(String year, Category category, EntryType type)
    {
        if(category==null)
        {
            return getMonths(year,type);
        }
        Log.e(TAG,"Fetching Months: Type: "+type.toString()+" | Year: "+year+" | Category: "+category.getName());

        String query = "SELECT "+
                " DISTINCT strftime('%m',e."+COL_DATE+")"+
                " FROM "+TABLE_ENTRY+" as e, "+TABLE_CATEGORY+" as c WHERE "+
                " c."+COL_ID+"=e."+COL_CATEGORY_ID+
                " AND c."+COL_ID+"="+category.getId()+
                " AND strftime('%Y',e."+COL_DATE+")='"+year+"'";

        return getDateFormatFromQuery(query);
    }

    @Override
    public List getMonths(String year, EntryType type)
    {
        Log.e(TAG,"Fetching Months: Type: "+type.toString()+" | Year: "+year);

        String query = "SELECT "+
                " DISTINCT strftime('%m',e."+COL_DATE+")"+
                " FROM "+TABLE_ENTRY+" as e, "+TABLE_CATEGORY+" as c WHERE "+
                " c."+COL_ID+"=e."+COL_CATEGORY_ID+
                " AND c."+COL_TYPE_ID+"="+type.id+
                " AND strftime('%Y',e."+COL_DATE+")='"+year+"'";

        return getDateFormatFromQuery(query);
    }

    @Override
    public List getMonths(String year)
    {
        Log.e(TAG,"Fetching All Months: Year: "+year);

        String query = "SELECT "+
                " DISTINCT strftime('%m',e."+COL_DATE+")"+
                " FROM "+TABLE_ENTRY+" as e, "+TABLE_CATEGORY+" as c WHERE "+
                " c."+COL_ID+"=e."+COL_CATEGORY_ID+
                " AND strftime('%Y',e."+COL_DATE+")='"+year+"'";

        return getDateFormatFromQuery(query);
    }

    @Override
    public List getDays(String monthAndYear, Category category, EntryType type)
    {
        if(category==null)
        {
            return getDays(monthAndYear, type);
        }
        Log.e(TAG,"Fetching Days: Type: "+type.toString()+" | Month/Year: "+monthAndYear+" | Category: "+category.getName());

        String query = "SELECT "+
                " DISTINCT strftime('%d/%m/%Y',"+COL_DATE+")"+
                " FROM "+TABLE_ENTRY+" WHERE "+
                " strftime('%m/%Y',"+COL_DATE+")='"+monthAndYear+"' AND "
                +COL_CATEGORY_ID+"="+category.getId();

        return getDateFormatFromQuery(query);
    }

    @Override
    public List getDays(String monthAndYear, EntryType type)
    {
        Log.e(TAG,"Fetching Days: Type: "+type.toString()+" | Month/Year: "+monthAndYear);

        String query = "SELECT "+
                " DISTINCT strftime('%d/%m/%Y',e."+COL_DATE+")"+
                " FROM "+TABLE_ENTRY+" as e, "+TABLE_CATEGORY+" as c WHERE "+
                " c."+COL_ID+"=e."+COL_CATEGORY_ID+
                " AND c."+COL_TYPE_ID+"="+type.id+
                " AND strftime('%m/%Y',e."+COL_DATE+")='"+monthAndYear+"'";

        return getDateFormatFromQuery(query);
    }

    @Override
    public List getDays(String monthAndYear)
    {
        Log.e(TAG,"Fetching All Days: Type: Month/Year: "+monthAndYear);

        String query = "SELECT "+
                " DISTINCT strftime('%d/%m/%Y',e."+COL_DATE+")"+
                " FROM "+TABLE_ENTRY+" as e, "+TABLE_CATEGORY+" as c WHERE "+
                " c."+COL_ID+"=e."+COL_CATEGORY_ID+
                " AND strftime('%m/%Y',e."+COL_DATE+")='"+monthAndYear+"'";

        return getDateFormatFromQuery(query);
    }

    @Override
    public float getYearTotal(String year, Category category, EntryType type)
    {
        if(category==null)
        {
            return getYearTotal(year, type);
        }
        Log.e(TAG,"Fetching Total of Year: Type: "+type.toString()+" | Year: "+year+" | Category: "+category.getName());

        String query = "SELECT "+
                " SUM(e."+COL_AMOUNT+")"+
                " FROM "+TABLE_ENTRY+" as e WHERE "+
                " e."+COL_CATEGORY_ID+"="+category.getId()+
                " AND strftime('%Y',e."+COL_DATE+")='"+year+"'";

        return getTotalFromQuery(query);
    }

    @Override
    public float getYearTotal(String year, EntryType type)
    {
        Log.e(TAG,"Fetching Total of Year: Type: "+type.toString()+" | Year: "+year);

        String query = "SELECT "+
                " SUM(e."+COL_AMOUNT+")"+
                " FROM "+TABLE_ENTRY+" as e, "+TABLE_CATEGORY+" as c WHERE "+
                " c."+COL_ID+"=e."+COL_CATEGORY_ID+
                " AND c."+COL_TYPE_ID+"="+type.id+
                " AND strftime('%Y',e."+COL_DATE+")='"+year+"'";

        return getTotalFromQuery(query);
    }

    @Override
    public float getMonthTotal(String monthAndYear, Category category, EntryType type)
    {
        if(category==null)
        {
            return getMonthTotal(monthAndYear, type);
        }
        Log.e(TAG,"Fetching Total of Month: Type: "+type.toString()+" | Month/Year: "+monthAndYear+" | Category: "+category.getName());

        String query = "SELECT "+
                " SUM(e."+COL_AMOUNT+")"+
                " FROM "+TABLE_ENTRY+" as e, "+TABLE_CATEGORY+" as c WHERE "+
                " c."+COL_ID+"=e."+COL_CATEGORY_ID+
                " AND c."+COL_ID+"="+category.getId()+
                " AND strftime('%m/%Y',e."+COL_DATE+")='"+monthAndYear+"'";

        return getTotalFromQuery(query);
    }

    @Override
    public float getMonthTotal(String monthAndYear, EntryType type)
    {
        Log.e(TAG,"Fetching Total of Month: Type: "+type.toString()+" | Month/Year: "+monthAndYear);

        String query = "SELECT "+
                " SUM(e."+COL_AMOUNT+")"+
                " FROM "+TABLE_ENTRY+" as e, "+TABLE_CATEGORY+" as c WHERE "+
                " c."+COL_ID+"=e."+COL_CATEGORY_ID+
                " AND c."+COL_TYPE_ID+"="+type.id+
                " AND strftime('%m/%Y',e."+COL_DATE+")='"+monthAndYear+"'";

        return getTotalFromQuery(query);
    }

    @Override
    public float getDateTotal(String date, Category category, EntryType type)
    {
        if(category==null)
        {
            return getDateTotal(date, type);
        }
        Log.e(TAG,"Fetching Total of Date: Type: "+type.toString()+" | Date: "+date+" | Category: "+category.getName());

        String query = "SELECT "+
                " SUM(e."+COL_AMOUNT+")"+
                " FROM "+TABLE_ENTRY+" as e, "+TABLE_CATEGORY+" as c WHERE "+
                " c."+COL_ID+"=e."+COL_CATEGORY_ID+
                " AND c."+COL_ID+"="+category.getId()+
                " AND strftime('%d/%m/%Y',e."+COL_DATE+")='"+date+"'";

        return getTotalFromQuery(query);
    }

    @Override
    public float getDateTotal(String date, EntryType type)
    {
        Log.e(TAG,"Fetching Total of Date: Type: "+type.toString()+" | Date: "+date);

        String query = "SELECT "+
                " SUM(e."+COL_AMOUNT+")"+
                " FROM "+TABLE_ENTRY+" as e, "+TABLE_CATEGORY+" as c WHERE "+
                " c."+COL_ID+"=e."+COL_CATEGORY_ID+
                " AND c."+COL_TYPE_ID+"="+type.id+
                " AND strftime('%d/%m/%Y',e."+COL_DATE+")='"+date+"'";

        return getTotalFromQuery(query);
    }

    @Override
    public float getGrandTotal(Category category, EntryType type)
    {
        if(category==null)
        {
            return getGrandTotal(type);
        }
        Log.e(TAG,"Fetching Grand Total : Type: "+type.toString()+" | Category: "+category.getName());

        String query = "SELECT "+
                " SUM(e."+COL_AMOUNT+")"+
                " FROM "+TABLE_ENTRY+" as e, "+TABLE_CATEGORY+" as c WHERE "+
                " c."+COL_ID+"=e."+COL_CATEGORY_ID+
                " AND c."+COL_TYPE_ID+"="+type.id+
                " AND c."+COL_ID+"="+category.getId();

        return getTotalFromQuery(query);
    }

    @Override
    public float getGrandTotal(EntryType type)
    {
        Log.e(TAG,"Fetching Grand Total : Type: "+type.toString());

        String query = "SELECT "+
                " SUM(e."+COL_AMOUNT+")"+
                " FROM "+TABLE_ENTRY+" as e, "+TABLE_CATEGORY+" as c WHERE "+
                " c."+COL_ID+"=e."+COL_CATEGORY_ID+
                " AND c."+COL_TYPE_ID+"="+type.id;

        return getTotalFromQuery(query);
    }

    private float getTotalFromQuery(String query)
    {
        Log.e(TAG,query);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor c = database.rawQuery(query,null);
        c.moveToFirst();

        float total = c.getFloat(0);
        database.close();

        Log.e(TAG,"Total: "+total);

        return total;
    }

    private List getDateFormatFromQuery(String query)
    {
        Log.e(TAG,query);
        List<String> dateFormat = new ArrayList<>();
        SQLiteDatabase database = helper.getReadableDatabase();

        Cursor c = database.rawQuery(query,null);
        c.moveToFirst();

        while(!c.isAfterLast())
        {
            String year = c.getString(0);

            dateFormat.add(year);

            Log.e(TAG,"Data:"+year);

            c.moveToNext();
        }

        Log.e(TAG,dateFormat.toString());
        database.close();

        return dateFormat;
    }

    private List<Entry> getEntriesFromQuery(String getQuery)
    {
        Log.e(TAG,"Executing Query: "+getQuery);
        List<Entry> entries = new ArrayList<>();
        SQLiteDatabase database = helper.getReadableDatabase();

        Cursor c = database.rawQuery(getQuery,null);
        c.moveToFirst();

        while(!c.isAfterLast())
        {
            Entry e = createEntryObject(c);

            entries.add(e);

            c.moveToNext();
        }

        Log.e(TAG,"List of Entries: "+entries.size());
        database.close();

        return entries;
    }

    private Entry createEntryObject(Cursor c)
    {
        Category category = new Category();
        category.setId(c.getInt(c.getColumnIndex("categoryId")));
        category.setName(c.getString(c.getColumnIndex("category")));
        category.setType(EntryType.find(c.getInt(c.getColumnIndex("typeId"))));

        Entry e = new Entry();

        e.setId(c.getInt(c.getColumnIndex("id")));
        e.setAmount(c.getFloat(c.getColumnIndex("amount")));
        e.setSource(c.getString(c.getColumnIndex("source")));
        e.setDate(c.getString(c.getColumnIndex("date")));
        e.setCategory(category);

        return e;
    }
}
