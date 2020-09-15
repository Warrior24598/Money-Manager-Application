package com.example.accounts.backup;

import android.content.Context;

import com.example.accounts.databaseService.ICategoryService;
import com.example.accounts.databaseService.IEntryService;
import com.example.accounts.databaseService.IExpenseLimitService;

public interface IBackupReader
{
    void read(Context context, String fileName, ICategoryService categoryService, IEntryService entryService, IExpenseLimitService expenseLimitService);
}
