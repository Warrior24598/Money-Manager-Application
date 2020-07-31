package com.example.accounts.recyclerviewadapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accounts.Constants;
import com.example.accounts.database.DatabaseHandler;
import com.example.accounts.R;
import com.example.accounts.listings.ListMonthDifference;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class AdapterDifferenceYear extends RecyclerView.Adapter<AdapterDifferenceYear.ViewHolder>
{

    Context context;
    List<String> years;
    String category, type;
    DatabaseHandler dbHandler;

    public AdapterDifferenceYear(Context context)
    {
        this.context = context;
        this.category = Constants.ALLCATS;
        this.type = Constants.ALLTYPE;

        dbHandler = DatabaseHandler.getHandler(context);

        this.years = dbHandler.getYears(category,type);
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
        String year = years.get(position);
        float incomeTotal = dbHandler.getYearTotal(year,category,Constants.TYPE_INCOME);
        float expenseTotal = dbHandler.getYearTotal(year,category,Constants.TYPE_EXPENSE);

        holder.txtMonth.setText(year);
        holder.txtTotal.setText(String.valueOf(incomeTotal-expenseTotal));

        //set touch handler to open new tab of days of month
        final Intent monthDifferenceList = new Intent(context, ListMonthDifference.class);
        monthDifferenceList.putExtra(Constants.YEAR,year);

        holder.cardContainer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                context.startActivity(monthDifferenceList);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return years.size();
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
