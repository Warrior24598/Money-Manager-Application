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

public class AdapterYears extends RecyclerView.Adapter<AdapterYears.ViewHolder>
{
    Context context;
    List<String> years;
    Category category;
    EntryType type;

    SQLiteOpenHelper dbHandler;
    ICategoryService categoryService;
    IEntryService entryService;

    public AdapterYears(Context context, Category category, EntryType type)
    {
        this.context = context;
        this.category = category;
        this.type = type;

        dbHandler = SystemSingleTon.instance().getDatabaseAbstractFactory().createDatabaseHandler(context);
        categoryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(dbHandler).createCategoryService();
        entryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(dbHandler).createEntryService();

        updateList();
    }

    public void updateList()
    {
        this.years = entryService.getYears(category,type);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.year_custom, parent, false);

        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        String year = years.get(position);
        float total = entryService.getYearTotal(year,category,type);

        holder.txtYear.setText(year);
        holder.txtTotal.setText(String.valueOf(total));

        //get list of months from year

        AdapterMonths adapterMonths = new AdapterMonths(context,year,category,type);

        holder.recyclerMonths.setAdapter(adapterMonths);
        holder.recyclerMonths.setLayoutManager(new LinearLayoutManager(context.getApplicationContext()));
    }

    @Override
    public int getItemCount()
    {
        return years.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtYear, txtTotal;
        RecyclerView recyclerMonths;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            txtYear = itemView.findViewById(R.id.txtYear);
            recyclerMonths = itemView.findViewById(R.id.recyclerMonths);
            txtTotal = itemView.findViewById(R.id.txtTotal);
        }
    }
}
