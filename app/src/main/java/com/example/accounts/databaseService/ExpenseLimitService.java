package com.example.accounts.databaseService;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.accounts.models.Category;
import com.example.accounts.models.EntryType;
import com.example.accounts.models.ExpenseConfig;

import java.util.ArrayList;
import java.util.List;

import static com.example.accounts.database.DatabaseConfig.*;

public class ExpenseLimitService implements IExpenseLimitService
{
    private static final String TAG = "ExpenseLimitService";
    SQLiteOpenHelper helper;

    public ExpenseLimitService(SQLiteOpenHelper helper)
    {
        this.helper = helper;
    }

    @Override
    public void addExpenseLimit(ExpenseConfig expenseConfig)
    {
        Log.e(TAG,"ExpenseLimit to be added: "+expenseConfig.toString());
        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_EXPENSE_CATEGORY_ID,expenseConfig.getCategory().getId());
        values.put(COL_LIMIT, expenseConfig.getLimit());

        long result = database.insert(TABLE_CONFIG,null,values);

        Log.e(TAG,"Result of insert: "+result);
    }

    @Override
    public void updateExpenseLimit(ExpenseConfig expenseConfig)
    {
        Log.e(TAG,"ExpenseLimit to be updated: "+expenseConfig.toString());
        SQLiteDatabase database = helper.getWritableDatabase();

        String updateQuery = "UPDATE "+TABLE_CONFIG+" SET "+
                                COL_LIMIT+"="+expenseConfig.getLimit()+
                                " WHERE "+
                                COL_EXPENSE_CATEGORY_ID+"="+expenseConfig.getCategory().getId();

        Log.e(TAG,updateQuery);

        database.execSQL(updateQuery);
        Log.e(TAG,"ExpenseLimit Updated");
        database.close();
    }

    @Override
    public void deleteExpenseLimit(ExpenseConfig expenseConfig)
    {
        Log.e(TAG,"ExpenseLimit to be deleted: "+expenseConfig.toString());
        SQLiteDatabase database = helper.getWritableDatabase();

        String deleteQuery = "DELETE FROM "+TABLE_CONFIG+
                " WHERE "+
                COL_EXPENSE_CATEGORY_ID+"="+expenseConfig.getCategory().getId();

        Log.e(TAG,deleteQuery);

        database.execSQL(deleteQuery);
        Log.e(TAG,"ExpenseLimit deleted");
        database.close();
    }

    @Override
    public ExpenseConfig getExpenseLimit(Category c)
    {
        if(null==c)
        {
            return null;
        }
        Log.e(TAG,"Getting ExpeseLimit: "+c.toString());
        SQLiteDatabase database = helper.getWritableDatabase();

        String getQuery = "SELECT "+COL_LIMIT+" FROM "+TABLE_CONFIG+" WHERE "+COL_EXPENSE_CATEGORY_ID+"="+c.getId();

        Log.e(TAG,getQuery);

        Cursor cursor = database.rawQuery(getQuery,null);

        if(cursor.moveToFirst()==false)
        {
            Log.e(TAG,"ExpenseLimit Not Found");
            database.close();
            return null;
        }

        ExpenseConfig config = new ExpenseConfig();

        config.setCategory(c);
        config.setLimit(cursor.getFloat(0));
        Log.e(TAG,"ExpenseLimit Found"+ config.toString());

        return config;
    }

    @Override
    public List<ExpenseConfig> getExpenseLimit()
    {
        Log.e(TAG,"Getting All ExpeseLimits: ");

        List<ExpenseConfig> expenseConfigs = new ArrayList<>();

        SQLiteDatabase database = helper.getWritableDatabase();

        String getQuery = "SELECT e."+COL_EXPENSE_CATEGORY_ID+" as id, c."+COL_NAME+" as name, e."+COL_LIMIT+" as `limit` FROM "
                +TABLE_CONFIG+" as e, "+TABLE_CATEGORY+" as c WHERE "+
                "e."+COL_EXPENSE_CATEGORY_ID+"=c."+COL_ID;

        Log.e(TAG,getQuery);

        Cursor cursor = database.rawQuery(getQuery,null);

        while (cursor.moveToNext())
        {
            ExpenseConfig config = new ExpenseConfig();

            Category c = new Category();
            c.setId(cursor.getInt(cursor.getColumnIndex("id")));
            c.setName(cursor.getString(cursor.getColumnIndex("name")));
            c.setType(EntryType.EXPENSE);

            config.setCategory(c);
            config.setLimit(cursor.getFloat(cursor.getColumnIndex("limit")));

            expenseConfigs.add(config);
        }
        Log.e(TAG,"ExpenseLimit Found: "+expenseConfigs.size());

        return expenseConfigs;
    }

    @Override
    public float getTotalLimit()
    {Log.e(TAG,"Getting Total ExpeseLimit");
        SQLiteDatabase database = helper.getWritableDatabase();

        String getQuery = "SELECT SUM("+COL_LIMIT+") FROM "+TABLE_CONFIG;

        Log.e(TAG,getQuery);

        Cursor cursor = database.rawQuery(getQuery,null);

        float total = cursor.getFloat(0);
        Log.e(TAG,"Total Limit: "+total);

        return total;
    }
}
