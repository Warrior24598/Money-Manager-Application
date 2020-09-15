package com.example.accounts.backup;

import com.example.accounts.models.Category;
import com.example.accounts.models.Entry;
import com.example.accounts.models.ExpenseConfig;

import org.json.simple.JSONArray;

import java.io.File;
import java.util.List;

public interface IBackupFormat
{
    String convertToString(List<Category> categories, List<Entry> entries, List<ExpenseConfig> configs);

    JSONArray writeCategories(List<Category> categories);
    JSONArray writeEntries(List<Entry> entries);
    JSONArray writeExpenseLimitConfig(List<ExpenseConfig> configs);

    List getCategories(String fileContent);
    List getEntries(String fileContent);
    List getExpenseLimitConfig(String fileContent);

}
