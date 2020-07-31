package com.example.accounts.recyclerviewadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accounts.Constants;
import com.example.accounts.DatabaseHandler;
import com.example.accounts.R;

import java.util.List;
import java.util.logging.Handler;

public class AdapterYears extends RecyclerView.Adapter<AdapterYears.ViewHolder>
{
    Context context;
    List<String> years;
    String category, type;
    DatabaseHandler dbHandler;

    public AdapterYears(Context context, String category, String type)
    {
        this.context = context;
        this.category = category;
        this.type = type;

        dbHandler = DatabaseHandler.getHandler(context);

        updateList();
    }

    public void updateList()
    {
        this.years = dbHandler.getYears(category,type);
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
        float total = dbHandler.getYearTotal(year,category,type);

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
