package com.example.accounts.listings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.accounts.Constants;
import com.example.accounts.SystemSingleTon;
import com.example.accounts.database.DatabaseHandler;
import com.example.accounts.R;
import com.example.accounts.databaseService.ICategoryService;
import com.example.accounts.databaseService.IEntryService;
import com.example.accounts.databaseService.IExpenseLimitService;
import com.example.accounts.dialogs.AddExpenseLimitConfigDialog;
import com.example.accounts.dialogs.OnActionCompletedListner;
import com.example.accounts.models.Category;
import com.example.accounts.models.Conversion;
import com.example.accounts.models.Entry;
import com.example.accounts.models.EntryType;
import com.example.accounts.models.ExpenseConfig;
import com.example.accounts.recyclerviewadapters.AdapterDays;
import com.example.accounts.recyclerviewadapters.AdapterSearchDays;

import java.util.List;

public class ListDays extends AppCompatActivity
{
    private static final String TAG = "ListDays";

    TextView txtTitle, txtMonthTotal,txtExpenseLimit;
    RecyclerView recyclerDays;
    ImageButton backButton;
    SearchView monthSearch;

    SQLiteOpenHelper dbHandler;
    ICategoryService categoryService;
    IEntryService entryService;
    IExpenseLimitService expenseLimitService;

    Category category;
    EntryType type;
    String month;

    AdapterDays adapter;
    AdapterSearchDays searchAdapter;

    ExpenseConfig config;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_days);

        //-----------------------------------------//
        //-------INITIALISATION OF WIDGETS---------//
        //-----------------------------------------//

        txtTitle = findViewById(R.id.txtTitle);
        txtMonthTotal= findViewById(R.id.txtMonthTotal);
        txtExpenseLimit = findViewById(R.id.txtExpenseLimit);
        backButton = findViewById(R.id.backButton);
        recyclerDays = findViewById(R.id.recyclerDays);
        monthSearch = findViewById(R.id.monthSearch);

        dbHandler = SystemSingleTon.instance().getDatabaseAbstractFactory().createDatabaseHandler(this);
        categoryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(dbHandler).createCategoryService();
        entryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(dbHandler).createEntryService();
        expenseLimitService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(dbHandler).createExpenseLimitService();

        category = categoryService.getCategory(getIntent().getIntExtra(Constants.CATEGORY,-1));
        type = EntryType.find(getIntent().getIntExtra(Constants.TYPE,-1));
        month = getIntent().getStringExtra(Constants.MONTH);

        config = expenseLimitService.getExpenseLimit(category);

        adapter = new AdapterDays(this,month,category,type);
        searchAdapter = new AdapterSearchDays(this,null,category,type);

        //set adapter to recyclerview
        recyclerDays.setAdapter(adapter);
        recyclerDays.setLayoutManager(new LinearLayoutManager(this));

        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

        monthSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener()
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
                    recyclerDays.setAdapter(adapter);
                return true;
            }
        });

        monthSearch.setOnCloseListener(new SearchView.OnCloseListener()
        {
            @Override
            public boolean onClose()
            {
                float monthTotal = entryService.getMonthTotal(month,category,type);
                txtMonthTotal.setText("Total: "+monthTotal);
                recyclerDays.setAdapter(adapter);
                return true;
            }
        });

        //set Widget values

        String title = type.toString()+" > ";
        if(category==null)
        {
            title+="ALL > ";
        }
        else
        {
            title+=category.getName()+" > ";
        }
        title+=Conversion.getMonthName(month.split("/")[0])+" "+month.split("/")[1];
        txtTitle.setText(title);

        /*
         * ******Configurations for Expense Limit Text View
         * */

        if(category==null || type == EntryType.INCOME)
        {
            txtExpenseLimit.setVisibility(View.GONE);
        }

        if(config==null)
        {
            txtExpenseLimit.setText("Limit: NOT SET");
        }
        else
        {
            txtExpenseLimit.setText("Limit: "+config.getLimit());
        }

        txtExpenseLimit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(config==null)
                {
                    AddExpenseLimitConfigDialog dialog = new AddExpenseLimitConfigDialog(category, new OnActionCompletedListner()
                    {
                        @Override
                        public void OnActionComplete(ExpenseConfig c)
                        {
                            config=c;

                            txtExpenseLimit.setText("Limit: "+c.getLimit());
                        }
                    });

                    dialog.show(getSupportFragmentManager(),"Set Limit");
                }
            }
        });
    }

    private void updateAdapterForSearch(String searchString)
    {
        List<Entry> searchEntries = entryService.searchEntries(searchString,month,category,type);
        searchAdapter.setEntries(searchEntries);
        recyclerDays.setAdapter(searchAdapter);

        txtMonthTotal.setText("Total: "+countTotal(searchEntries));
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

        float monthTotal = entryService.getMonthTotal(month,category,type);
        txtMonthTotal.setText("Total: "+monthTotal);
        adapter.updateList();
    }
}
