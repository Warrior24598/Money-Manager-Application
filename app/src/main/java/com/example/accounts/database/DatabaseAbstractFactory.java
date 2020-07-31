package com.example.accounts.database;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseAbstractFactory implements IDatabaseAbstractFactory
{
    private SQLiteOpenHelper databaseHandler=null;

    @Override
    public SQLiteOpenHelper createDatabaseHandler(Context context)
    {
        if(databaseHandler == null)
        {
            databaseHandler = DatabaseHandler.getHandler(context);
        }
        return databaseHandler;
    }
}
