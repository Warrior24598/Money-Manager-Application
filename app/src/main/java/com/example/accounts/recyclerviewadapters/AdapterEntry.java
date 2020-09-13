package com.example.accounts.recyclerviewadapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accounts.Constants;
import com.example.accounts.SystemSingleTon;
import com.example.accounts.database.DatabaseHandler;
import com.example.accounts.R;
import com.example.accounts.addupdate.UpdateEntry;
import com.example.accounts.databaseService.ICategoryService;
import com.example.accounts.databaseService.IEntryService;
import com.example.accounts.models.Category;
import com.example.accounts.models.Entry;
import com.example.accounts.models.EntryType;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class AdapterEntry extends RecyclerView.Adapter<AdapterEntry.ViewHolder>
{
    private static final String TAG = "AdapterEntry";
    
    Context context;
    Category category;
    List<Entry> entryList;

    SQLiteOpenHelper dbHandler;
    IEntryService entryService;

    public AdapterEntry(Context context, List<Entry> entries, Category category)
    {
        this.context = context;
        this.category = category;
        this.entryList = entries;

        dbHandler = SystemSingleTon.instance().getDatabaseAbstractFactory().createDatabaseHandler(context);
        entryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(dbHandler).createEntryService();
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        int layout;
        if(this.category==null)
        {
            layout = R.layout.entry_custom_with_label;
        }
        else
        {
            layout = R.layout.entry_custom;
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position)
    {
        final Entry e = entryList.get(position);
        Log.e(TAG,"Biniding View ID: "+e.getId());
        String source = e.getSource();
        Category category = e.getCategory();
        float total = e.getAmount();

        holder.txtEntry.setText(source);
        holder.txtTotal.setText(String.valueOf(total));

        if(this.category==null)
            holder.txtCategory.setText(category.getName());

        //set longclick listner to open menu


        final PopupMenu menu = new PopupMenu(context,holder.cardContainer);

        menu.inflate(R.menu.entry_menu);

        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.edit:

                        Intent update = new Intent(context, UpdateEntry.class);
                        update.putExtra(Constants.ID,e.getId());

                        context.startActivity(update);

                        return true;

                    case R.id.delete:
                        openAlert(e);
                        return true;
                    default:
                        return false;
                }
            }
        });

        holder.cardContainer.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                menu.show();
                return true;
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return entryList.size();
    }



    //alertbox to show confirming delete of entry from database
    public void openAlert(final Entry e)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Confirm Deleting Entry:");

        builder.setMessage(e.toString());

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                entryService.deleteEntry(e.getId());
                Toast.makeText(context, "Entry Deleted", Toast.LENGTH_SHORT).show();
                entryList.remove(e);
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
        TextView txtCategory, txtEntry, txtTotal;
        MaterialCardView cardContainer;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            cardContainer = itemView.findViewById(R.id.cardContainer);
            txtEntry = itemView.findViewById(R.id.txtEntry);
            txtTotal = itemView.findViewById(R.id.txtTotal);

            if(category==null)
                txtCategory= itemView.findViewById(R.id.txtCategory);
        }
    }
}
