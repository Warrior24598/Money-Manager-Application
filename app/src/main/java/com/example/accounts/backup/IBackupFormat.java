package com.example.accounts.backup;

import com.example.accounts.models.Category;
import com.example.accounts.models.Entry;
import com.example.accounts.models.EntryType;

public interface IBackupFormat
{
    String write();

    void read(Category category);

    void read (Entry entry);

    void read (EntryType entryType);
}
