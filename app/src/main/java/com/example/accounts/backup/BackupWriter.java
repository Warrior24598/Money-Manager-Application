package com.example.accounts.backup;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.accounts.databaseService.ICategoryService;
import com.example.accounts.databaseService.IEntryService;
import com.example.accounts.databaseService.IExpenseLimitService;
import com.example.accounts.models.Category;
import com.example.accounts.models.Entry;
import com.example.accounts.models.ExpenseConfig;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class BackupWriter implements IBackupWriter
{
    private static final String TAG = "BackupWriter";
    Context context;

    public BackupWriter(Context context)
    {
        this.context = context;
    }

    @Override
    public void write(ICategoryService categoryService, IEntryService entryService, IExpenseLimitService limitService)
    {
        IBackupFormat format = new JSONFormat();
        try
        {
            Log.e(TAG,"Getting File Ready to write");
            FileOutputStream backupFile = new FileOutputStream(Environment.getExternalStorageDirectory()+"/accounts.bak");

            Log.e(TAG,"Reading All Categories from Database");
            List<Category> categories = categoryService.getCategories();

            Log.e(TAG,"Reading All Entries from Database");
            List<Entry> entries = entryService.getEntries();

            Log.e(TAG,"Reading All LimitConfigs from Database");
            List<ExpenseConfig> configs = limitService.getExpenseLimit();

            Log.e(TAG,"Converting to JSON object");
            String writableString = format.convertToString(categories,entries,configs);

            Log.e(TAG,"Writing to file");
            backupFile.write(writableString.getBytes());

            Log.e(TAG,"Writing file successful");
            backupFile.close();

            Toast.makeText(context, "Data Exported Successfully", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
