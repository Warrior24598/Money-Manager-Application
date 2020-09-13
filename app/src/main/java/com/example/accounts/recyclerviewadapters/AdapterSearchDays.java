package com.example.accounts.recyclerviewadapters;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accounts.R;
import com.example.accounts.SystemSingleTon;
import com.example.accounts.databaseService.ICategoryService;
import com.example.accounts.databaseService.IEntryService;
import com.example.accounts.models.Category;
import com.example.accounts.models.Entry;
import com.example.accounts.models.EntryType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterSearchDays extends RecyclerView.Adapter<AdapterSearchDays.ViewHolder>
{

    Context context;
    List<Entry> entries;
    List<String> days;
    Category category;
    EntryType type;

    Map<String, List<Entry>> uniqueDateEntries;

    SQLiteOpenHelper dbHandler;
    ICategoryService categoryService;
    IEntryService entryService;

    public AdapterSearchDays(Context context,  List<Entry> entries, Category category, EntryType type)
    {
        this.type = type;
        this.context = context;
        this.entries = entries;
        this.category = category;

        dbHandler = SystemSingleTon.instance().getDatabaseAbstractFactory().createDatabaseHandler(context);
        categoryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(dbHandler).createCategoryService();
        entryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(dbHandler).createEntryService();

        filterUniqueDates(entries);
    }

    public void setEntries(List<Entry> entries)
    {
        this.entries = entries;

        filterUniqueDates(entries);
        notifyDataSetChanged();

    }

    private void filterUniqueDates(List<Entry> entries)
    {
        uniqueDateEntries = new HashMap<>();
        days = new ArrayList<>();

        if(entries==null)
            return;

        for(Entry e : entries)
        {
            if(uniqueDateEntries.get(e.getDate())==null)
            {
                List<Entry> entryList = new ArrayList<>();
                entryList.add(e);
                uniqueDateEntries.put(e.getDate(),entryList);
                days.add(e.getDate());
            }
            else
            {
                uniqueDateEntries.get(e.getDate()).add(e);
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_custom,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        String day = days.get(position);
        float total = entryService.getDateTotal(day,category,type);

        holder.txtDay.setText(day);
        holder.txtTotal.setText(String.valueOf(total));

        List<Entry> entries = uniqueDateEntries.get(day);

        AdapterEntry adapter = new AdapterEntry(context,entries,category);

        holder.recyclerEntry.setAdapter(adapter);
        holder.recyclerEntry.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public int getItemCount()
    {
        return uniqueDateEntries.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        RecyclerView recyclerEntry;
        TextView txtDay, txtTotal;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            recyclerEntry = itemView.findViewById(R.id.recyclerEntry);
            txtDay = itemView.findViewById(R.id.txtDay);
            txtTotal = itemView.findViewById(R.id.txtTotal);
        }
    }
}
