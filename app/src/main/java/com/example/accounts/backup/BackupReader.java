package com.example.accounts.backup;

import android.content.Context;
import android.util.Log;

import com.example.accounts.databaseService.ICategoryService;
import com.example.accounts.databaseService.IEntryService;
import com.example.accounts.databaseService.IExpenseLimitService;
import com.example.accounts.models.Category;
import com.example.accounts.models.Entry;
import com.example.accounts.models.ExpenseConfig;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class BackupReader implements IBackupReader
{
    private static final String TAG = "BackupReader";
    @Override
    public void read(Context context, String fileName, ICategoryService categoryService, IEntryService entryService, IExpenseLimitService expenseLimitService)
    {
        JSONFormat format = new JSONFormat();
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String fileCotent = reader.readLine();

            Log.e(TAG,"FileContent: "+fileCotent);

            List<Category> categoryList = format.getCategories(fileCotent);
            List<Entry> entryList = format.getEntries(fileCotent);
            List<ExpenseConfig> configList = format.getExpenseLimitConfig(fileCotent);

            restoreCategories(categoryService,categoryList);
            restoryEntries(entryService,entryList);
            restoryExpenseConfig(expenseLimitService,configList);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void restoryExpenseConfig(IExpenseLimitService expenseLimitService, List<ExpenseConfig> configList)
    {
        for(ExpenseConfig c : configList)
        {
            expenseLimitService.addExpenseLimit(c);
        }
    }

    private void restoryEntries(IEntryService entryService, List<Entry> entryList)
    {
        for(Entry e:entryList)
        {
            entryService.addEntry(e);
        }
    }

    private void restoreCategories(ICategoryService categoryService, List<Category> categoryList)
    {
        for(Category c:categoryList)
        {
            categoryService.addCategory(c);
        }
    }

}
