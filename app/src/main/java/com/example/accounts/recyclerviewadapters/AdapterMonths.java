package com.example.accounts.recyclerviewadapters;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accounts.Constants;
import com.example.accounts.SystemSingleTon;
import com.example.accounts.database.DatabaseHandler;
import com.example.accounts.R;
import com.example.accounts.databaseService.ICategoryService;
import com.example.accounts.databaseService.IEntryService;
import com.example.accounts.models.Category;
import com.example.accounts.models.Conversion;
import com.example.accounts.listings.ListDays;
import com.example.accounts.models.EntryType;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

class AdapterMonths extends RecyclerView.Adapter<AdapterMonths.ViewHolder>
{

    Context context;
    List<String> months;
    String year;
    Category category;
    EntryType type;

    SQLiteOpenHelper dbHandler;
    ICategoryService categoryService;
    IEntryService entryService;

    public AdapterMonths(Context context, String year, Category category, EntryType type)
    {
        this.context = context;
        this.year = year;
        this.category = category;
        this.type = type;

        dbHandler = SystemSingleTon.instance().getDatabaseAbstractFactory().createDatabaseHandler(context);
        categoryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(dbHandler).createCategoryService();
        entryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(dbHandler).createEntryService();

        this.months = entryService.getMonths(year,category,type);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.month_custom,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        String month = months.get(position);
        float total = entryService.getMonthTotal(month+"/"+year,category,type);

        holder.txtMonth.setText(Conversion.getMonthName(month));
        holder.txtTotal.setText(String.valueOf(total));

        //set touch handler to open new tab of days of month
        final Intent dayList = new Intent(context, ListDays.class);
        dayList.putExtra(Constants.MONTH,month+"/"+year);
        dayList.putExtra(Constants.TYPE,type.id);
        if(category!=null)
            dayList.putExtra(Constants.CATEGORY,category.getId());

        holder.cardContainer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                context.startActivity(dayList);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return months.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        MaterialCardView cardContainer;
        TextView txtMonth, txtTotal;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            cardContainer = itemView.findViewById(R.id.cardContainer);
            txtMonth = itemView.findViewById(R.id.txtMonth);
            txtTotal = itemView.findViewById(R.id.txtTotal);
        }
    }
}
