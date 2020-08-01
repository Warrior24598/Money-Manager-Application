package com.example.accounts.models;

public enum EntryType
{
    EXPENSE(0),
    INCOME(1);

    public final int id;

    private EntryType(int id)
    {
        this.id = id;
    }

    public static EntryType find(int id)
    {
        switch (id)
        {
            case 0: return EXPENSE;
            case 1: return INCOME;
            default: return null;
        }
    }
}
