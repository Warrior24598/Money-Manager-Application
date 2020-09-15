package com.example.accounts.databaseService;

import android.database.sqlite.SQLiteDatabase;

import com.example.accounts.models.Category;
import com.example.accounts.models.Entry;
import com.example.accounts.models.EntryType;

import java.util.List;

public interface IEntryService
{
    void addEntry(Entry e);

    void deleteEntry(int id);

    void updateEntry(Entry e);

    Entry getEntry(int id);

    List getEntries();

    List getEntries(String date, EntryType type);

    List getEntries(String date, Category category, EntryType type);

    List searchEntries(String searchQuery, String date,Category category, EntryType type);

    List getRecentEntries(int numberOfEntries);

    List getYears(Category category, EntryType type);

    List getYears(EntryType type);

    List getYears();

    List getMonths(String year, Category category, EntryType type);

    List getMonths(String year, EntryType type);

    List getMonths(String year);

    List getDays(String monthAndYear, Category category, EntryType type);

    List getDays(String monthAndYear, EntryType type);

    List getDays(String monthAndYear);

    float getYearTotal(String year, Category category, EntryType type);

    float getYearTotal(String year, EntryType type);

    float getMonthTotal(String monthAndYear, Category category, EntryType type);

    float getMonthTotal(String monthAndYear, EntryType type);

    float getDateTotal(String date, Category category, EntryType type);

    float getDateTotal(String date, EntryType type);

    float getGrandTotal(Category category, EntryType type);

    float getGrandTotal(EntryType type);
}
