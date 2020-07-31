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
import com.google.android.material.card.MaterialCardView;

import java.util.List;

class AdapterMonths extends RecyclerView.Adapter<AdapterMonths.ViewHolder>
{

    Context context;
    List<String> months;
    String year,category, type;
    DatabaseHandler dbHandler;

    public AdapterMonths(Context context, String year, String category, String type)
    {
        this.context = context;
        this.year = year;
        this.category = category;
        this.type = type;

        dbHandler = DatabaseHandler.getHandler(context);

        this.months = dbHandler.getMonths(year,category,type);
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
        float total = dbHandler.getMonthTotal(month+"/"+year,category,type);

        holder.txtMonth.setText(Conversion.getMonthName(month));
        holder.txtTotal.setText(String.valueOf(total));

        //set touch handler to open new tab of days of month
        final Intent dayList = new Intent(context, ListDays.class);
        dayList.putExtra(Constants.MONTH,month+"/"+year);
        dayList.putExtra(Constants.TYPE,type);
        dayList.putExtra(Constants.CATEGORY,category);

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
