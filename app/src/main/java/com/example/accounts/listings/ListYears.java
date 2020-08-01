package com.example.accounts.listings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.accounts.Constants;
import com.example.accounts.SystemSingleTon;
import com.example.accounts.database.DatabaseHandler;
import com.example.accounts.R;
import com.example.accounts.addupdate.AddEntry;
import com.example.accounts.databaseService.ICategoryService;
import com.example.accounts.databaseService.IEntryService;
import com.example.accounts.models.Category;
import com.example.accounts.models.EntryType;
import com.example.accounts.recyclerviewadapters.AdapterYears;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ListYears extends AppCompatActivity
{
    private static final String TAG = "ListYears";
    TextView txtCategory, txtType, txtCategoryTotal;
    RecyclerView recyclerYears;
    FloatingActionButton fabAddEntry;

    Category category;
    EntryType type;

    AdapterYears adapter;

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

        txtCategory = findViewById(R.id.txtCategory);
        txtCategoryTotal = findViewById(R.id.txtCategoryTotal);
        txtType= findViewById(R.id.txtType);
        recyclerYears = findViewById(R.id.recyclerYears);
        fabAddEntry = findViewById(R.id.fabAddEntry);

        dbHandler = SystemSingleTon.instance().getDatabaseAbstractFactory().createDatabaseHandler(this);
        categoryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(dbHandler).createCategoryService();
        entryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(dbHandler).createEntryService();

        Log.e(TAG,"Entry: "+getIntent().getIntExtra(Constants.TYPE,-1));
        category = categoryService.getCategory(getIntent().getIntExtra(Constants.CATEGORY,-1));
        type = EntryType.find(getIntent().getIntExtra(Constants.TYPE,-1));



        //setup apater and attach to recyclerview
        adapter = new AdapterYears(this,category,type);


        //-----------------------------------//
        //-------SET VALUES TO WIDGETS-------//
        //-----------------------------------//

        if(null==category)
        {
            txtCategory.setText("ALL");
        }
        else
        {
            txtCategory.setText(category.getName());
        }
        txtType.setText(type.toString());

        recyclerYears.setAdapter(adapter);
        recyclerYears.setLayoutManager(new LinearLayoutManager(this));

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
