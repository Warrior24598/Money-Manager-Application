package com.example.accounts.databaseService;

import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseServiceAbstractFactory implements IDatabaseServiceAbstractFactory
{
    private IEntryService entryService = null;

    public DatabaseServiceAbstractFactory(SQLiteOpenHelper helper)
    {
        entryService = new EntryService(helper);
    }

    @Override
    public IEntryService createEntryService()
    {
        return entryService;
    }
}
