package com.example.accounts.listings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.TextView;

import com.example.accounts.Constants;
import com.example.accounts.SystemSingleTon;
import com.example.accounts.database.DatabaseHandler;
import com.example.accounts.R;
import com.example.accounts.databaseService.ICategoryService;
import com.example.accounts.databaseService.IEntryService;
import com.example.accounts.models.Category;
import com.example.accounts.models.Conversion;
import com.example.accounts.models.EntryType;
import com.example.accounts.recyclerviewadapters.AdapterDays;

public class ListDays extends AppCompatActivity
{

    TextView txtTypeCategory,txtMonthYear, txtMonthTotal;
    RecyclerView recyclerDays;

    SQLiteOpenHelper dbHandler;
    ICategoryService categoryService;
    IEntryService entryService;

    Category category;
    EntryType type;
    String month;

    AdapterDays adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_days);

        //-----------------------------------------//
        //-------INITIALISATION OF WIDGETS---------//
        //-----------------------------------------//

        txtTypeCategory = findViewById(R.id.txtTypeCategory);
        txtMonthYear= findViewById(R.id.txtMonthYear);
        txtMonthTotal= findViewById(R.id.txtMonthTotal);
        recyclerDays = findViewById(R.id.recyclerDays);

        dbHandler = SystemSingleTon.instance().getDatabaseAbstractFactory().createDatabaseHandler(this);
        categoryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(dbHandler).createCategoryService();
        entryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(dbHandler).createEntryService();

        category = categoryService.getCategory(getIntent().getIntExtra(Constants.CATEGORY,-1));
        type = EntryType.find(getIntent().getIntExtra(Constants.TYPE,-1));
        month = getIntent().getStringExtra(Constants.MONTH);

        adapter = new AdapterDays(this,month,category,type);

        //set adapter to recyclerview
        recyclerDays.setAdapter(adapter);
        recyclerDays.setLayoutManager(new LinearLayoutManager(this));


        //set Widget values
        if(category==null)
        {
            txtTypeCategory.setText(type.toString()+" - All");
        }
        else
        {
            txtTypeCategory.setText(type.toString()+" - "+category.getName());
        }
        txtMonthYear.setText(Conversion.getMonthName(month.split("/")[0])+" "+month.split("/")[1]);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        float monthTotal = entryService.getMonthTotal(month,category,type);
        txtMonthTotal.setText("Total: "+monthTotal);
        adapter.updateList();
    }
}
