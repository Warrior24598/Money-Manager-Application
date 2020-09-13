package com.example.accounts.backup;


import com.example.accounts.models.Category;
import com.example.accounts.models.Entry;
import com.example.accounts.models.EntryType;

import org.json.simple.JSONObject;

public class JSONFormat implements IBackupFormat
{
    @Override
    public String write()
    {
        return null;
    }

    @Override
    public void read(Category category)
    {

    }

    @Override
    public void read(Entry entry)
    {

    }

    @Override
    public void read(EntryType entryType)
    {

    }
}
