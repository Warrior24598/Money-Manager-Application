package com.example.accounts.listings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.accounts.Constants;
import com.example.accounts.SystemSingleTon;
import com.example.accounts.R;
import com.example.accounts.addupdate.AddEntry;
import com.example.accounts.databaseService.ICategoryService;
import com.example.accounts.databaseService.IEntryService;
import com.example.accounts.models.Category;
import com.example.accounts.models.Entry;
import com.example.accounts.models.EntryType;
import com.example.accounts.recyclerviewadapters.AdapterSearchDays;
import com.example.accounts.recyclerviewadapters.AdapterYears;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ListYears extends AppCompatActivity
{
    private static final String TAG = "ListYears";
    TextView txtTitle, txtCategoryTotal;
    RecyclerView recyclerYears;
    FloatingActionButton fabAddEntry;
    ImageButton backButton;
    SearchView yearSearch;

    Category category;
    EntryType type;

    AdapterYears adapter;
    AdapterSearchDays searchAdapter;

    SQLiteOpenHelper dbHandler;
    ICategoryService categoryService;
    IEntryService entryService;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_years);

        //-----------------------------------------//
        //-------INITIALISATION OF WIDGETS---------//
        //-----------------------------------------//

        txtTitle = findViewById(R.id.txtTitle);
        txtCategoryTotal = findViewById(R.id.txtCategoryTotal);
        recyclerYears = findViewById(R.id.recyclerYears);
        fabAddEntry = findViewById(R.id.fabAddEntry);
        backButton = findViewById(R.id.backButton);
        yearSearch = findViewById(R.id.yearSearch);

        dbHandler = SystemSingleTon.instance().getDatabaseAbstractFactory().createDatabaseHandler(this);
        categoryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(dbHandler).createCategoryService();
        entryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(dbHandler).createEntryService();

        category = categoryService.getCategory(getIntent().getIntExtra(Constants.CATEGORY,-1));
        type = EntryType.find(getIntent().getIntExtra(Constants.TYPE,-1));

        //setup apater and attach to recyclerview
        adapter = new AdapterYears(this,category,type);
        searchAdapter = new AdapterSearchDays(this,null,category,type);


        //-----------------------------------//
        //-------SET VALUES TO WIDGETS-------//
        //-----------------------------------//

        String title = type.toString()+" > ";
        if(null==category)
        {
            title+="ALL";
        }
        else
        {
            title+=category.getName();
        }
        txtTitle.setText(title);

        recyclerYears.setAdapter(adapter);
        recyclerYears.setLayoutManager(new LinearLayoutManager(this));

        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

        //set click listner to fab to add new entry

        fabAddEntry.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent addEntry = new Intent(ListYears.this, AddEntry.class);

                addEntry.putExtra(Constants.CATEGORY,category.getId());
                addEntry.putExtra(Constants.TYPE,type.id);

                startActivity(addEntry);
            }
        });

        yearSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                Log.e(TAG,"Searching for string: "+newText);
                updateAdapterForSearch(newText);

                if(newText.length()==0)
                    recyclerYears.setAdapter(adapter);
                return true;
            }
        });

        yearSearch.setOnCloseListener(new SearchView.OnCloseListener()
        {
            @Override
            public boolean onClose()
            {

                float categoryTotal = entryService.getGrandTotal(category,type);

                txtCategoryTotal.setText("Total: "+categoryTotal);
                recyclerYears.setAdapter(adapter);
                return true;
            }
        });

    }

    private void updateAdapterForSearch(String searchString)
    {
        List<Entry> searchEntries = entryService.searchEntries(searchString,null,category,type);
        searchAdapter.setEntries(searchEntries);
        recyclerYears.setAdapter(searchAdapter);

        txtCategoryTotal.setText("Total: "+countTotal(searchEntries));
    }

    private  float countTotal(List<Entry> entries)
    {
        float total = 0;
        for(Entry e: entries)
        {
            total+=e.getAmount();
        }
        return total;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        float categoryTotal = entryService.getGrandTotal(category,type);

        txtCategoryTotal.setText("Total: "+categoryTotal);
        adapter.updateList();
    }
}
