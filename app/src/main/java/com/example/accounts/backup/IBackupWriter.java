package com.example.accounts.backup;

import com.example.accounts.databaseService.ICategoryService;
import com.example.accounts.databaseService.IEntryService;
import com.example.accounts.databaseService.IExpenseLimitService;

public interface IBackupWriter
{
    void write(ICategoryService categoryService, IEntryService entryService, IExpenseLimitService limitService);
}
