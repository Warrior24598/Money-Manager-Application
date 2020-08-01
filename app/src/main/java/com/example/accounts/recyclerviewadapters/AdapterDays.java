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

import com.example.accounts.SystemSingleTon;
import com.example.accounts.database.DatabaseHandler;
import com.example.accounts.R;
import com.example.accounts.databaseService.ICategoryService;
import com.example.accounts.databaseService.IEntryService;
import com.example.accounts.models.Category;
import com.example.accounts.models.EntryType;

import java.util.List;

public class AdapterDays extends RecyclerView.Adapter<AdapterDays.ViewHolder>
{

    Context context;
    List<String> days;
    String monthAndYear;
    Category category;
    EntryType type;

    SQLiteOpenHelper dbHandler;
    ICategoryService categoryService;
    IEntryService entryService;

    public AdapterDays(Context context, String monthAndYear, Category category, EntryType type)
    {
        this.context = context;
        this.monthAndYear = monthAndYear;
        this.category = category;
        this.type = type;

        dbHandler = SystemSingleTon.instance().getDatabaseAbstractFactory().createDatabaseHandler(context);
        categoryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(dbHandler).createCategoryService();
        entryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(dbHandler).createEntryService();

        updateList();
    }

    public void updateList()
    {
        this.days = entryService.getDays(monthAndYear,category,type);
        notifyDataSetChanged();
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

        AdapterEntry adapter = new AdapterEntry(context,day,category,type);

        holder.recyclerEntry.setAdapter(adapter);
        holder.recyclerEntry.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public int getItemCount()
    {
        return days.size();
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
