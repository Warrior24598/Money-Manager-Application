package com.example.accounts.tabfragments;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accounts.Constants;
import com.example.accounts.SystemSingleTon;
import com.example.accounts.database.DatabaseHandler;
import com.example.accounts.R;
import com.example.accounts.databaseService.ICategoryService;
import com.example.accounts.databaseService.IEntryService;
import com.example.accounts.dialogs.AddCategory;
import com.example.accounts.listings.ListYears;
import com.example.accounts.models.Category;
import com.example.accounts.models.EntryType;
import com.example.accounts.recyclerviewadapters.AdapterCategories;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class IncomeTab extends Fragment
{
    View view;

    RecyclerView recyclerCategories;
    TextView txtGrandTotal;
    FloatingActionButton fabAddCategory;

    AdapterCategories adapter;

    SQLiteOpenHelper dbHandler;
    ICategoryService categoryService;
    IEntryService entryService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        if(view==null)
        {

            view = inflater.inflate(R.layout.tab_income,container,false);

            //-----------------------------------------//
            //-------INITIALISATION OF WIDGETS---------//
            //-----------------------------------------//

            recyclerCategories = view.findViewById(R.id.recyclerCategories);
            txtGrandTotal = view.findViewById(R.id.txtGrandTotal);
            fabAddCategory = view.findViewById(R.id.fabAddCategory);

            dbHandler = SystemSingleTon.instance().getDatabaseAbstractFactory().createDatabaseHandler(getContext());
            categoryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(dbHandler).createCategoryService();
            entryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(dbHandler).createEntryService();


            adapter = new AdapterCategories(view.getContext(),EntryType.INCOME);


            //Set adapter to recyclerview
            recyclerCategories.setAdapter(adapter);
            recyclerCategories.setLayoutManager(new LinearLayoutManager(view.getContext()));

            //open add categories dialog on click on fab

            final AddCategory addCategoryDialog = new AddCategory(view.getContext(),EntryType.INCOME,adapter);

            fabAddCategory.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    addCategoryDialog.show(getFragmentManager(),"AddCategory");
                }
            });

            txtGrandTotal.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent yearList = new Intent(getContext(), ListYears.class);
                    yearList.putExtra(Constants.TYPE, EntryType.INCOME.id);
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
        float total = entryService.getGrandTotal(EntryType.INCOME);
        txtGrandTotal.setText("Grand Total: "+total);
    }
}
