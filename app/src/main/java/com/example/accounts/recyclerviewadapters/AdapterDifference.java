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
import com.example.accounts.DatabaseHandler;
import com.example.accounts.R;
import com.example.accounts.data.Conversion;
import com.example.accounts.listings.ListDays;

import java.util.List;

public class AdapterDifference extends RecyclerView.Adapter<AdapterDifference.ViewHolder>
{
    Context context;
    String year;
    List<String> months;

    DatabaseHandler dbHandler;

    public AdapterDifference(Context context, String year)
    {
        this.context = context;
        this.year = year;

        dbHandler = DatabaseHandler.getHandler(context);
        this.months = dbHandler.getMonths(year, Constants.ALLCATS,Constants.ALLTYPE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_month_difference,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        final String month = months.get(position);
        float income = dbHandler.getMonthTotal(month+"/"+year,Constants.ALLCATS,Constants.TYPE_INCOME);
        float expense = dbHandler.getMonthTotal(month+"/"+year,Constants.ALLCATS,Constants.TYPE_EXPENSE);
        float saving = income - expense;

        holder.txtMonth.setText(Conversion.getMonthName(month));
        holder.txtIncome.setText(String.valueOf(income));
        holder.txtExpense.setText(String.valueOf(expense));
        holder.txtSaving.setText(String.valueOf(saving));

        holder.txtIncome.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent income = new Intent(context, ListDays.class);
                income.putExtra(Constants.MONTH,month+"/"+year);
                income.putExtra(Constants.CATEGORY,Constants.ALLCATS);
                income.putExtra(Constants.TYPE,Constants.TYPE_INCOME);

                context.startActivity(income);
            }
        });

        holder.txtExpense.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent expense = new Intent(context, ListDays.class);
                expense.putExtra(Constants.MONTH,month+"/"+year);
                expense.putExtra(Constants.CATEGORY,Constants.ALLCATS);
                expense.putExtra(Constants.TYPE,Constants.TYPE_EXPENSE);

                context.startActivity(expense);
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
        TextView txtMonth, txtExpense, txtIncome, txtSaving;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            txtMonth = itemView.findViewById(R.id.txtMonth);
            txtIncome= itemView.findViewById(R.id.txtIncome);
            txtExpense= itemView.findViewById(R.id.txtExpense);
            txtSaving= itemView.findViewById(R.id.txtSaving);
        }
    }
}
