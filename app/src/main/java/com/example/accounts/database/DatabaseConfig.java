package com.example.accounts.database;

public class DatabaseConfig
{

    public static final String DATABASE = "accounts.db";
    public static final int DATABASE_VERSION = 2;

    //---------Accounts entry table---------//
    public static final String TABLE_ENTRY = "entry";
    public static final String COL_ID = "id";
    public static final String COL_SOURCE = "source";
    public static final String COL_AMOUNT = "amount";
    public static final String COL_DATE = "date";
    public static final String COL_CATEGORY_ID = "categoryId";
    public static final String COL_TYPE_ID = "typeId";


    //---------Category table---------//
    public static final String TABLE_CATEGORY= "category";
    public static final String COL_NAME = "name";

    //---------Type table-----------//
    public static final String TABLE_TYPE = "type";


    //----------Configuration table--------//
    public static final String TABLE_CONFIG = "expenseConfig";
    public static final String COL_EXPENSE_CATEGORY_ID = "categoryId";
    public static final String COL_LIMIT = "`limit`";
}
