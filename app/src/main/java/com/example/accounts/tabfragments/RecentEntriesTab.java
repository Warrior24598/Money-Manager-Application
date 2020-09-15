package com.example.accounts.tabfragments;

import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accounts.R;
import com.example.accounts.SystemSingleTon;
import com.example.accounts.databaseService.IEntryService;
import com.example.accounts.recyclerviewadapters.AdapterRecentEntries;

public class RecentEntriesTab extends Fragment
{
    SQLiteOpenHelper databaseHandler;
    IEntryService entryService;

    AdapterRecentEntries adapterRecentEntries;
    RecyclerView recyclerRecentEntries;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.tab_recent,container,false);

        recyclerRecentEntries = view.findViewById(R.id.recyclerRecentEntries);

        databaseHandler = SystemSingleTon.instance().getDatabaseAbstractFactory().createDatabaseHandler(getContext());
        entryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(databaseHandler).createEntryService();

        adapterRecentEntries = new AdapterRecentEntries(getContext(),entryService.getRecentEntries(25));

        recyclerRecentEntries.setAdapter(adapterRecentEntries);
        recyclerRecentEntries.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }
}
