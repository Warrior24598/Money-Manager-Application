package com.example.accounts.recyclerviewadapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accounts.R;
import com.example.accounts.models.Entry;
import com.example.accounts.models.EntryType;

import java.util.List;

public class AdapterRecentEntries extends RecyclerView.Adapter<AdapterRecentEntries.ViewHolder>
{
    Context context;
    List<Entry> recentEntries;

    public AdapterRecentEntries(Context context, List<Entry> recentEntries)
    {
        this.context = context;
        this.recentEntries = recentEntries;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_recent_entry,parent,false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Entry e = recentEntries.get(position);

        holder.txtSource.setText(e.getSource());
        holder.txtCategory.setText(e.getCategory().getName());
        holder.txtDate.setText(e.getDate());

        if(e.getCategory().getType().equals(EntryType.INCOME))
        {
            holder.txtAmount.setText("+"+String.valueOf(e.getAmount()));
            holder.txtAmount.setTextColor(Color.GREEN);
        }
        else
        {
            holder.txtAmount.setText("-"+String.valueOf(e.getAmount()));
            holder.txtAmount.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount()
    {
        return recentEntries.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView txtSource, txtCategory, txtAmount, txtDate;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            txtSource = itemView.findViewById(R.id.txtSource);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtDate = itemView.findViewById(R.id.txtDate);
        }
    }
}
