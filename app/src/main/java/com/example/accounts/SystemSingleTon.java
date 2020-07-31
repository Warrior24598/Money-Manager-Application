package com.example.accounts;

import android.database.sqlite.SQLiteOpenHelper;

import com.example.accounts.database.DatabaseAbstractFactory;
import com.example.accounts.database.IDatabaseAbstractFactory;
import com.example.accounts.databaseService.DatabaseServiceAbstractFactory;
import com.example.accounts.databaseService.IDatabaseServiceAbstractFactory;

public class SystemSingleTon
{
    private IDatabaseAbstractFactory databaseAbstractFactory = null;
    private IDatabaseServiceAbstractFactory databaseServiceAbstractFactory = null;

    private static SystemSingleTon instance;

    public static SystemSingleTon instance()
    {
        if(instance==null)
        {
            instance = new SystemSingleTon();
        }
        return instance;
    }

    public IDatabaseAbstractFactory getDatabaseAbstractFactory()
    {
        if(databaseAbstractFactory == null)
        {
            databaseAbstractFactory = new DatabaseAbstractFactory();
        }
        return databaseAbstractFactory;
    }

    public IDatabaseServiceAbstractFactory getDatabaseServiceAbstractFactory(SQLiteOpenHelper helper)
    {
        if(databaseServiceAbstractFactory == null)
        {
            databaseServiceAbstractFactory = new DatabaseServiceAbstractFactory(helper);
        }
        return databaseServiceAbstractFactory;
    }
}
