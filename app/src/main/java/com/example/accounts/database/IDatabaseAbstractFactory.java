package com.example.accounts.database;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

public interface IDatabaseAbstractFactory
{
    SQLiteOpenHelper createDatabaseHandler(Context context);
}
