package com.example.accounts.tabfragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accounts.Constants;
import com.example.accounts.database.DatabaseHandler;
import com.example.accounts.R;
import com.example.accounts.dialogs.AddCategory;
import com.example.accounts.listings.ListYears;
import com.example.accounts.models.EntryType;
import com.example.accounts.recyclerviewadapters.AdapterCategories;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ExpenseTab extends Fragment
{
    View view;

    RecyclerView recyclerCategories;
    TextView txtGrandTotal;
    FloatingActionButton fabAddCategory;

    AdapterCategories adapter;

    DatabaseHandler handler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if(view==null)
        {

            view = inflater.inflate(R.layout.tab_expense,container,false);

            //-----------------------------------------//
            //-------INITIALISATION OF WIDGETS---------//
            //-----------------------------------------//

            recyclerCategories = view.findViewById(R.id.recyclerCategories);
            txtGrandTotal = view.findViewById(R.id.txtGrandTotal);
            fabAddCategory = view.findViewById(R.id.fabAddCategory);

            handler = DatabaseHandler.getHandler(view.getContext());

            List<String> categories = handler.getCategories(EntryType.EXPENSE);

            adapter = new AdapterCategories(view.getContext(),categories, EntryType.EXPENSE);


            //Set adapter to recyclerview
            recyclerCategories.setAdapter(adapter);
            recyclerCategories.setLayoutManager(new LinearLayoutManager(view.getContext()));


            //open add categories dialog on click on fab
            final AddCategory addCategoryDialog = new AddCategory(view.getContext(),EntryType.EXPENSE,adapter);

            fabAddCategory.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    addCategoryDialog.show(getFragmentManager(),"AddCategory");
                }
            });

            fabAddCategory.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    builder.setTitle("WARNING");
                    builder.setMessage("DELETE EVERYTHING");

                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            handler.deleteAll();
                            System.exit(0);
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

                    return true;
                }
            });


            txtGrandTotal.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent yearList = new Intent(getContext(), ListYears.class);
                    yearList.putExtra(Constants.CATEGORY,Constants.ALLCATS);
                    yearList.putExtra(Constants.TYPE, EntryType.EXPENSE);
                    getContext().startActivity(yearList);
                }
            });

        }
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        //set GrandTotal to widget
        float total = handler.getGrandTotal(Constants.ALLCATS,EntryType.EXPENSE);
        txtGrandTotal.setText("Grand Total: "+total);
    }
}
