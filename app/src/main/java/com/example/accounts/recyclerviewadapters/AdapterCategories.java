package com.example.accounts.recyclerviewadapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accounts.Constants;
import com.example.accounts.DatabaseHandler;
import com.example.accounts.R;
import com.example.accounts.listings.ListYears;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class AdapterCategories extends RecyclerView.Adapter<AdapterCategories.ViewHolder>
{
    Context context;
    public List<String> categories;
    String type;

    DatabaseHandler dbHandler;

    public AdapterCategories(Context context, List<String> categories, String type)
    {
        this.context = context;
        this.categories = categories;
        this.type = type;

        dbHandler = DatabaseHandler.getHandler(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_custom,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        final String category = categories.get(position);

        holder.txtCategory.setText(category);

        //open list of years on clicking on category container

        final Intent yearList = new Intent(context, ListYears.class);
        yearList.putExtra(Constants.CATEGORY,category);
        yearList.putExtra(Constants.TYPE,type);

        holder.cardContainer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                context.startActivity(yearList);
            }
        });

        holder.cardContainer.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                openAlert(category);
                return true;
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return categories.size();
    }

    //open alertbox to confirm deletion of catgory
    void openAlert(final String category)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Confirm Delete: ");
        builder.setMessage("Category: "+category+"\n\n!!! ALL ENTRIES OF THIS CATEGORY WILL BE DELETED !!!");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dbHandler.deleteCategory(category,type);
                Toast.makeText(context, "Category Deleted: "+category, Toast.LENGTH_SHORT).show();

                categories.remove(category);
                notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                return;
            }
        });

        builder.show();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        MaterialCardView cardContainer;
        TextView txtCategory;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            cardContainer = itemView.findViewById(R.id.cardContainer);
            txtCategory = itemView.findViewById(R.id.txtCategory);
        }
    }
}
