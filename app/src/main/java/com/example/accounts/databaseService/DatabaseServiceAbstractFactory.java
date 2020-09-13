package com.example.accounts.databaseService;

import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseServiceAbstractFactory implements IDatabaseServiceAbstractFactory
{
    private IEntryService entryService = null;
    private ICategoryService categoryService = null;
    private IExpenseLimitService expenseLimitService = null;

    public DatabaseServiceAbstractFactory(SQLiteOpenHelper helper)
    {
        entryService = new EntryService(helper);
        categoryService = new CategoryService(helper);
        expenseLimitService = new ExpenseLimitService(helper);
    }

    @Override
    public IEntryService createEntryService()
    {
        return entryService;
    }

    @Override
    public ICategoryService createCategoryService()
    {
        return categoryService;
    }

    @Override
    public IExpenseLimitService createExpenseLimitService()
    {
        return expenseLimitService;
    }
}
